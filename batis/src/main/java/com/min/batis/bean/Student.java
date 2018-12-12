package com.min.batis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: data-base-example
 * @description:
 * @author: mcy
 * @create: 2018-11-06 12:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private Double sal;
}
