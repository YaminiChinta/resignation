package com.example.myjwt.security.services;

import com.example.myjwt.beans.GenCTrackerEpics;
import com.example.myjwt.exception.RecordNotFoundException;
import com.example.myjwt.models.AssignmentUser;
import com.example.myjwt.models.Epic;
import com.example.myjwt.models.Story;
import com.example.myjwt.payload.request.EpicRequest;
import com.example.myjwt.payload.response.EpisListItem;
import com.example.myjwt.repo.AssignmentUserRepository;
import com.example.myjwt.repo.CategoryRepository;
import com.example.myjwt.repo.EpicRepository;
import com.example.myjwt.repo.StoryRepository;
import com.example.myjwt.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EpicService {

	@Autowired
	EpicRepository epicRepository;

	@Autowired
	StoryRepository storyRepository;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	private AssignmentUserRepository assignmentUserRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional
	public void addService(EpicRequest theEpicRequest) {
		Epic theEpic = new Epic(null, theEpicRequest.getName(), theEpicRequest.getDescription(),
				theEpicRequest.getExpectedStoryPoint(), theEpicRequest.getEta());
		epicRepository.save(theEpic);
	}

	public List<?> getAllEpics() {
		List<Epic> epics = epicRepository.findAllByOrderByIdDesc();
		List<EpisListItem> customEpics = new ArrayList<>();
		epics.forEach(epic -> {
			customEpics.add(getCustomEpic(epic));
		});
		return customEpics;
	}

	public List<EpisListItem> getAllEpics(List<Long> associates) {
		List<AssignmentUser> assignmentUsers = assignmentUserRepository.findByAssociateIDIn(associates);
		List<Story> storyList = storyRepository.findByOwnerIn(assignmentUsers);
		Set<Long> epicIds = new HashSet<>();
		storyList.forEach(story -> {
			epicIds.add(story.getEpic().getId());
		});
		System.out.println(epicIds);
		List<Epic> epics = epicRepository.findAllById(epicIds);
		List<EpisListItem> customEpics = new ArrayList<>();
		epics.forEach(epic -> {
			customEpics.add(getCustomEpic(epic));
		});
		return customEpics;
	}

	public GenCTrackerEpics getEpicsStatus(List<Long> associates) {
		List<AssignmentUser> assignmentUsers = assignmentUserRepository.findByAssociateIDIn(associates);
		List<Story> storyList = storyRepository.findByOwnerIn(assignmentUsers);
		Set<Long> epicIds = new HashSet<>();
		storyList.forEach(story -> {
			epicIds.add(story.getEpic().getId());
		});
		Long completed = 0L;
		for (long epic : epicIds) {
			List<Story> epicStoryList = storyRepository.findByEpicId(epic);
			if (epicStoryList.stream()
					.filter(story -> story.getStoryStatus().getGroupValue()
							.equalsIgnoreCase(AppConstants.STORY_STATUS_ACCEPTED))
					.collect(Collectors.toList()).size() == epicStoryList.size())
				completed++;
		}
		GenCTrackerEpics genCTrackerEpics = new GenCTrackerEpics();

		genCTrackerEpics.setCompletedEpics(completed);
		genCTrackerEpics.setTotalEpics(Long.valueOf(epicIds.size()));
		return genCTrackerEpics;
	}

	public EpisListItem getEpic(long epicId) {
		Epic epic = epicRepository.findById(epicId).orElseThrow(() -> new RecordNotFoundException("Epic not found"));
		return getCustomEpic(epic);
	}

	public List<Story> getEpicStories(long epicId) throws Exception {
		Epic epic = epicRepository.findById(epicId).orElseThrow(() -> new RecordNotFoundException("Epic not found"));
		List<Story> stories = storyRepository.findByEpic(epic).orElse(new ArrayList<>());
		return stories;
	}

	public long getAllEpicsCount() {
		return epicRepository.count();
	}

	public boolean isEpicCompleted(Epic epic) {
		long totalStories = storyRepository.countByEpic(epic);
		long completedStories = storyRepository.countByEpicAndStoryStatus(epic,
				categoryRepository.findByStatusId(AppConstants.STORY_STATUS_ACCEPTED));
		return totalStories == completedStories;
	}

	public List<?> getAllCompletedEpics(List<Long> associates) {
		List<AssignmentUser> assignmentUsers = assignmentUserRepository.findByAssociateIDIn(associates);
		List<Story> storyList = storyRepository.findByOwnerIn(assignmentUsers);
		Set<Long> epicIds = new HashSet<>();
		storyList.forEach(story -> {
			epicIds.add(story.getEpic().getId());
		});
		List<Epic> epics = epicRepository.findAllById(epicIds);
		epics = epics.stream().filter(epic -> isEpicCompleted(epic)).collect(Collectors.toList());
		List<EpisListItem> completedEpics = epics.stream().map(epic -> getCustomEpic(epic)).collect(Collectors.toList());
		return completedEpics;
	}

	private EpisListItem getCustomEpic(Epic epic) {

		EpisListItem listItem = new EpisListItem();

		listItem.setName(epic.getName());
		listItem.setId(epic.getId());
		listItem.setDescription(epic.getDescription());
		listItem.setEta(epic.getEta());
		listItem.setExpectedStoryPoint(epic.getExpectedStoryPoint());
		listItem.setTotalStories(storyRepository.countByEpic(epic));
		listItem.setCompletedStories(storyRepository.countByEpicAndStoryStatus(epic,categoryRepository.findByStatusId(AppConstants.STORY_STATUS_ACCEPTED)));

		return listItem;
	}
}