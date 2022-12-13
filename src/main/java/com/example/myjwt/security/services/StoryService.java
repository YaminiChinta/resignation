package com.example.myjwt.security.services;

import com.example.myjwt.beans.GenCTrackerStories;
import com.example.myjwt.models.*;
<<<<<<< HEAD
=======
import com.example.myjwt.models.enm.ERole;
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
import com.example.myjwt.payload.request.StoryRequest;
import com.example.myjwt.repo.*;
import com.example.myjwt.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
=======
import java.util.*;
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
import java.util.stream.Collectors;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private EpicRepository epicRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AssignmentUserRepository assignmentUserRepository;

    public void saveStory(StoryRequest request) throws Exception {
        Story story = new Story();
        story.setComments(new ArrayList<>());
        User user = customUserDetailsService.loadUserFromContext();
        story.setSubject(request.getSubject());
        story.setDetails(request.getDetails());
        story.setOwner(customUserDetailsService.loadAssignmentUserFromAssociateId(request.getOwnerId()));
        story.setEpic(epicRepository.findById(request.getEpicId()).orElseThrow(() -> new Exception("No Epics found")));
        if (request.getSprintId() != null)
            story.setSprint(sprintRepository.findById(request.getSprintId()).orElseThrow(() -> new Exception("No sprints found")));
        story.setStoryStatus(categoryRepository.findByStatusId(AppConstants.STORY_STATUS_BACKLOG));
        story.setStoryPriority(categoryRepository.findById(request.getStoryPriority()).orElseThrow(() -> new Exception("No priority found")));
        story = storyRepository.save(story);
    }

    @Transactional
    public void updateStory(StoryRequest request) throws Exception {

        Story story = storyRepository.findById(request.getId()).orElseThrow(() -> new Exception("No story found"));
        User user = customUserDetailsService.loadUserFromContext();
<<<<<<< HEAD
        story.setSubject(request.getSubject());
        story.setDetails(request.getDetails());
        story.setOwner(customUserDetailsService.loadAssignmentUserFromAssociateId(request.getOwnerId()));
        story.setEpic(epicRepository.findById(request.getEpicId()).orElseThrow(() -> new Exception("No Epics found")));
        if (request.getSprintId() != null)
            story.setSprint(sprintRepository.findById(request.getSprintId()).orElseThrow(() -> new Exception("No sprints found")));
        story.setStoryStatus(categoryRepository.findById(request.getStoryStatus()).orElseThrow(() -> new Exception("No story status found")));
        story.setStoryPriority(categoryRepository.findById(request.getStoryPriority()).orElseThrow(() -> new Exception("No priority found")));
        if (request.getStoryPointEstimation() != null)
            story.setStoryPointEstimation(request.getStoryPointEstimation());
=======

        Set<Role> roles = user.getRoles();

        if (roles.size() == 1) {
            for (Role role : roles) {
                if (role.getName() == ERole.Associate) {
                    story.setStoryStatus(categoryRepository.findById(request.getStoryStatus()).orElseThrow(() -> new Exception("No story status found")));
                }
            }
        } else {

            story.setSubject(request.getSubject());
            story.setDetails(request.getDetails());
            story.setOwner(customUserDetailsService.loadAssignmentUserFromAssociateId(request.getOwnerId()));
            story.setEpic(epicRepository.findById(request.getEpicId()).orElseThrow(() -> new Exception("No Epics found")));
            if (request.getSprintId() != null)
                story.setSprint(sprintRepository.findById(request.getSprintId()).orElseThrow(() -> new Exception("No sprints found")));
            story.setStoryStatus(categoryRepository.findById(request.getStoryStatus()).orElseThrow(() -> new Exception("No story status found")));
            story.setStoryPriority(categoryRepository.findById(request.getStoryPriority()).orElseThrow(() -> new Exception("No priority found")));
            if (request.getStoryPointEstimation() != null)
                story.setStoryPointEstimation(request.getStoryPointEstimation());
        }
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
    }

    @Transactional
    public void addComments(Long id, String comment) throws Exception {
        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
        if (!comment.isBlank())
            story.getComments().add(comment);
    }

