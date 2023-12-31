package com.pbl6.hotelbookingapp.repository;

import com.pbl6.hotelbookingapp.entity.BedType;
import com.pbl6.hotelbookingapp.entity.View;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Integer> {
    Optional<View> findByName(String name);
    Page<View> findByNameContaining(String name, Pageable pageable);

}
