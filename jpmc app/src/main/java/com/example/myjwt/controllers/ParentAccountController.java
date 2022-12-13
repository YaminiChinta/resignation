package com.example.myjwt.controllers;

import com.example.myjwt.models.Epic;
import com.example.myjwt.models.ParentAccount;
import com.example.myjwt.payload.request.EpicRequest;
import com.example.myjwt.payload.request.ParentAccountRequest;
import com.example.myjwt.security.services.EpicService;
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
@RequestMapping("/api/parent-account")
public class ParentAccountController {

    @Autowired
    ParentAccountService parentAccountService;

    @GetMapping("")
    public ResponseEntity<List<?>> getParentAccounts(){
        return ResponseEntity.ok().body(parentAccountService.getParentAccounts());
    }
}
