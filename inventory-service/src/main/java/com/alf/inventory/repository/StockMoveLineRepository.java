package com.alf.inventory.repository;

import com.alf.inventory.entity.StockMoveLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMoveLineRepository  extends JpaRepository<StockMoveLine, Long> {
   List<StockMoveLine> findByMoveId(Long movId);
}
