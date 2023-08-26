package com.banquito.core.branches.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "branches")
public class Branch {
    
    @Id
    private String id;
    @Indexed(name = "idxu_branches_code", unique = true)
    private String code;
    private String name;
}

