package com.ifi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "abc")
@Table(name = "employee_data", schema = "ifi_db")
@Data
public class EmployeeEntity implements Serializable {
    private static final long serialVersionUID = 25141992L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    @Basic
    @Column(name = "joined_date")
    private Timestamp joinedDate;
}
