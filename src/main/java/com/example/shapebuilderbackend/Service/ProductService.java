package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.GetAllProductsResponse;
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
                .map(et -> new GetAllProductsResponse(et.getId(), et.getName(), et.getProtein(), et.getFat(), et.getCarbs(), et.getCalories()))
                .collect(Collectors.toList());
    };
}
