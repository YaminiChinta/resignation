package com.example.myjwt.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoryRequest {
    private Long id;

    private String subject;

    private String details;

    private Long ownerId;

    private Long epicId;

    private Long sprintId;

    private Integer storyPointEstimation;

    private String currentStatus;

    private String acceptanceStatus;


    private String priority;


}