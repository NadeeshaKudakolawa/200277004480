package com.ministry.training.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
        name = "nominations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_officer_programme",
                        columnNames = {"officer_id", "training_programme_id"}
                )
        }
)
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "officer_id", nullable = false)
    private String officerId;

    @NotBlank
    @Column(nullable = false)
    private String officerName;

    @NotBlank
    @Column(nullable = false)
    private String department;

    @NotBlank
    @Column(name = "training_programme_id", nullable = false)
    private String trainingProgrammeId;

    @NotBlank
    @Column(nullable = false)
    private String trainingProgrammeTitle;

    public Nomination() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTrainingProgrammeId() {
        return trainingProgrammeId;
    }

    public void setTrainingProgrammeId(String trainingProgrammeId) {
        this.trainingProgrammeId = trainingProgrammeId;
    }

    public String getTrainingProgrammeTitle() {
        return trainingProgrammeTitle;
    }

    public void setTrainingProgrammeTitle(String trainingProgrammeTitle) {
        this.trainingProgrammeTitle = trainingProgrammeTitle;
    }
}