package com.example.myjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String details;

    private Long ownerId;

    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Epic epic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    private Integer storyPointEstimation;

    private String currentStatus;

    @Column(length = Integer.MAX_VALUE)
    private String acceptanceStatus;


    private ArrayList<String> comments;

    private String priority;


}