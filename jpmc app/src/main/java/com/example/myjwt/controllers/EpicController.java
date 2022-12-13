package com.example.myjwt.controllers;

import com.example.myjwt.models.Epic;
import com.example.myjwt.payload.request.EpicRequest;
import com.example.myjwt.payload.response.ApiResponse;
import com.example.myjwt.security.services.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/epic")
public class EpicController {

    @Autowired
    EpicService epicService;

    @PostMapping("")
    public ResponseEntity<?> addEpic(@RequestBody EpicRequest epicRequest){
        epicService.addService(epicRequest);
        return ResponseEntity.ok().body(new ApiResponse(true, "Epic created"));
    }

    @GetMapping("")
    public ResponseEntity<List<?>> getEpics(){
        return ResponseEntity.ok().body(epicService.getAllEpics());
    }

    @GetMapping("/{epicId}")
    public ResponseEntity<?> getEpic(@PathVariable long epicId){
        return ResponseEntity.ok().body(epicService.getEpic(epicId));
    }

    @GetMapping("/{epicId}/stories")
    public ResponseEntity<List<?>> getEpicStories(@PathVariable long epicId) throws Exception {
        return ResponseEntity.ok().body(epicService.getEpicStories(epicId));
    }
}
