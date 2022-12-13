package com.example.myjwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;

    private Long edlId;

    private String eldName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdl_id")
    private PDL pdl;
}