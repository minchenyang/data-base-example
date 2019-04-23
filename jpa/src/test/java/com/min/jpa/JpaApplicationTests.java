package com.min.jpa;

import com.min.jpa.entity.ProductInfo;
import com.min.jpa.repository.CustomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaApplicationTests {
    @Autowired
    private CustomRepository customRepository;

    @Test
    public void contextLoads() {
        customRepository.findAll((Specification<ProductInfo>) (root, criteriaQuery, criteriaBuilder) ->
                {
                    List<Predicate> list = new ArrayList<Predicate>();

                    list.add(criteriaBuilder.equal(root.get("cartonNo").as(String.class), ""));//某普通字段

                    list.add(criteriaBuilder.equal(root.get("id").get("rCard").as(String.class), ""));//主键中某字段

                    list.add(criteriaBuilder.like(root.get("mocode").as(String.class), "%" + "" + "%"));//like

                    list.add(criteriaBuilder.between(root.get("frozenDate").as(Long.class), 0L, 1L));//between and

                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("id").get("rcard").as(String.class), "1"));//大于等于

                    list.add(root.get("id").get("lotNo").as(String.class).in(""));//in

                    //ORDER BY packdate DESC,packtime DESC
                    Predicate[] p = new Predicate[list.size()];
                    criteriaQuery.where(criteriaBuilder.and(list.toArray(p)));
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("packDate")),criteriaBuilder.desc(root.get("packTime")));

                    return criteriaQuery.getRestriction();
                }
                );
    }


}
