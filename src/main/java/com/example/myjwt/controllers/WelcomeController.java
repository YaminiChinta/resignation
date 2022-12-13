package com.example.myjwt.controllers;

import com.example.myjwt.exception.BadRequestException;
import com.example.myjwt.models.*;
import com.example.myjwt.models.enm.ECalenderMonth;
import com.example.myjwt.models.enm.ERole;
import com.example.myjwt.payload.UsersDetail;
import com.example.myjwt.payload.request.ApproveUser;
import com.example.myjwt.payload.response.ApiResponse;
import com.example.myjwt.repo.*;
import com.example.myjwt.security.services.SettingService;
import com.example.myjwt.security.services.UserService;
import com.example.myjwt.util.AppConstants;
import com.example.myjwt.util.EmailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class WelcomeController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private AssignmentReportRepository assignmentReportRepository;

	@Autowired
	AssignmentUserRepository assignmentUserRepo;

	@Autowired
	HexCodeRepository hexCodeRepository;

	@Autowired
	EvaluationResultRepository evaluationResultRepository;

	@Autowired
	EvaluationResultCategoryRepository evaluationResultCategoryRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SettingsRepository settingsRepository;

	@Autowired
	private LeaveStatusCSSRepository leaveStatusCSSRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	SettingService settingService;

	@Autowired
	HolidayRepository holidayRepository;

	@Autowired
<<<<<<< HEAD
	ResignationCategoryRepository resignationCategoryRepository;

	@Autowired
