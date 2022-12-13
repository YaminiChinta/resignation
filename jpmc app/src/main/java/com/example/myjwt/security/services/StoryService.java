package com.example.myjwt.security.services;

import com.example.myjwt.beans.GenCTrackerStories;
import com.example.myjwt.models.*;
import com.example.myjwt.payload.request.StoryRequest;
import com.example.myjwt.payload.response.StoryManagerResponse;
import com.example.myjwt.repo.CategoryRepository;
import com.example.myjwt.repo.EpicRepository;
import com.example.myjwt.repo.SprintRepository;
import com.example.myjwt.repo.StoryRepository;
import com.example.myjwt.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

    public void saveStory(StoryRequest request) throws Exception {
        Story story = new Story();
        story.setComments(new ArrayList<>());
        User user = customUserDetailsService.loadUserFromContext();
        story.setSubject(request.getSubject());
        story.setDetails(request.getDetails());
        story.setOwnerId(request.getOwnerId());
        story.setCreatorId(user.getAssociateId());
        story.setEpic(epicRepository.findById(request.getEpicId()).orElseThrow(() -> new Exception("No Epics found")));
        if (request.getSprintId() != null)
            story.setSprint(sprintRepository.findById(request.getSprintId()).orElseThrow(() -> new Exception("No sprints found")));
        story.setCurrentStatus("Not Started");
        if (request.getAcceptanceStatus() != null)
            story.setAcceptanceStatus(request.getAcceptanceStatus());
        if (request.getPriority() != null)
            story.setPriority(request.getPriority());
        story = storyRepository.save(story);
    }

    @Transactional
    public void updateStory(StoryRequest request) throws Exception {
        Story story = storyRepository.findById(request.getId()).orElseThrow(() -> new Exception("No story found"));
        User user = customUserDetailsService.loadUserFromContext();
        story.setSubject(request.getSubject());
        story.setDetails(request.getDetails());
        story.setOwnerId(request.getOwnerId());
        story.setCreatorId(user.getAssociateId());
        story.setEpic(epicRepository.findById(request.getEpicId()).orElseThrow(() -> new Exception("No Epics found")));
        if (request.getSprintId() != null)
            story.setSprint(sprintRepository.findById(request.getSprintId()).orElseThrow(() -> new Exception("No sprints found")));
        if (request.getAcceptanceStatus() != null)
            story.setAcceptanceStatus(request.getAcceptanceStatus());
        if (request.getCurrentStatus() != null)
            story.setCurrentStatus(request.getCurrentStatus());
        if (request.getPriority() != null)
            story.setPriority(request.getPriority());
        if (request.getStoryPointEstimation() != null)
            story.setStoryPointEstimation(request.getStoryPointEstimation());
    }

    @Transactional
    public void addComments(Long id, String comment) throws Exception {
        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
        if (!comment.isBlank())
            story.getComments().add(comment);
    }

    @Transactional
    public void updateStatus(Long id, String status) throws Exception {
        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
        if (!status.isBlank())
            story.setCurrentStatus(status);
    }


    @Transactional
    public void deleteStory(Long id) throws Exception {
        storyRepository.deleteById(id);
    }

    @Transactional
    public List<HashMap<String, Object>> getAssociateStories() {
        User user = customUserDetailsService.loadUserFromContext();
        Optional<List<Story>> stories = storyRepository.findByOwnerId(user.getAssociateId());
        List<HashMap<String, Object>> response = new ArrayList<>();
        if (stories.isPresent()) {
            for (Story story : stories.get()) {
                HashMap<String, Object> temp = new HashMap<>();
                temp.put("id", story.getId());
                temp.put("subject", story.getSubject());
                temp.put("creator", story.getCreatorId());
                temp.put("storyPoint", story.getStoryPointEstimation());
                response.add(temp);
            }
        }
        return response;
    }

    @Transactional
    public List<HashMap<String, Object>> getAllStories() throws Exception {
        List<Story> stories = storyRepository.findAll();
        List<HashMap<String, Object>> response = new ArrayList<>();

        for (Story story : stories) {
            HashMap<String, Object> temp = new HashMap<>();
            temp.put("id", story.getId());
            temp.put("subject", story.getSubject());
            temp.put("creator", customUserDetailsService.loadAssignmentUserFromAssociateId(story.getCreatorId()).getAssociateName());
            temp.put("responsible", customUserDetailsService.loadAssignmentUserFromAssociateId(story.getOwnerId()).getAssociateName());
            temp.put("priority", story.getPriority());
            temp.put("epic", story.getEpic().getName());
            temp.put("currentStatus",story.getCurrentStatus());
            response.add(temp);
        }
        return response;
    }


    @Transactional
    public StoryManagerResponse loadStoryById(Long id) throws Exception {
        Story story = storyRepository.findById(id).orElseThrow(() -> new Exception("No story found"));
        StoryManagerResponse storyManagerResponse = new StoryManagerResponse();
        storyManagerResponse.setId(story.getId());
        storyManagerResponse.setSubject(story.getSubject());
        storyManagerResponse.setDetails(story.getDetails());
        storyManagerResponse.setCreatorId(story.getCreatorId());
        storyManagerResponse.setCurrentStatus(story.getCurrentStatus());
        storyManagerResponse.setStoryPointEstimation(story.getStoryPointEstimation());
        storyManagerResponse.setAcceptanceStatus(story.getAcceptanceStatus());
        storyManagerResponse.setComments(story.getComments());
        storyManagerResponse.setPriority(story.getPriority());
        storyManagerResponse.setOwnerId(story.getOwnerId());
        storyManagerResponse.setEpicId(story.getEpic().getId());
        return storyManagerResponse;
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
    public List<String> loadAllPriorities() {
        List<Category> byCatGroup = categoryRepository.findByCatGroup(AppConstants.STORY_PRIORITY);
        List<String> priorities = new ArrayList<>();
        for (Category cat : byCatGroup) {
            priorities.add(cat.getGroupValue());
        }
        return priorities;
    }

    @Transactional
    public List<String> loadAllStoryStatus() {
        List<Category> byCatGroup = categoryRepository.findByCatGroup(AppConstants.STORY_STATUS);
        List<String> statuses = new ArrayList<>();
        for (Category cat : byCatGroup) {
            statuses.add(cat.getGroupValue());
        }
        return statuses;
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
        List<Story> storyList = storyRepository.findByOwnerIdIn(associates);
        List<Story> completed = storyList.stream().filter(story -> story.getCurrentStatus().equals("Accepted")).collect(Collectors.toList());
        GenCTrackerStories genCTrackerStories = new GenCTrackerStories();
        genCTrackerStories.setAcceptedStories(Double.valueOf(completed.size()));
        genCTrackerStories.setTotalStories(Double.valueOf(storyList.size()));
        return genCTrackerStories;
    }


}