package com.pintor.product_module.domain.stock.repository;

import com.pintor.product_module.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByProductIdIn(List<Long> productIds);
    Optional<Stock> findByProductId(Long productId);
}