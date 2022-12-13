package com.example.myjwt.security.services;

import com.example.myjwt.beans.GenCTrackerSprints;
import com.example.myjwt.models.Sprint;
import com.example.myjwt.models.Story;
import com.example.myjwt.payload.request.SprintCreateRequest;
import com.example.myjwt.repo.SprintRepository;
import com.example.myjwt.repo.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SprintService {


    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private StoryService storyService;

    @Autowired
    private StoryRepository storyRepository;


    public void createSprint(SprintCreateRequest request) throws Exception {
        Sprint sprint  = new Sprint();
        sprint.setName(request.getName());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setScrumMasterId(request.getScrumMasterId());
        sprint.setScrumMasterName(customUserDetailsService.loadAssignmentUserFromAssociateId(sprint.getScrumMasterId()).getAssociateName());
        sprint = sprintRepository.save(sprint);
        System.out.println("Reached");
        storyService.updateSprintStories(sprint.getId(),request.getStoryIds());
        System.out.println("Reached 2");
    }

    @Transactional
    public void updateSprint(Long id,SprintCreateRequest request) throws Exception {
        Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new Exception("No sprints found"));
        sprint.setName(request.getName());
        sprint.setStartDate(request.getStartDate());
        sprint.setEndDate(request.getEndDate());
        sprint.setScrumMasterId(request.getScrumMasterId());
        sprint.setScrumMasterName(customUserDetailsService.loadAssignmentUserFromAssociateId(sprint.getScrumMasterId()).getAssociateName());
        storyService.updateSprintStories(id,request.getStoryIds());
    }

    @Transactional
    public SprintCreateRequest loadSprintById(Long id) throws Exception {
        Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new Exception("No sprints found"));
        SprintCreateRequest response = new SprintCreateRequest();
        response.setName(sprint.getName());
        response.setStartDate(sprint.getStartDate());
        response.setEndDate(sprint.getEndDate());
        response.setScrumMasterId(sprint.getScrumMasterId());
        response.setScrumMasterName(customUserDetailsService.loadAssignmentUserFromAssociateId(sprint.getScrumMasterId()).getAssociateName());
        List<Long> storyList = storyService.loadStoriesBySprint(sprint.getId()).stream().map((story) -> story.getId()).collect(Collectors.toList());
        response.setStoryIds(storyList);
        return response;
    }

    @Transactional
    public void deleteSprint(Long id){
        sprintRepository.deleteById(id);
    }

    public List<Sprint> loadAllSprints(){
        return sprintRepository.findAll();
    }

    @Transactional
    public GenCTrackerSprints getSprintsCompletion() throws Exception {
        List<Sprint> sprintList = sprintRepository.findAll();
        Double completed = 0D;
        for(Sprint sprint : sprintList){
            List<Story> stories = storyService.loadStoriesBySprint(sprint.getId());
            if(stories.stream().filter(story -> story.getCurrentStatus().equals("Accepted")).collect(Collectors.toList()).size()==stories.size()){
                completed++;
            }
        }
        GenCTrackerSprints genCTrackerSprints = new GenCTrackerSprints();
        genCTrackerSprints.setTotalSprints(Double.valueOf(sprintList.size()));
        genCTrackerSprints.setCompletedSprints(completed);
        return genCTrackerSprints;

    }

    public List<Sprint> getActiveSprints(){
        List<Sprint> sprints = sprintRepository.findAll();
        List<Sprint> activeSprints = new ArrayList<>();

        sprints.forEach(sprint -> {
            Instant current = Instant.now();
            Instant startDate = sprint.getStartDate();
            Instant endDate = sprint.getEndDate();
            if(current.isAfter(startDate) && current.isBefore(endDate)){
                activeSprints.add(sprint);
            }
        });

        return activeSprints;
    }

    public long getActiveSprintsOwnerCount(){
        List<Sprint> activeSprints = getActiveSprints();
        Set<Long> owners = new HashSet<>();
        activeSprints.forEach(sprint -> {
            storyRepository.findBySprint(sprint).orElse(new ArrayList<>()).forEach(story -> {
                owners.add(story.getOwnerId());
            });
        });

        return owners.size();
    }

}