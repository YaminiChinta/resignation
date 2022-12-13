package com.example.myjwt.controllers;

import com.example.myjwt.models.SBU;
import com.example.myjwt.payload.request.SBURequest;
import com.example.myjwt.security.services.SBUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/sbu")
public class SBUController {

    @Autowired
    SBUService sbuService;

    @GetMapping("")
    public ResponseEntity<List<SBU>> getSBUs(){
        return ResponseEntity.ok().body(sbuService.getAllSBUs());
    }
}
