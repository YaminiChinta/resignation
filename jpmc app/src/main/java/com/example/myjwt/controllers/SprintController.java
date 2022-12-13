package com.example.myjwt.controllers;

import com.example.myjwt.payload.request.SprintCreateRequest;
import com.example.myjwt.payload.response.ApiResponse;
import com.example.myjwt.security.services.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sprint")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @PostMapping("/createSprint")
    public ResponseEntity<?> createSprint(@RequestBody SprintCreateRequest sprint) {
        try {
            sprintService.createSprint(sprint);
            return ResponseEntity.ok(new ApiResponse(true, "Sprint created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/updateSprint/{id}")
    public ResponseEntity<?> updateSprint(@RequestBody SprintCreateRequest sprint, @PathVariable Long id) {
        try {
            sprintService.updateSprint(id, sprint);
            return ResponseEntity.ok(new ApiResponse(true, "Sprint updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/deleteSprint/{id}")
    public ResponseEntity<?> deleteSprint(@PathVariable Long id) {
        try {
            sprintService.deleteSprint(id);
            return ResponseEntity.ok(new ApiResponse(true, "Sprint deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/getAllSprints")
    public ResponseEntity<?> getAllSprints() {
        return ResponseEntity.ok(sprintService.loadAllSprints());
    }
//
    @GetMapping("/getSprintById/{id}")
    public ResponseEntity<?> getSprintById(@PathVariable Long id){
        try {
            SprintCreateRequest sprint = sprintService.loadSprintById(id);
            return ResponseEntity.ok(sprint);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(false,e.getMessage()));
        }
    }



}