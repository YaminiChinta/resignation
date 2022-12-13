package com.example.myjwt.repo;

import com.example.myjwt.models.PDL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDLRepository extends JpaRepository<PDL,Long> {
    List<PDL> findAllByOrderByIdDesc();
}