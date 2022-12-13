package com.example.myjwt.repo;

import com.example.myjwt.models.AssignmentReport;
import com.example.myjwt.models.AssignmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AssignmentUserRepository extends JpaRepository<AssignmentUser, Long> {
	@Query(value = "SELECT associateID, associateName, sID, sIDStatus, screeningStatus, latestStart, screeningStatus, "
			+ "vLAN, assignmentID, sOID, sOLine, projectID, projectDescription, projectBillability, projectType, projectstatus, project_Solution_Type, "
			+ "projectManagerID, projectManagerName, accountID, accountName, accountManagerID, "
			+ "accountManagerName, lOB, region, cP, cRM, eDP, ePL_ID, ePL_Name, verticalHorizontal, jobCode, designation, "
			+ "grade, gradeDescription, pAQGrade, pA_Category, fINID, departmentName, serviceLine, hCMSetID, "
			+ "hCMSupervisorID, projectOwningDepartment, projectOwningPractice, billabilityStatus, "
			+ "billabilityReason, secondaryStageTag, criticalFlag, locationID, onOff, country, state, "
			+ "city, assignmentStart, assignmentEnd, assignmentStatus, futureAssignment, percentAllocation, "
			+ "assignment_City, assignment_location, assgn_state, assgn_country, fTE, associatelevelFTE, "
			+ "correctedAssociatelevelFTE, projectRole, operationalRole, locationReasoncode, projectStart, "
			+ "projectEnd, subVertical, sBU1, sBU2, contractorEnd, eSAComments, bURMA_Id, bURMA_Name FROM assignmentuser", nativeQuery = true)
	List<Object[]> findAllAssignmentUsers();
	
	List<AssignmentUser> findAll();
	
	List<AssignmentUser> findByAssignmentReport(AssignmentReport assignmentReport);
	
	List<AssignmentUser> findByAssignmentReportAndServiceLineAndLOB(AssignmentReport assignmentReport, String selServiceLine, String selLOB);
	
	List<AssignmentUser> findByAssignmentReportAndServiceLineAndGradeDescriptionIn(AssignmentReport assignmentReport, String selServiceLine, ArrayList<String> gradeList);
	
	List<AssignmentUser> findByAssignmentReportAndServiceLine(AssignmentReport assignmentReport, String selServiceLine);
	
	List<AssignmentUser> findByAssignmentReportAndServiceLineAndGradeDescription(AssignmentReport assignmentReport, String selServiceLine, String grade);

	List<AssignmentUser> findByAssignmentReportAndServiceLineAndOnOff(AssignmentReport assignmentReport, String selPractice, String onOff);

	@Query(value="select distinct service_line from assignmentuser",nativeQuery=true)
	List<String> getAllServiceLine();



	List<AssignmentUser> findByAssignmentReportAndAssociateID(AssignmentReport assignmentReport, Long id);
		
	public Boolean existsByAssociateID(Long associateId);

	List<AssignmentUser> findByAssociateIDIn(List<Long> associateIds);

	List<AssignmentUser> findByAssociateID(Long associateId);

//	@Query("SELECT distinct associateId, lob, account_name FROM assignmentuser where lob ='CT' and account_name = 'JPMC Corporate Technology' and grade_description like 'PA%'")
//	List<AssignmentUser> findByLOBAndAccountNameAndGradeDescriptionIgnoreCaseContaining(String lob, String account, String grade);
//
//
}