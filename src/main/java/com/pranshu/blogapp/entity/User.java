package com.pranshu.blogapp.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    @Column(unique = true)
    private String username;
    private String password;

    @Builder.Default
    private List<String> roles = new ArrayList<>();
    // public int getId() {
    //     return id;
    // }

    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();
    
}


/*
 * The extra characters you’re seeing, \", are escape characters used to
 * denote a ". In this case, they’re being used to specify the name of
 * the table as a String in the @Table annotation
 * 
 *  " "user" " will cause issue of which opening (") is for which closing (")
 *  so we do  "  /"user/"   "
 * 
 *  we do so because user may be a reserved keyword in some databases so by this,
 *  we specify we mean the string "user" 
 */