package com.example.myjwt.security.services;

import com.example.myjwt.exception.RecordNotFoundException;
import com.example.myjwt.models.PDL;
import com.example.myjwt.models.ParentAccount;
import com.example.myjwt.payload.request.ParentAccountRequest;
import com.example.myjwt.repo.PDLRepository;
import com.example.myjwt.repo.ParentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ParentAccountService {

    @Autowired
    private ParentAccountRepository parentAccountRepository;

    @Autowired
    private PDLRepository pdlRepository;

    public void addParentAccount(ParentAccountRequest parentAccountRequest){
        PDL pdl = pdlRepository.findById(parentAccountRequest.getPdlId()).orElse(null);
        if(pdl == null){
            throw new RecordNotFoundException("PDl not found");
        }

        ParentAccount parentAccount = new ParentAccount(null, parentAccountRequest.getName(), parentAccountRequest.getEdlId(), parentAccountRequest.getEldName(), pdl);

        parentAccountRepository.save(parentAccount);
    }

    public List<?> getParentAccounts(){
        List<ParentAccount> parentAccounts = parentAccountRepository.findAllByOrderByIdDesc();
        List<HashMap<String,Object>> customParentAccounts = new ArrayList<>();
        parentAccounts.forEach(pc -> {
            HashMap<String, Object> pcMap = new HashMap<>();
            pcMap.put("id",pc.getId());
            pcMap.put("name",pc.getName());
            pcMap.put("edlId",pc.getEdlId());
            pcMap.put("edlName", pc.getEldName());

            HashMap<String, Object> pdlMap = new HashMap<>();
            pdlMap.put("id", pc.getPdl().getId());
            pdlMap.put("name", pc.getPdl().getName());

            pcMap.put("pdl", pdlMap);

            customParentAccounts.add(pcMap);
        });
        return customParentAccounts;
    }
}
