package com.example.myjwt.controllers;

import com.example.myjwt.security.services.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
	
	

	public Long getCurrentUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = Long.valueOf(0);
		if (principal instanceof UserPrincipal) {
			userId = ((UserPrincipal)principal).getId();
		} 
		return userId;
	}

}