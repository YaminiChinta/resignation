package com.example.myjwt.controllers;

import com.example.myjwt.models.PDL;
import com.example.myjwt.models.ParentAccount;
import com.example.myjwt.payload.request.PDLRequest;
import com.example.myjwt.payload.request.ParentAccountRequest;
import com.example.myjwt.security.services.PDLService;
import com.example.myjwt.security.services.ParentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/pdl")
public class PDLController {

    @Autowired
    PDLService pdlService;

    @GetMapping("")
    public ResponseEntity<List<?>> getPDLs(){
        return ResponseEntity.ok().body(pdlService.getAllPDLs());
    }
}
