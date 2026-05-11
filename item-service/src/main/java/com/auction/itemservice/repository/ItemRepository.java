package com.auction.itemservice.repository;

import com.auction.itemservice.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategory(String category);

    @Query("""
            SELECT i.category, COUNT(i)
            FROM Item i
            GROUP BY i.category
            ORDER BY COUNT(i) DESC
            """)
    List<Object[]> getTopCategories();
}