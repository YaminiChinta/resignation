package com.example.myjwt.repo;

import com.example.myjwt.models.PDL;
import com.example.myjwt.models.ParentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentAccountRepository  extends JpaRepository<ParentAccount,Long> {
    List<ParentAccount> findAllByOrderByIdDesc();

    List<ParentAccount> findByPdl(PDL pdl);
}