//    @Transactional
//    public void updateStatus(Long id, String status) throws Exception {
//        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
//        if (!status.isBlank())
//            story.setCurrentStatus(status);
//    }


    @Transactional
    public void deleteStory(Long id) throws Exception {
        storyRepository.deleteById(id);
    }

    @Transactional
    public List<Story> getAssociateStories() throws Exception {
        User user = customUserDetailsService.loadUserFromContext();
        Optional<List<Story>> stories = storyRepository.findByOwner(customUserDetailsService.loadAssignmentUserFromAssociateId(user.getAssociateId()));
<<<<<<< HEAD
        return stories.get();
=======
        return stories.orElse(new ArrayList<>());
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
    }

    @Transactional
    public List<Story> getAllStories() throws Exception {
        List<Story> stories = storyRepository.findAll();
        stories.forEach(story -> story.getOwner());
        return stories;
    }

    public List<Story> getAllStories(List<Long> associates) throws Exception {
        List<AssignmentUser> assignmentUsers = assignmentUserRepository.findByAssociateIDIn(associates);
<<<<<<< HEAD
		List<Story> stories = storyRepository.findByOwnerIn(assignmentUsers);
		stories.forEach(story -> story.getOwner());
=======
        List<Story> stories = storyRepository.findByOwnerIn(assignmentUsers);
        stories.forEach(story -> story.getOwner());
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
        return stories;
    }


    @Transactional
    public Story loadStoryById(Long id) throws Exception {
        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
        return story;
    }

    @Transactional
    public List<HashMap<String, Object>> loadAllEpics() {
        List<Epic> epicList = epicRepository.findAll();
        List<HashMap<String, Object>> response = new ArrayList<>();
        for (Epic epic : epicList) {
            HashMap<String, Object> temp = new HashMap<>();
            temp.put("id", epic.getId());
            temp.put("name", epic.getName());
            response.add(temp);
        }
        return response;
    }

    @Transactional
    public List<Category> loadAllPriorities() {
        List<Category> byCatGroup = categoryRepository.findByCatGroup(AppConstants.STORY_PRIORITY);

        return byCatGroup;
    }

    @Transactional
    public List<Category> loadAllStoryStatus() {
        List<Category> byCatGroup = categoryRepository.findByCatGroup(AppConstants.STORY_STATUS);
        return byCatGroup;
    }

    @Transactional
    public List<Story> loadStoriesBySprint(Long id) throws Exception {
        Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new Exception("No sprint found"));
        List<Story> stories = storyRepository.findBySprint(sprint).orElse(new ArrayList<>());

        return stories;
    }


    @Transactional
    public void updateSprintStories(Long sprintId, List<Long> storyIds) throws Exception {
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new Exception("No sprint found"));
        List<Story> stories = storyRepository.findAll();
        for (Story story : stories) {
            if (storyIds.contains(story.getId())) {
                story.setSprint(sprint);
            }
        }
    }

    @Transactional
    public GenCTrackerStories getStoryCompletion(List<Long> associates) {
        List<AssignmentUser> assignmentUsers = assignmentUserRepository.findByAssociateIDIn(associates);
        List<Story> storyList = storyRepository.findByOwnerIn(assignmentUsers);
        List<Story> completed = storyList.stream().filter(story -> story.getStoryStatus().getGroupValue().equals(AppConstants.STORY_STATUS_ACCEPTED)).collect(Collectors.toList());
        storyList.forEach(story -> story.getOwner());
        GenCTrackerStories genCTrackerStories = new GenCTrackerStories();
        genCTrackerStories.setAcceptedStories(Long.valueOf(completed.size()));
        genCTrackerStories.setTotalStories(Long.valueOf(storyList.size()));
        return genCTrackerStories;
    }

//    public Long getAllStoriesByOwner(Long associateId){
//        return storyRepository.countByOwnerId(associateId);
//    }
//
//
//    public Long getCompletedStoriesByOwner(Long associateId){
//        return storyRepository.countByOwnerId(associateId);
//    }

}