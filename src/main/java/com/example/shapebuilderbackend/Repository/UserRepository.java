package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Dto.GetAllUserProducts;
import com.example.shapebuilderbackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
        select new com.example.shapebuilderbackend.Dto.GetAllUserProducts(
            p.name, p.protein, p.fat, p.carbs, p.calories
        )
        from Product p
        where p.user.Id = :userId
    """)
    List<GetAllUserProducts> findAllByUserId(@Param("userId") Long userId);

    @Query("select u from User u left join fetch u.products where u.Id = :id")
    Optional<User> findByIdWithProducts(@Param("id") Long id);
}