=======
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
	private EpicRepository epicRepository;

	@Autowired
	private SprintRepository sprintRepository;
	@Autowired
	private StoryRepository storyRepository;

	@GetMapping("/welcome/all")
	public String initiateWelcomePageCall() {
		String msg = "Welcome to the App. " + "Let's Login or SignUp";
		setInitialValuesInDB();
		return msg;
	}

	@GetMapping("/welcome/all/story")
	public String initiateAssignStories() {
		String msg = "Stories assigned";
		assignStories();
		return msg;
	}

	@GetMapping("/welcome")
	public String welcome() {
		String msg = "Welcome to the App. ";
		return msg;
	}

	@GetMapping("/all")
	public String allAccess() {
		return "Welcome to the App. " + "Let's Login or SignUp";
	}

	@GetMapping("/user")
	public String userAccess() {
		return "Hello user! You are authorized :) ";
	}

	private Role getFirstRole(Set<Role> roles) {
		Role firstRole = null;
		while (roles.iterator().hasNext()) {
			firstRole = roles.iterator().next();
			break;
		}

		return firstRole;
	}

	private boolean isAdmin(Set<Role> roles) {

		for (Role role : roles) {
			System.out.println("role.getName().name() = " + role.getName().name());
			if (role.getName().name().equalsIgnoreCase(ERole.Admin.name()))
				return true;
		}

		return false;
	}

	@GetMapping("/userdetail")
	public List<UsersDetail> getAllUser() {

		List<User> list = userRepository.findAll();
		List<UsersDetail> userList = new ArrayList<UsersDetail>();

		for (int i = 0; i < list.size(); i++) {

			User user = list.get(i);
			Long associateId = user.getAssociateId();

			System.out.println(
					"----------------------------------------:" + list.size() + ":::::" + user.getAssociateId());
			for (Role role : user.getRoles()) {
				System.out.println(role.getName().name() + "::"
						+ getFirstRole(user.getRoles()).getName().name().equalsIgnoreCase(ERole.HR.name()));
			}

			System.out.println("----------------------------------------::::");

			if (user.getRoles().size() == 1
					&& getFirstRole(user.getRoles()).getName().name().equalsIgnoreCase(ERole.HR.name())) {
				System.out.println("----------------------------------------HR");
				UsersDetail userDetail = new UsersDetail();
				userDetail.setAssociateId(associateId);
				userDetail.setAssociateName("HR");
				userDetail.setEmail(settingService.getEmailId(user.getAssociateId() + "", null, null));
				userDetail.setServiceLine("-");
				userDetail.setGrade("-");
				userDetail.setRoles(user.getRoles());
				userDetail.setApproved(user.getIsApproved());
				userList.add(userDetail);
			} else if (user.getRoles().size() == 1
					&& getFirstRole(user.getRoles()).getName().name().equalsIgnoreCase(ERole.TAG.name())) {
				System.out.println("----------------------------------------TAG");
				UsersDetail userDetail = new UsersDetail();
				userDetail.setAssociateId(associateId);
				userDetail.setAssociateName("TAG");
				userDetail.setEmail(settingService.getEmailId(user.getAssociateId() + "", null, null));
				userDetail.setServiceLine("-");
				userDetail.setGrade("-");
				userDetail.setRoles(user.getRoles());
				userDetail.setApproved(user.getIsApproved());
				userList.add(userDetail);
			} else if (isAdmin(user.getRoles())) {
				System.out.println("----------------------------------------ADMIN");
				UsersDetail userDetail = new UsersDetail();
				userDetail.setAssociateId(associateId);
				userDetail.setAssociateName("Admin");
				userDetail.setEmail(settingService.getEmailId(user.getAssociateId() + "", null, null));
				userDetail.setServiceLine("-");
				userDetail.setGrade("-");
				userDetail.setRoles(user.getRoles());
				userDetail.setApproved(user.getIsApproved());
				userList.add(userDetail);
			} else {
				System.out.println("----------------------------------------USER");
				List<AssignmentUser> assignmentUserList = assignmentUserRepo.findByAssociateID(associateId);
				AssignmentUser assignmentUser = assignmentUserList.get(0);
				// Map<String,String> map = new HashMap<String,String>();
				UsersDetail userDetail = new UsersDetail();
				userDetail.setAssociateId(associateId);
				userDetail.setAssociateName(assignmentUser.getAssociateName());
				userDetail.setEmail(settingService.getEmailId(user.getAssociateId() + "", null, null));
				userDetail.setServiceLine(assignmentUser.getServiceLine());
				userDetail.setGrade(assignmentUser.getGradeDescription());
				userDetail.setRoles(user.getRoles());
				userDetail.setApproved(user.getIsApproved());
				userList.add(userDetail);
			}

		}

		System.out.println("userList size = " + userList.size());

		return userList;
	}

	@PostMapping("/approval")
	public ResponseEntity<?> approveUser(@Valid @RequestBody List<ApproveUser> approveUser) {

		System.out.println(approveUser);
		for (int i = 0; i < approveUser.size(); i++) {
			Long associateId = approveUser.get(i).getAssociateId();
			Boolean approved = approveUser.get(i).getApproved();
			System.out.println(associateId);
			User user = userRepository.findByAssociateId(associateId).get();
			Set<Role> dbRoles = user.getRoles();

			String[] roles = approveUser.get(i).getRoles();

			Set<Role> userRole = new HashSet<Role>();

			for (int j = 0; j < roles.length; j++) {
				userRole.add(getRole(roles[j]));
			}
			userRole.addAll(dbRoles);
			user.setRoles(userRole);
			if (!user.getIsApproved()) {
				user.setIsApproved(approved);
			}
			userRepository.save(user);

		}

		return ResponseEntity.ok().body(new ApiResponse(true, "successfully"));
	}

	public Role getRole(String name) {
		return roleRepository.findByName(ERole.valueOf(name));
	}

	@GetMapping("/verify/{vcode}")
	public String verifyUser(@PathVariable String vcode) {
		System.out.println(vcode);
		Hexcode hexCode = hexCodeRepository.findByCode(vcode);
		if (hexCode == null) {
			return "verify_failed! Verification invalid or already verified!";
		} else {

			switch (hexCode.getTableName()) {
			case AppConstants.TBL_USER:
				switch (hexCode.getAction()) {
				case AppConstants.HEXCODE_ACTION_VALIDATE:
					switch (hexCode.getSubAction()) {
					case AppConstants.HEXCODE_SUBACTION_EMAIL:

						User user = userRepository.findById(hexCode.getRefId())
								.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

						// user.setIsVerified(true);
						updateUserAndDeleteHexCode(user, hexCode);

						return "verify_success!!!   Login to explore!!!";
					}
					break;
				}
				break;
			}

			return "Could not find relevant authentication !!!";
		}

	}

	@Transactional
	private void updateUserAndDeleteHexCode(User user, Hexcode hexCode) {
		userRepository.save(user);
		hexCodeRepository.delete(hexCode);
	}

	private void createDefaultRoles() {
		ERole roles[] = ERole.values();
		for (ERole role : roles) {
			Role r = new Role(role);
			roleRepository.save(r);
		}
		roleRepository.flush();
	}

	private void createDefaultBillableCategories() {

		String[] billabilityCategories = { "Billable", "Planned Billable (NBL)", "Planned Billable (Billable)",
				"Training (NBL)", "Training (Billable)", "Planned to Release", "Release Initiated",
				AppConstants.NO_BILLABILITY_PLAN, "Duplicate Allocation" };

		for (int i = 0; i < billabilityCategories.length; i++) {
			Category category = new Category();
			category.setCatGroup(AppConstants.CATEGORY_BILLABILITY);
			category.setGroupKey(AppConstants.CATEGORY_BILLABILITY);
			category.setGroupValue(billabilityCategories[i]);
			categoryRepository.save(category);
			categoryRepository.flush();
		}
	}

	private Category createDefaultStoryStatusCategories() {

		String[] statusCategories = { AppConstants.STORY_STATUS_BACKLOG, AppConstants.STORY_STATUS_READY,
				AppConstants.STORY_STATUS_INPROGRESS, AppConstants.STORY_STATUS_ACCEPTED };

		Category defaultCategory = null;

		for (int i = 0; i < statusCategories.length; i++) {
			Category category = new Category();
			if (i == 0)
				defaultCategory = category;
			category.setCatGroup(AppConstants.STORY_STATUS);
			category.setGroupKey(AppConstants.STORY_STATUS);
			category.setGroupValue(statusCategories[i]);
			categoryRepository.save(category);
			categoryRepository.flush();
		}
		return defaultCategory;
	}

	private void createDefaultReferredCategories() {

		String[] referralCategories = { "Referred", "Interview scheduled", "Screen Reject", "Interview Rejected",
				"Interview Selected", "Client Interview scheduled", "Client Screen Reject", "Client Interview Rejected",
				"Client Interview Selected", "Offer in progress", "Offered", "Joined", "Offer Declined",
				"Rejected: No approval" };

		for (int i = 0; i < referralCategories.length; i++) {
			Category category = new Category();
			category.setCatGroup(AppConstants.CATEGORY_STATUS);
			category.setGroupKey(AppConstants.CATEGORY_STATUS);
			category.setGroupValue(referralCategories[i]);
			categoryRepository.save(category);
			categoryRepository.flush();
		}
		String[] leaveCategories = { "W", "L", "H", "HD", "S" };

	}

	private void createLeaveCategories() {

		String[] leaveCategories = { "W", "L", "H", "HD", "S" };

		for (int i = 0; i < leaveCategories.length; i++) {
			Category category = new Category();
			category.setCatGroup(AppConstants.LEAVE_STATUS);
			category.setGroupKey(AppConstants.LEAVE_STATUS);
			category.setGroupValue(leaveCategories[i]);
			categoryRepository.save(category);

		}
		categoryRepository.flush();
	}

	private void createLeaveStatus() {

		Object[][] leaveCategories = { { "W", new HashMap<>(Map.of("backgroundColor", "white", "color", "#D2D1D1")) },
				{ "L", new HashMap<>(Map.of("backgroundColor", "#FF0000", "color", "white")) },
				{ "H", new HashMap<>(Map.of("backgroundColor", "#f5db93", "cursor", "context-menu", "color", "blue")) },
				{ "HD", new HashMap<>(Map.of("backgroundColor", "#FF9696", "color", "white")) }, { "S", new HashMap<>(
						Map.of("backgroundColor", "#EDEDED", "cursor", "context-menu", "color", "#C2C2C2")) } };

		for (int i = 0; i < leaveCategories.length; i++) {
			LeaveStatusCSS leaveStatusCSS = new LeaveStatusCSS();
			leaveStatusCSS.setLeaveCategory(String.valueOf(leaveCategories[i][0]));
			leaveStatusCSS.setCss((HashMap<String, String>) leaveCategories[i][1]);
			leaveStatusCSSRepository.save(leaveStatusCSS);
		}
		leaveStatusCSSRepository.flush();

	}

