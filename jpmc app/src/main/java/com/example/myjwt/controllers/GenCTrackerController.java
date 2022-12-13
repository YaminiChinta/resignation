package com.example.myjwt.controllers;

import com.example.myjwt.beans.GenCGroupDetail;
import com.example.myjwt.beans.GenCTrackerDashboard;
import com.example.myjwt.beans.GenCTrackerSprints;
import com.example.myjwt.beans.GenCTrackerStories;
import com.example.myjwt.exception.BadRequestException;
import com.example.myjwt.models.AssignmentReport;
import com.example.myjwt.models.AssignmentUser;
import com.example.myjwt.models.Role;
import com.example.myjwt.models.User;
import com.example.myjwt.models.enm.EGrade;
import com.example.myjwt.models.enm.ERole;
import com.example.myjwt.repo.*;
import com.example.myjwt.repo.AssignmentReportRepository;
import com.example.myjwt.repo.AssignmentUserRepository;
import com.example.myjwt.repo.UserRepository;
import com.example.myjwt.security.services.EpicService;
import com.example.myjwt.security.services.SprintService;
import com.example.myjwt.security.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GenCTrackerController extends BaseController {

	@Autowired
	private AssignmentReportRepository assignmentReportRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private AssignmentUserRepository assignmentUserRepository;

	@Autowired
	private StoryService storyService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private ParentAccountRepository parentAccountRepository;

	@Autowired
	private PDLRepository pdlRepository;

	@Autowired
	private EpicService epicService;

	@GetMapping("/gencTracker/dashboard")
	public GenCTrackerDashboard getGencTrackerDashboard() {

		Long currentUserId = getCurrentUserId();

		User user = userRepository.findById(currentUserId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		System.out.println("currentUserId == " + currentUserId);

		AssignmentReport report = assignmentReportRepository.findFirstByOrderByIdDesc()
				.orElseThrow(() -> new BadRequestException("No assignment report found"));

		AssignmentUser loggedInAssociate = assignmentUserRepository
				.findByAssignmentReportAndAssociateID(report, user.getAssociateId()).get(0);

		List<AssignmentUser> allAssociates = assignmentUserRepository.findByAssignmentReportAndServiceLine(report,
				loggedInAssociate.getServiceLine());

		Double totalGenCs = 0.0;
		Double billableGenCs = 0.0;

		System.out.println(user.getRoles());

		int iRole = -1;


		if(iRole == -1) {
			for (Role role : user.getRoles()) {
				if (role.getName().name().equalsIgnoreCase(ERole.PDL.name())) {
					iRole = 0;
					break;
				}
			}
		}

		if(iRole == -1) {
			for (Role role : user.getRoles()) {
				if (role.getName().name().equalsIgnoreCase(ERole.EDL.name())) {
					iRole = 1;
					break;
				}
			}
		}

		if(iRole == -1) {
			for (Role role : user.getRoles()) {
				if (role.getName().name().equalsIgnoreCase(ERole.LOBLead.name())) {
					iRole = 2;
					break;
				}
			}
		}

		if(iRole == -1) {
			for (Role role : user.getRoles()) {
				if (role.getName().name().equalsIgnoreCase(ERole.AccountLead.name())) {
					iRole = 2;
					break;
				}
			}
		}

		if(iRole == -1) {
			for (Role role : user.getRoles()) {
				if (role.getName().name().equalsIgnoreCase(ERole.ProjectManager.name())) {
					iRole = 3;
					break;
				}
			}
		}

		switch (iRole) {
			case 0:
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus())
							billableGenCs = billableGenCs + aUser.getfTE();

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				break;
			case 1:
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus())
							billableGenCs = billableGenCs + aUser.getfTE();

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				break;
			case 2:
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus())
							billableGenCs = billableGenCs + aUser.getfTE();

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				break;
			case 3:
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus())
							billableGenCs = billableGenCs + aUser.getfTE();

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				break;
			case 4:
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
							|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus())
							billableGenCs = billableGenCs + aUser.getfTE();

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				break;
		}

		GenCTrackerDashboard dashboard = new GenCTrackerDashboard();

		GenCTrackerStories storyCompletion = null;
		List<Long> associates = new ArrayList<Long>();

		switch (iRole) {
			//PDL
			case 0:
//			Optional<PDL> pdl = pdlRepository.findById(new CustomUserDetailsService().loadUserFromContext().getAssociateId());
//			List<ParentAccount> parentAccounts = parentAccountRepository.findByPdl(pdl.get());
				for (AssignmentUser aUser : allAssociates) {
					if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
						
						associates.add(aUser.getAssociateID());
						totalGenCs = totalGenCs + aUser.getfTE();

						if (aUser.getBillabilityStatus()) {
							billableGenCs = billableGenCs + aUser.getfTE();
						}

						System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
					}
				}
				storyCompletion = storyService.getStoryCompletion(associates);
				try {
					GenCTrackerSprints sprintsCompletion = sprintService.getSprintsCompletion();
					dashboard.setTotalSprints(sprintsCompletion.getTotalSprints());
					dashboard.setCompletedSprints(sprintsCompletion.getCompletedSprints());
				} catch (Exception e) {

				}
				dashboard.setBillableGenCs(billableGenCs);
				dashboard.setTotalGenCs(totalGenCs);
				dashboard.setTotalStories(storyCompletion.getTotalStories());
				dashboard.setAcceptedStories(storyCompletion.getAcceptedStories());
				GenCGroupDetail genCGroupDetail = new GenCGroupDetail();
				genCGroupDetail.setParentAccount("JPMC");
				genCGroupDetail.setBillableGenCs(billableGenCs);
				genCGroupDetail.setTotalGenCs(totalGenCs);
				genCGroupDetail.setCompletedSprints(dashboard.getCompletedSprints());
				genCGroupDetail.setTotalSprints(dashboard.getTotalSprints());
				genCGroupDetail.setTotalStories(dashboard.getTotalStories());
				genCGroupDetail.setCompletedStories(storyCompletion.getAcceptedStories());
				HashMap<String, Double> epicsStatus = epicService.getEpicsStatus();
				genCGroupDetail.setTotalEpics(epicsStatus.get("totalEpics"));
				genCGroupDetail.setCompletedEpics(epicsStatus.get("completedEpics"));
				System.out.println("PDL --- ");
				dashboard.setGenCGroupDetails(new ArrayList<>(Arrays.asList(genCGroupDetail)));
			break;
			case 1:
			for (AssignmentUser aUser : allAssociates) {
				if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
					totalGenCs = totalGenCs + aUser.getfTE();

					if (aUser.getBillabilityStatus()) {
						billableGenCs = billableGenCs + aUser.getfTE();
					}

					System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
				}
			}
			break;
			case 2:
			for (AssignmentUser aUser : allAssociates) {
				if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
					totalGenCs = totalGenCs + aUser.getfTE();

					if (aUser.getBillabilityStatus()) {
						billableGenCs = billableGenCs + aUser.getfTE();
					}

					System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
				}
			}
			break;
			case 3:
			for (AssignmentUser aUser : allAssociates) {
				if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
					totalGenCs = totalGenCs + aUser.getfTE();

					if (aUser.getBillabilityStatus()) {
						billableGenCs = billableGenCs + aUser.getfTE();
					}

					System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
				}
			}
			break;
			case 4:
			for (AssignmentUser aUser : allAssociates) {
				if (aUser.getGradeDescription().equalsIgnoreCase(EGrade.PAT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.P.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PT.name())
						|| aUser.getGradeDescription().equalsIgnoreCase(EGrade.PA.name())) {
					totalGenCs = totalGenCs + aUser.getfTE();

					if (aUser.getBillabilityStatus()) {
						billableGenCs = billableGenCs + aUser.getfTE();
					}

					System.out.println("-----------------2----------------------" + totalGenCs + ":" + billableGenCs);
				}
			}
			break;
		}

		dashboard.setBillableGenCs(billableGenCs);
		dashboard.setTotalGenCs(totalGenCs);
		System.out.println(storyCompletion);
		dashboard.setTotalStories(storyCompletion.getTotalStories());
		dashboard.setAcceptedStories(storyCompletion.getAcceptedStories());
		dashboard.setTotalEpics(epicService.getAllEpicsCount());
		dashboard.setCompletedEpics(epicService.getAllCompletedEpicsCount());
		dashboard.setCurrentSprintGenCs(sprintService.getActiveSprintsOwnerCount());

		return dashboard;
	}
}
