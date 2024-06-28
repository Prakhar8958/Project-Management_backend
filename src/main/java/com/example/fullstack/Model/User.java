package com.example.fullstack.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String password;

    @JsonIgnore
    @OneToMany(mappedBy ="assignee",cascade = CascadeType.ALL)
    private List<Issue> assignIssues=new ArrayList<>();

    private int projectSize;


}
