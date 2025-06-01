package com.example.SearchService.services;

import com.example.SearchService.config.RabbitMQConfig;
import com.example.SearchService.dto.ProductDTO;
import com.example.SearchService.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductEventConsumer {

    @Autowired
    private RedisTemplate<String, List<Product>> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Product convertToProduct(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setMerchantId(dto.getMerchantId());
        product.setDiscount(dto.getDiscount());
        product.setStock(dto.getStock());
        System.out.println(dto.getStock() + " PRODUCT STOCK  " + product.getStock());
        return product;
    }


    private Comparator<Product> getSortComparator(String query) {
        if (query.contains("sort:price_asc")) {
            return Comparator.comparing(Product::getPriceAfterDiscount);
        } else if (query.contains("sort:price_desc")) {
            return Comparator.comparing(Product::getPriceAfterDiscount).reversed();
        }
        return null;
    }


    private boolean doesProductMatchQuery(Product product, String queryKey) {
        String lowerQuery = queryKey.toLowerCase();

        boolean nameMatches = product.getName() != null &&
                extractValue(lowerQuery, "name:")
                        .map(qName -> product.getName().toLowerCase().contains(qName))
                        .orElse(true);

        boolean categoryMatches = product.getCategory() != null &&
                lowerQuery.contains("category:" + product.getCategory().toString().toLowerCase());

        return nameMatches || categoryMatches;
    }

    private Optional<String> extractValue(String query, String key) {
        int start = query.indexOf(key);
        if (start == -1) return Optional.empty();
        start += key.length();
        int end = query.indexOf("-", start);
        if (end == -1) end = query.length();
        String value = query.substring(start, end);
        return "_".equals(value) ? Optional.empty() : Optional.of(value);
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_CREATE_QUEUE)
    public void handleProductCreate(ProductDTO dto) {
        try {
        Product product = convertToProduct(dto);
        System.out.println("📦 Received CREATE product: " + product);

        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                System.out.println("🔍 Checking key: " + key);
                if (doesProductMatchQuery(product, key)) {
                    try {
                        System.out.println("✅ Adding product to key: " + key);
                        List<Product> cached = redisTemplate.opsForValue().get(key);
                        if (cached == null) cached = new ArrayList<>();
                        cached.add(product);
                        redisTemplate.opsForValue().set(key, cached);
                    } catch (Exception e) {
                        System.err.println("⚠️ CREATE: Skipping corrupt key '" + key + "': " + e.getMessage());
                        // Optionally clean: redisTemplate.delete(key);
                    }
                }
            }
        }
    }
    catch (Exception e) {
            System.err.println("Error handling product creation: " + e.getMessage());
    }

    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_UPDATE_QUEUE)
    public void handleProductUpdate(ProductDTO dto) {
        Product product = convertToProduct(dto);
        System.out.println("✏️ Received UPDATE product: " + product);

        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                try {
                    List<Product> cached = redisTemplate.opsForValue().get(key);
                    if (cached == null) continue;

                    boolean found = false;
                    for (int i = 0; i < cached.size(); i++) {
                        if (cached.get(i).getId().equals(product.getId())) {
                            if (doesProductMatchQuery(product, key)) {
                                cached.set(i, product);
                            } else {
                                cached.remove(i);
                            }
                            found = true;
                            break;
                        }
                    }

                    if (!found && doesProductMatchQuery(product, key)) {
                        cached.add(product);
                    }

                    redisTemplate.opsForValue().set(key, cached);
                } catch (ClassCastException | SerializationException e) {
                    System.err.println("⚠️ UPDATE: Skipping corrupt key '" + key + "': " + e.getMessage());
                    // redisTemplate.delete(key);
                }
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_DELETE_QUEUE)
    public void handleProductDelete(ProductDTO dto) {
        System.out.println("🗑️ Received DELETE product: " + dto);

        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            for (String key : keys) {
                try {
                    List<Product> cached = redisTemplate.opsForValue().get(key);
                    if (cached == null) continue;

                    cached.removeIf(p -> p.getId().equals(dto.getId()));
                    redisTemplate.opsForValue().set(key, cached);
                } catch (ClassCastException | SerializationException e) {
                    System.err.println("⚠️ DELETE: Skipping corrupt key '" + key + "': " + e.getMessage());
                    // redisTemplate.delete(key);
                }
            }
        }
    }
}
