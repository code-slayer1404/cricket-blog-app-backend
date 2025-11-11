package com.pranshu.blogapp.payload;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private int id;

    private String name;
    private String username;
    @JsonIgnore
    private String password; // pitfall is that no userDTO in controller argument will have this as its not deserialized i.e. @RequestBody will not work for this field. 
    @Builder.Default
    private List<String> roles= new ArrayList<>();


}
