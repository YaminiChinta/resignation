package com.example.myjwt.repo;

import com.example.myjwt.models.SBU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SBURepository extends JpaRepository<SBU,Long> {
    List<SBU> findAllByOrderByIdDesc();
}