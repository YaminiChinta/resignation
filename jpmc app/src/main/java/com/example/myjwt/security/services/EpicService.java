package com.example.myjwt.security.services;

import com.example.myjwt.exception.RecordNotFoundException;
import com.example.myjwt.models.Epic;
import com.example.myjwt.models.ParentAccount;
import com.example.myjwt.models.Story;
import com.example.myjwt.payload.request.EpicRequest;
import com.example.myjwt.repo.EpicRepository;
import com.example.myjwt.repo.ParentAccountRepository;
import com.example.myjwt.repo.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EpicService {

	@Autowired
	EpicRepository epicRepository;

	@Autowired
	ParentAccountRepository parentAccountRepository;

	@Autowired
	StoryRepository storyRepository;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Transactional
	public void addService(EpicRequest theEpicRequest) {
		ParentAccount parentAccount = parentAccountRepository.findById(theEpicRequest.getParentAccountId())
				.orElse(null);
		if (parentAccount == null) {
			throw new RecordNotFoundException("Parent Account not found");
		}

		Epic theEpic = new Epic(null, theEpicRequest.getName(), theEpicRequest.getDescription(),
				theEpicRequest.getExpectedStoryPoint(), theEpicRequest.getEta(), parentAccount);
		epicRepository.save(theEpic);
	}

	public List<?> getAllEpics() {
		List<Epic> epics = epicRepository.findAllByOrderByIdDesc();
		List<HashMap<String, Object>> customEpics = new ArrayList<>();
		epics.forEach(epic -> {
			HashMap<String, Object> epicMap = new HashMap<>();
			epicMap.put("id", epic.getId());
			epicMap.put("name", epic.getName());
			epicMap.put("description", epic.getDescription());
			epicMap.put("expectedStoryPoint", epic.getExpectedStoryPoint());
			epicMap.put("eta", epic.getEta());

			customEpics.add(getCustomEpic(epic));
		});
		return customEpics;
	}

	public HashMap<String, Double> getEpicsStatus() {
		List<Epic> epicList = epicRepository.findAll();
		double completed = 0;
		for (Epic epic : epicList) {
			Optional<List<Story>> storyList = storyRepository.findByEpic(epic);
			if (storyList.isPresent()
					&& storyList.get().stream().filter(story -> story.getCurrentStatus().equals("Accepted"))
							.collect(Collectors.toList()).size() == storyList.get().size())
				completed++;
		}
		HashMap<String, Double> response = new HashMap<>();
		response.put("totalEpics", Double.valueOf(epicList.size()));
		response.put("completedEpics", completed);
		return response;
	}



	public HashMap<String, Object> getEpic(long epicId) {
		Epic epic = epicRepository.findById(epicId).orElseThrow(() -> new RecordNotFoundException("Epic not found"));
		return getCustomEpic(epic);
	}

	public List<HashMap<String, Object>> getEpicStories(long epicId) throws Exception {
		Epic epic = epicRepository.findById(epicId).orElseThrow(() -> new RecordNotFoundException("Epic not found"));
		List<Story> stories = storyRepository.findByEpic(epic)
				.orElseThrow(() -> new RecordNotFoundException("Epic not found"));
		List<HashMap<String, Object>> customStories = new ArrayList<>();
		for (Story story : stories) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", story.getId());
			map.put("subject", story.getSubject());
			map.put("creator", customUserDetailsService.loadAssignmentUserFromAssociateId(story.getCreatorId())
					.getAssociateName());
			map.put("responsible",
					customUserDetailsService.loadAssignmentUserFromAssociateId(story.getOwnerId()).getAssociateName());
			map.put("priority", story.getPriority());
			map.put("currentStatus", story.getCurrentStatus());

			customStories.add(map);
		}

		return customStories;
	}

	public long getAllEpicsCount() {
		return epicRepository.count();
	}

	public long getAllCompletedEpicsCount() {
		long completedEpics = 0L;
		List<Epic> epics = epicRepository.findAll();
		for (Epic epic : epics) {
			long totalStories = storyRepository.countByEpic(epic);
			long completedStories = storyRepository.countByEpicAndCurrentStatus(epic, "Accepted");
			if (totalStories == completedStories) {
				completedEpics++;
			}
		}

		return completedEpics;
	}

	private HashMap<String, Object> getCustomEpic(Epic epic) {
		HashMap<String, Object> epicMap = new HashMap<>();
		epicMap.put("id", epic.getId());
		epicMap.put("name", epic.getName());
		epicMap.put("description", epic.getDescription());
		epicMap.put("expectedStoryPoint", epic.getExpectedStoryPoint());
		epicMap.put("eta", epic.getEta());

		HashMap<String, Object> pcMap = new HashMap<>();
		pcMap.put("id", epic.getParentAccount().getId());
		pcMap.put("name", epic.getParentAccount().getName());

		epicMap.put("parentAccount", pcMap);

		return epicMap;
	}
}
