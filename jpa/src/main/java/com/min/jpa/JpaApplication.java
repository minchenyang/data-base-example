package com.min.jpa;

import com.min.jpa.entity.ProductInfo;
import com.min.jpa.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.List;

@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

    /**
     * 直接可以调用基本的增删改查
     */
    @Autowired
    private CustomRepository customRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<ProductInfo> sql = customRepository.findSQL("1");
        System.out.println(sql);
    }
}
