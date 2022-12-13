package com.example.myjwt.beans;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenCGroupDetail {
    private String parentAccount;
    private Double totalGenCs;
    private Double billableGenCs;
    private Double totalEpics;
    private Double completedEpics;
    private Double totalSprints;
    private Double completedSprints;
    private Double totalStories;
    private Double completedStories;
    private Double totalGenCsHaveStories;


}