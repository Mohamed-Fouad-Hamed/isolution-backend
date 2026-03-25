package com.alf.inventory.repository;

import com.alf.inventory.entity.StockMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMoveRepository
        extends JpaRepository<StockMove, Long> {
}

