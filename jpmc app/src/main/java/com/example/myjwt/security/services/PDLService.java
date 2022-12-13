package com.example.myjwt.security.services;

import com.example.myjwt.exception.RecordNotFoundException;
import com.example.myjwt.models.Epic;
import com.example.myjwt.models.PDL;
import com.example.myjwt.models.ParentAccount;
import com.example.myjwt.models.SBU;
import com.example.myjwt.payload.request.EpicRequest;
import com.example.myjwt.payload.request.PDLRequest;
import com.example.myjwt.repo.EpicRepository;
import com.example.myjwt.repo.PDLRepository;
import com.example.myjwt.repo.ParentAccountRepository;
import com.example.myjwt.repo.SBURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PDLService {
    @Autowired
    PDLRepository pdlRepository;

    @Autowired
    SBURepository sbuRepository;

    public void addPDL(PDLRequest pdlRequest){
        SBU sbu = sbuRepository.findById(pdlRequest.getSbuId()).orElse(null);
        if(sbu == null){
            throw new RecordNotFoundException("SBU not found");
        }

        PDL thePdl = new PDL(null, pdlRequest.getName(), sbu);
    }

    public List<?> getAllPDLs(){
        List<PDL> pdls = pdlRepository.findAllByOrderByIdDesc();
        List<HashMap<String,Object>> customPDLs = new ArrayList<>();
        pdls.forEach(pdl -> {
            HashMap<String, Object> pdlMap = new HashMap<>();
            pdlMap.put("id",pdl.getId());
            pdlMap.put("name",pdl.getName());

            HashMap<String, Object> sbuMap = new HashMap<>();
            sbuMap.put("id", pdl.getSbu().getId());
            sbuMap.put("name", pdl.getSbu().getName());

            pdlMap.put("sbu", sbuMap);

            customPDLs.add(pdlMap);
        });
        return customPDLs;
    }
}
