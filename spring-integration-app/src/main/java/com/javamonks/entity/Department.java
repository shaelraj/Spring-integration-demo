package com.javamonks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// Class
public class Department {

    @Id
    // Use GenerationType.SEQUENCE
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_gen")
    @SequenceGenerator(
            name = "department_seq_gen",      // Internal generator name (used in @GeneratedValue)
            sequenceName = "department_seq", // The actual database sequence name
            allocationSize = 1               // How many IDs Hibernate fetches at once (1 is standard for this use case)
    )

    private Long departmentId;
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;
    private String status;
    private String code;
    private int retry;
}
