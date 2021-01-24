package com.meretskiy.internet.market.repositories;

import com.meretskiy.internet.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//интерфейс JpaSpecificationExecutor<Product> позволяет нам использовать спецификации.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
