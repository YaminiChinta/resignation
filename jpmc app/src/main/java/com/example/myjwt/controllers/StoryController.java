package com.example.myjwt.controllers;

import com.example.myjwt.payload.request.StoryRequest;
import com.example.myjwt.payload.response.ApiResponse;
import com.example.myjwt.payload.response.StoryManagerResponse;
import com.example.myjwt.security.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/story")
public class StoryController {

    @Autowired
    private StoryService storyService;


    @PostMapping("/createStory")
    public ResponseEntity<?> createStory(@RequestBody StoryRequest request) {
        try {
            storyService.saveStory(request);
            return ResponseEntity.ok(new ApiResponse(true, "Story created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/updateStory")
    public ResponseEntity<?> updateStory(@RequestBody StoryRequest request) {
        try {
            storyService.updateStory(request);
            return ResponseEntity.ok(new ApiResponse(true, "Story updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/addComment/{storyId}")
    public ResponseEntity<?> addComments(@RequestBody String comment, @PathVariable Long storyId) {
        try {
            System.out.println(comment);
            storyService.addComments(storyId, comment);
            return ResponseEntity.ok(new ApiResponse(true, "Comment added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/deleteStory/{storyId}")
    public ResponseEntity<?> deleteStory(@PathVariable Long storyId) {
        try {
            storyService.deleteStory(storyId);
            return ResponseEntity.ok(new ApiResponse(true, "Story deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/updateStatus/{storyId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long storyId, @RequestBody String status) {
        try {
            storyService.updateStatus(storyId, status);
            return ResponseEntity.ok(new ApiResponse(true, "Status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/getAssociateStories")
    public ResponseEntity<?> getAssociateStories() {
        List<HashMap<String, Object>> associateStories = storyService.getAssociateStories();
        return ResponseEntity.ok(associateStories);
    }

    @GetMapping("/getAllStories")
    public ResponseEntity<?> getAllStories() {

        List<HashMap<String, Object>> allStories = null;
        try {
            allStories = storyService.getAllStories();
            return ResponseEntity.ok(allStories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/getAllPriorities")
    public ResponseEntity<?> getAllPriorities() {

        List<String> allStories = storyService.loadAllPriorities();
        return ResponseEntity.ok(allStories);

    }

    @GetMapping("/getAllStoryStatus")
    public ResponseEntity<?> getAllStoryStatus() {

        List<String> allStories = storyService.loadAllStoryStatus();
        return ResponseEntity.ok(allStories);

    }

    @GetMapping("/getStoryById/{id}")
    public ResponseEntity<?> getStoryById(@PathVariable Long id) {
        try {
            StoryManagerResponse storyManagerResponse = storyService.loadStoryById(id);
            return ResponseEntity.ok(storyManagerResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/getAllEpics")
    public ResponseEntity<?> getAllEpics() {
        List<HashMap<String, Object>> epics = storyService.loadAllEpics();
        return ResponseEntity.ok(epics);
    }



    @PutMapping("/updateSprintStories/{sprintId}")
    public ResponseEntity<?> updateSprintStories(@PathVariable Long sprintId,@RequestBody List<Long> storyIds){
        try {
            storyService.updateSprintStories(sprintId,storyIds);
            return ResponseEntity.ok(new ApiResponse(true,"Sprint Stories Updated Successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse(false, e.getMessage()));
        }
    }


}