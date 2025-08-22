package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddUserProductRequest;
import com.example.shapebuilderbackend.Dto.DtoProductSummary;
import com.example.shapebuilderbackend.Dto.GetAllProductsResponse;
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
                Math.round(p.getProtein() * newAmount),
                Math.round(p.getFat() * newAmount),
                Math.round(p.getCarbs() * newAmount),
                Math.round(p.getCalories() * newAmount)
        );
    }
}
