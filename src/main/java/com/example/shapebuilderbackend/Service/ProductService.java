package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.BadRequestException;
import com.example.shapebuilderbackend.Exception.ForbiddenException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<GetAllProductsResponse> getAllProducts(){
        return productRepository.findAll().stream()
                .map(et -> new GetAllProductsResponse(et.getId(), et.getName(), et.getProtein(), et.getFat(), et.getCarbs(), et.getCalories(), et.isCustom()))
                .collect(Collectors.toList());
    };

    public AddProductResponse addProduct(AddProductRequest addProductRequest) {
        Product product = new Product();
        product.setName(addProductRequest.getName());
        product.setProtein(addProductRequest.getProtein());
        product.setFat(addProductRequest.getFat());
        product.setCarbs(addProductRequest.getCarbs());
        product.setCalories(addProductRequest.getCalories());
        product.setCustom(false);
        Product saved = productRepository.save(product);

        return new AddProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getProtein(),
                saved.getFat(),
                saved.getCarbs(),
                saved.getCalories(),
                saved.isCustom()
        );
    }

    public DtoUpdateProduct updateProduct(DtoUpdateProduct updateProductRequest) {
        Product updatedProduct = productRepository.findById(updateProductRequest.getId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu o podanym id: "+ updateProductRequest.getId()));
        updatedProduct.setName(updateProductRequest.getName());
        updatedProduct.setProtein(updateProductRequest.getProtein());
        updatedProduct.setFat(updateProductRequest.getFat());
        updatedProduct.setCarbs(updateProductRequest.getCarbs());
        updatedProduct.setCalories(updateProductRequest.getCalories());
        Product saved = productRepository.save(updatedProduct);

        return new DtoUpdateProduct(
                saved.getId(),
                saved.getName(),
                saved.getProtein(),
                saved.getFat(),
                saved.getCarbs(),
                saved.getCalories()
        );
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu o id: "+ productId));

        productRepository.delete(product);
    }

    public DtoProductSummary calculateProductSummary(MealProduct mp) {
        if (mp == null) {
            throw new BadRequestException("Produkt nie może być pusty");
        }

        Product p = mp.getProduct();
        if (p == null) {
            throw new NotFoundException("Produkt nie może być pusty");
        }

        double amount = mp.getAmount();
        if (amount <= 0) {
            throw new ForbiddenException("Ilość musi być większa od 0");
        }

        double newAmount = amount / 100.0;

        return new DtoProductSummary(
                p.getName(),
                amount,
                p.getProtein() * newAmount,
                p.getFat() * newAmount,
                p.getCarbs() * newAmount,
                p.getCalories() * newAmount
        );
    }
}
