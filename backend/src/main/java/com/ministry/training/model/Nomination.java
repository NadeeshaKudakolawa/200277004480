package com.ministry.training.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "nominations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_officer_programme",
                        columnNames = {
                                "officer_id",
                                "training_programme_id"
                        }
                )
        }
)
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Officer ID is required.")
    @Column(
            name = "officer_id",
            nullable = false
    )
    private String officerId;

    @NotBlank(message = "Officer name is required.")
    @Column(nullable = false)
    private String officerName;

    @NotBlank(message = "Department is required.")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Training programme is required.")
    @Column(
            name = "training_programme_id",
            nullable = false
    )
    private String trainingProgrammeId;

    @NotBlank(message = "Training programme title is required.")
    @Column(nullable = false)
    private String trainingProgrammeTitle;

    @NotNull(message = "Training date is required.")
    @FutureOrPresent(
            message = "Training date cannot be in the past."
    )
    @Column(nullable = false)
    private LocalDate trainingDate;

    @NotBlank(message = "Venue is required.")
    @Column(nullable = false)
    private String venue;

    @NotBlank(message = "Trainer / Resource Person is required.")
    @Column(nullable = false)
    private String trainer;

    @NotNull(message = "Maximum participants is required.")
    @Min(
            value = 1,
            message = "Maximum participants must be at least 1."
    )
    @Column(nullable = false)
    private Integer maximumParticipants;

    @NotBlank(message = "Target department is required.")
    @Column(nullable = false)
    private String targetDepartments;

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

    public void setTrainingProgrammeTitle(
            String trainingProgrammeTitle
    ) {
        this.trainingProgrammeTitle =
                trainingProgrammeTitle;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    public void setMaximumParticipants(
            Integer maximumParticipants
    ) {
        this.maximumParticipants =
                maximumParticipants;
    }

    public String getTargetDepartments() {
        return targetDepartments;
    }

    public void setTargetDepartments(
            String targetDepartments
    ) {
        this.targetDepartments =
                targetDepartments;
    }
}