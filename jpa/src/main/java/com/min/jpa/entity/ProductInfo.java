package com.min.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity//声明类为实体或表
@Table(name = "product_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    @Id//主键
    @Column(name = "product_id")
    private String productId;
    //名字
    @Column(name = "product_name")
    private String productName;
    //单价
    @Column(name = "product_price")
    private String productPrice;
    //库存
    @Column(name = "product_stock")
    private String productStock;
    //描述
    @Column(name = "product_description")
    private String productDescription;
    //小图
    @Column(name = "product_icon")
    private String productIcon;
    //类目编号
    @Column(name = "category_type")
    private Integer categoryType;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}
