package com.example.Transaction.Service;

import com.example.Transaction.Model.Product;
import com.example.Transaction.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public Product getProduct(Long productId) {
        return repository.findById(productId).get();
    }

    public void deleteProduct(Long productID)
    {
        repository.deleteById(productID);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}