<<<<<<< HEAD
	private void createResignationCategoriesTable(){
		String[] resignationCategories = { "Compensation", "Promotion", "Role Change",
				"Education", "Oppurtunities" };
		for (int i = 0; i < resignationCategories.length; i++) {
			ResignationCategory resignationCategory = new ResignationCategory();
			resignationCategory.setRescatName(resignationCategories[i]);
			resignationCategory.setRescatValue(resignationCategories[i]);
			resignationCategoryRepository.save(resignationCategory);
		}
	}

=======
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
	private void createSettings() {

		String[][] settings = {
				{ "ADM", "TAG Team Email (to)", "Gada.Naveena" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "TAG Team Email (cc)", "Sindhuja.S.S" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "HR Team Email (to)", "Madhumita.Ghosh" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "HR Team Email (cc)", "Garima.Ojha" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "LOBLead", "190741", "text", "CIB - BT" }, { "ADM", "LOBLead", "190741", "text", "CIB - CMS" },
				{ "ADM", "LOBLead", "190741", "text", "CIB - Digital" },
				{ "ADM", "LOBLead", "190741", "text", "CIB - MI" },
				{ "ADM", "LOBLead", "190741", "text", "CIB - Tavisca" }, { "ADM", "LOBLead", "121870", "text", "AM" },
				{ "ADM", "LOBLead", "106716", "text", "CCB" }, { "ADM", "LOBLead", "122539", "text", "CB" },
				{ "ADM", "LOBLead", "106716", "text", "GTI" }, { "ADM", "LOBLead", "106716", "text", "CT" },
				{ "ADM", "LOBLead", "106716", "text", "CyberSecurity" }, { "ADM", "EDL", "254395", "text", "JPMC" },
				{ "ADM", "PDL", "231612", "text", "JPMC" },
				{ AppConstants.APP_SETTNGS_VISIBILITY_GENERAL, AppConstants.APP_SETTINGS_EMAILWORKING, "false",
						"boolean", "" },
				{ AppConstants.APP_SETTNGS_VISIBILITY_GENERAL, AppConstants.APP_SETTINGS_DEFAULTEMAIL,
						"narenkgcts@outlook.com", "text", "" },
				{ "ADM", "Projects Ending (to)", "JPMCLOBLeads" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "Projects Ending (cc)",
						"Narendra.Gupta" + EmailConstants.DEFAULT_DOMAIN + ";Abir.Chatterjee"
								+ EmailConstants.DEFAULT_DOMAIN,
						"text", "" },
				{ "ADM", "Assignment Ending (to)", "JPMCLOBLeads" + EmailConstants.DEFAULT_DOMAIN, "text", "" },
				{ "ADM", "Assignment Ending (cc)", "Narendra.Gupta" + EmailConstants.DEFAULT_DOMAIN + ";Abir.Chatterjee"
						+ EmailConstants.DEFAULT_DOMAIN, "text", "" }, };

		for (int i = 0; i < settings.length; i++) {
			Settings setting = new Settings();
			setting.setVisibility(settings[i][0]);
			setting.setParam(settings[i][1]);
			setting.setValue(settings[i][2]);
			setting.setType(settings[i][3]);
			setting.setVisibilityTwo(settings[i][4]);
			settingsRepository.save(setting);

		}
		settingsRepository.flush();
	}

	private void createDefaultUser() {
		User user = new User();
		user.setAssociateId(999L);
		user.setIsActive(true);
		user.setIsApproved(true);
		user.setIsVerified(true);
		user.setPassword(passwordEncoder.encode("India@123"));
		user.setRoles(new HashSet(roleRepository.findAll()));

		userRepository.save(user);
		userRepository.flush();
	}

	private void createADMHRUser() {
		User user = new User();
		user.setAssociateId(359901L);
		user.setIsActive(true);
		user.setIsApproved(true);
		user.setIsVerified(true);
		user.setPassword(passwordEncoder.encode("India@123"));
		user.setRoles(Collections.singleton(roleRepository.findByName(ERole.HR)));
		userRepository.save(user);
		userRepository.flush();
	}

	private void createADMTAGUser() {
		User user = new User();
		user.setAssociateId(2107512L);
		user.setIsActive(true);
		user.setIsApproved(true);
		user.setIsVerified(true);
		user.setPassword(passwordEncoder.encode("India@123"));
		user.setRoles(Collections.singleton(roleRepository.findByName(ERole.TAG)));
		userRepository.save(user);
		userRepository.flush();
	}

	private void createHolidays() {
		List<Holiday> holidayList = new ArrayList<>(
				Arrays.asList(new Holiday(null, 2022, ECalenderMonth.JANUARY, 1, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.JANUARY, 14, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.JANUARY, 26, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.MARCH, 1, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.APRIL, 15, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.MAY, 3, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.AUGUST, 15, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.AUGUST, 31, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.OCTOBER, 5, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.OCTOBER, 24, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.NOVEMBER, 1, "Bangalore"),
						new Holiday(null, 2022, ECalenderMonth.DECEMBER, 25, "Bangalore")));
		holidayRepository.saveAll(holidayList);
	}

	private void createSkillTables() {
		String[] skillFamiliesArr = { "Java", "Java Springboot", "Java Springboot Microservices",
				"Java Springboot Microservices AWS", "Java Springboot AWS", "Java Springboot React" };

		String[] skillDetailsArr = { "Java", "Java Springboot", "Java Springboot Microservices",
				"Java Springboot Microservices AWS", "Java Springboot AWS", "Java Springboot React" };

		for (int i = 0; i < skillFamiliesArr.length; i++) {
			Skill skill = new Skill();
			skill.setSkillName(skillFamiliesArr[i]);
			skill.setSkillDetails(skillDetailsArr[i]);
			skillRepository.save(skill);
		}

		String[] evaluationResultArr = { "Rejected", "Selected", "Recommended for Next Round" };
		String[] evaluationResultCategoryArr = { "Rejected Remote Only", "Screen Reject", "Internal Reject",
				"Client Reject", "Less Experience", "Not Reachable", "Not Interested/Available" };

		for (int i = 0; i < evaluationResultArr.length; i++) {
			EvaluationResult result = new EvaluationResult();
			result.setResult(evaluationResultArr[i]);
			evaluationResultRepository.save(result);
		}

		for (int i = 0; i < evaluationResultCategoryArr.length; i++) {
			EvaluationResultCategory resultCategory = new EvaluationResultCategory();
			resultCategory.setResultCategory(evaluationResultCategoryArr[i]);
			evaluationResultCategoryRepository.save(resultCategory);
		}
	}

	public void createEmployeeUsers() {
		List<Long> associateIdList = new ArrayList<>(
				Arrays.asList(231612L, 2068147L, 159266L, 230257L, 839468L, 2130352L, 2121141L));
		List<User> userList = new ArrayList<>();
		for (Long i : associateIdList) {
			User user = new User();
			user.setAssociateId(i);
			user.setIsActive(true);
			user.setIsApproved(true);
			user.setIsVerified(true);
			user.setPassword(passwordEncoder.encode("India@123"));
			user.setRoles(Collections.singleton(roleRepository.findByName(ERole.Associate)));
			userList.add(user);
		}
		userRepository.saveAll(userList);
	}

