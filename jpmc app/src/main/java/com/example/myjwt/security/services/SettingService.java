package com.example.myjwt.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myjwt.models.Settings;
import com.example.myjwt.repo.SettingsRepository;
import com.example.myjwt.util.EmailConstants;

@Service
public class SettingService {

	@Autowired
	private SettingsRepository settingsRepository;

	private String getSettingsValue(String serviceLine, String param) {
		Settings setting = settingsRepository.findByVisibilityAndParam(serviceLine, param);
		return setting.getValue();
	}

	private String getTAGEmailTo(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.TAG_EMAIL_TO);
	}

	private String getTAGEmailCC(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.TAG_EMAIL_CC);
	}

	private String getHREmailTo(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.HR_EMAIL_TO);
	}

	private String getHREmailCC(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.HR_EMAIL_CC);
	}

	private String getLOBLeadAssociateId(String serviceLine, String LOB) {
		return getSettingsValue(serviceLine, LOB + " Lead");
	}

	private String getEDLAssociateId(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.EDL_EMAIL);
	}

	private String getPDLAssociateId(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.PDL_EMAIL);
	}

	private boolean isCTSEMailWorking() {
		String value = getSettingsValue(null, "Cognizant Email Working?");
		boolean flag = Boolean.valueOf(value);
		return flag;
	}

	private String getDefaultEmailId() {
		return getSettingsValue(null, EmailConstants.DEFAULT_EMAIL);
	}

	private String getProjectsEndingTo(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.PROJECTS_ENDING_TO);
	}

	private String getProjectsEndingCC(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.PROJECTS_ENDING_CC);
	}

	private String getAssignmentsEndingTo(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.ASSIGNMENTS_ENDING_TO);
	}

	private String getAssignmentsEndingCC(String serviceLine) {
		return getSettingsValue(serviceLine, EmailConstants.ASSIGNMENTS_ENDING_CC);
	}

	public String getEmailId(String emailOf, String serviceLine, String LOB) {

		if (!isCTSEMailWorking()) {
			System.out.println(
					"-----------------------------------Returning default email id-----------------------------------");
			return getDefaultEmailId();
		}
		System.out
				.println("-----------------------------------Getting true email id-----------------------------------");
		switch (emailOf) {
		case EmailConstants.EDL_EMAIL:
			return getEDLAssociateId(serviceLine) + EmailConstants.DEFAULT_DOMAIN;
		case EmailConstants.PDL_EMAIL:
			return getPDLAssociateId(serviceLine) + EmailConstants.DEFAULT_DOMAIN;
		case EmailConstants.TAG_EMAIL_TO:
			return getTAGEmailTo(serviceLine);
		case EmailConstants.TAG_EMAIL_CC:
			return getTAGEmailCC(serviceLine);
		case EmailConstants.HR_EMAIL_TO:
			return getHREmailTo(serviceLine);
		case EmailConstants.HR_EMAIL_CC:
			return getHREmailCC(serviceLine);
		case EmailConstants.LOB_LEAD_EMAIL:
			return getLOBLeadAssociateId(serviceLine, LOB) + EmailConstants.DEFAULT_DOMAIN;
		case EmailConstants.PROJECTS_ENDING_TO:
			return getProjectsEndingTo(serviceLine);
		case EmailConstants.PROJECTS_ENDING_CC:
			return getProjectsEndingCC(serviceLine);
		case EmailConstants.ASSIGNMENTS_ENDING_TO:
			return getAssignmentsEndingTo(serviceLine);
		case EmailConstants.ASSIGNMENTS_ENDING_CC:
			return getAssignmentsEndingCC(serviceLine);
		case EmailConstants.DEFAULT_FROM:
			return EmailConstants.DEFAULT_FROM;
		default:
			return emailOf + EmailConstants.DEFAULT_DOMAIN;
		}
	}
}
