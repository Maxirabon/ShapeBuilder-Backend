package com.example.shapebuilderbackend.Component;

import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ProductInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            return;
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("products.sql")) {
            if (is == null) {
                throw new NotFoundException("Nie znaleziono pliku products.sql w resources");
            }

            List<Product> products = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .filter(line -> !line.isBlank())
                    .map(this::parseProduct)
                    .toList();

            productRepository.saveAll(products);
        }
    }

    private Product parseProduct(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Nieprawidłowy format linii: " + line);
        }

        Product product = new Product();
        product.setName(parts[0].trim());
        product.setProtein(Integer.parseInt(parts[1].trim()));
        product.setFat(Integer.parseInt(parts[2].trim()));
        product.setCarbs(Integer.parseInt(parts[3].trim()));
        product.setCalories(Integer.parseInt(parts[4].trim()));
        product.setCustom(false);
        product.setUser(null);

        return product;
    }
}
