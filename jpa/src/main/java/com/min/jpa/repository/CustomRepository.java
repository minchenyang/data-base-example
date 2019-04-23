package com.min.jpa.repository;

import com.min.jpa.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository extends JpaRepository<ProductInfo, String> , JpaSpecificationExecutor<ProductInfo> {

    //根据属性来查询
    ProductInfo findByproductName(String productName);
    //还能支持多个属性组合
    ProductInfo findByproductNameAndProductPrice(String productName, String productPrice);
    //自定义查询
    @Query(nativeQuery = true, value = "SELECT * FROM product_info")
    List<ProductInfo> findSQL(@Param("productName1") String productName1);
}
