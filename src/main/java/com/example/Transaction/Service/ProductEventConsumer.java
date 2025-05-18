package com.example.Transaction.Service;

import com.example.Transaction.Model.Product;
import com.example.Transaction.Repository.ProductRepository;
import com.example.Transaction.config.RabbitMQConfig;
import com.example.Transaction.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor      // generates the constructor for final fields
public class ProductEventConsumer {

    private final ProductRepository productRepo;

    // ---------- helpers ----------
    private Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());          // if ID is DB-generated, omit this line for CREATE
        p.setName(dto.getName());
        p.setCategory(dto.getCategory());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setMerchantId(dto.getMerchantId());
        p.setDiscount(dto.getDiscount());
        p.setStock(dto.getStock());
        return p;
    }

    // ---------- CREATE ----------
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_CREATE_QUEUE)
    public void onProductCreate(ProductDTO dto) {
        Product product = toEntity(dto);
        System.out.println("📦 CREATE product → " + product);
        productRepo.save(product);              // INSERT
    }

    // ---------- UPDATE ----------
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_UPDATE_QUEUE)
    public void onProductUpdate(ProductDTO dto) {
        Product product = toEntity(dto);
        System.out.println("✏️ UPDATE product → " + product);
        productRepo.save(product);              // UPSERT (JPA will do UPDATE when ID exists)
    }

    // ---------- DELETE ----------
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.PRODUCT_DELETE_QUEUE)
    public void onProductDelete(ProductDTO dto) {
        System.out.println("🗑️ DELETE product → id=" + dto.getId());
        productRepo.deleteById(dto.getId());
    }
}
