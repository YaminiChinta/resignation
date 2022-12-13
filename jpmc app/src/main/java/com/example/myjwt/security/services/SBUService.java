package com.example.myjwt.security.services;

import com.example.myjwt.models.SBU;
import com.example.myjwt.payload.request.SBURequest;
import com.example.myjwt.repo.SBURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SBUService {

    @Autowired
    SBURepository sbuRepository;

    public void addSBU(SBURequest sbuRequest){
        SBU sbu = new SBU(null, sbuRequest.getName(), sbuRequest.getHeadId(), sbuRequest.getHeadName());
        sbuRepository.save(sbu);
    }

    public List<SBU> getAllSBUs(){
        return sbuRepository.findAllByOrderByIdDesc();
    }
}
