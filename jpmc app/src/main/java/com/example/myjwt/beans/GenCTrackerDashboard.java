package com.example.myjwt.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenCTrackerDashboard {
	private Double totalGenCs;
	private Double billableGenCs;
	private Double totalSprints;
	private Double completedSprints;
	private Double acceptedStories;
	private Double totalStories;
	private long totalEpics;
	private long completedEpics;
	private long currentSprintGenCs;
	
	private List<GenCGroupDetail> genCGroupDetails;
	
}
