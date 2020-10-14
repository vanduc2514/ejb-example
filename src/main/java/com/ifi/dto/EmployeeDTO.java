package com.ifi.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 25141992L;

    private Integer id;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate joinedDate;
}