//    public void createDefaultEpics() {
//        List<Epic> epicList = new ArrayList<>();
//        List<ParentAccount> parentAccounts = parentAccountRepository.findAll();
//        epicList.add(new Epic(null, "Epic1", 10, 45D, parentAccounts.get(0)));
//        epicList.add(new Epic(null, "Epic2", 10, 45D, parentAccounts.get(1)));
//        epicList.add(new Epic(null, "Epic3", 10, 45D, parentAccounts.get(2)));
//        epicList.add(new Epic(null, "Epic4", 10, 45D, parentAccounts.get(3)));
//        epicRepository.saveAll(epicList);
//    }
//
//	public void createDefaultSprints() {
//		List<Sprint> sprintList = new ArrayList<>();
//		List<Epic> epicList = epicRepository.findAll();
//		sprintList.add(new Sprint(null, "Sprint1", Instant.now(), Instant.now(), 254395L, "ScrumMaster1"));
//		sprintList.add(new Sprint(null, "Sprint2", Instant.now(), Instant.now(), 254395L, "ScrumMaster2"));
//		sprintList.add(new Sprint(null, "Sprint3", Instant.now(), Instant.now(), 254395L, "ScrumMaster3"));
//		sprintList.add(new Sprint(null, "Sprint4", Instant.now(), Instant.now(), 254395L, "ScrumMaster4"));
//		sprintRepository.saveAll(sprintList);
//	}

	public Category createDefaultStoryPriority() {
		List<String> storyPriority = new ArrayList<>(Arrays.asList("High", "Medium", "Low"));
		List<Category> categoryList = new ArrayList<>();

		Category defaultCategory = null;

		int i = 0;
		for (String status : storyPriority) {
			Category temp = new Category();

			if (i == 0)
				defaultCategory = temp;
			i++;
			temp.setGroupKey(AppConstants.STORY_PRIORITY);
			temp.setCatGroup(AppConstants.STORY_PRIORITY);
			temp.setGroupValue(status);
			categoryList.add(temp);
		}
		categoryRepository.saveAll(categoryList);

		return defaultCategory;
	}

	public List<Epic> createDefaultEpics() {
		List<Epic> epicList = new ArrayList<Epic>();

		for (int i = 0; i < 100; i++) {
			Epic epic = new Epic();
			epic.setDescription("Epic Description " + i);
			epic.setEta(new Date());
			epic.setExpectedStoryPoint(Long.valueOf(i) + 1);
			epic.setName("Epic Name " + i);
			epicList.add(epic);
		}

		epicRepository.saveAll(epicList);
		return epicList;
	}

	public void createDefaultStories(List<Epic> epicList, Category defaultStatus, Category defaultPriority) {
		List<Story> storyList = new ArrayList<Story>();
		int epicId = 0;
		for (Epic epic : epicList) {
			epicId++;
			if (epicId < 5)
				continue;
			for (int i = 0; i < 10; i++) {
				Story story = new Story();
				story.setComments(null);
				story.setDetails("Epic:" + epicId + " - Details " + i);
				story.setEpic(epic);
				story.setStoryPointEstimation(Long.valueOf(i) + 1);
				story.setSubject("Epic:" + epicId + " - Subject " + i);
				story.setStoryStatus(defaultStatus);
				story.setStoryPriority(defaultPriority);
				storyList.add(story);
			}
		}

		storyRepository.saveAll(storyList);
	}

	public void setInitialValuesInDB() {
		System.out.println("Creating initial database");
		User user = userRepository.findByAssociateId(254395L).orElse(null);
		System.out.println("---------------------------------------------------:" + user);
		if (user == null) {
			createDefaultRoles();
			createDefaultUser();
			createADMHRUser();
			createADMTAGUser();
			createSkillTables();
			createDefaultBillableCategories();
			createDefaultReferredCategories();
			createSettings();
			createLeaveCategories();
			createLeaveStatus();
			createHolidays();
<<<<<<< HEAD
			createResignationCategoriesTable();
=======
>>>>>>> ecc75d82f1d8a03bafb05c1dbadf2296d87e657d
			Category defaultStatus = createDefaultStoryStatusCategories();
			Category defaultPriority = createDefaultStoryPriority();
			List<Epic> epicList = createDefaultEpics();
			createDefaultStories(epicList, defaultStatus, defaultPriority);
		}
	}

	@Transactional
	public void assignStories() {
		System.out.println("Creating initial database");
		AssignmentReport report = assignmentReportRepository.findFirstByOrderByIdDesc()
				.orElseThrow(() -> new BadRequestException("No assignment report found"));
		List<AssignmentUser> usersList = assignmentUserRepo
				.findByAssignmentReportAndServiceLineAndGradeDescription(report, "ADM", "PAT");
//		System.out.println(usersList);
		List<Story> storyList = storyRepository.findAll();

		Random random = new Random();

		for (Story story : storyList) {

			int answer = random.nextInt(usersList.size() - 1);
			System.out.println(usersList.get(answer));
			story.setOwner(usersList.get(answer));
		}
		storyRepository.saveAll(storyList);
	}

}