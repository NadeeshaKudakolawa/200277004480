package com.ministry.training.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {

    @Id
    @NotBlank(message = "Programme ID is required.")
    @Column(name = "programme_id")
    private String programmeId;

    @NotBlank(message = "Programme title is required.")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Training date is required.")
    @FutureOrPresent(message = "Training date cannot be in the past.")
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

    @NotBlank(message = "Target departments are required.")
    @Column(nullable = false)
    private String targetDepartments;

    public TrainingProgramme() {
    }

    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.maximumParticipants = maximumParticipants;
    }

    public String getTargetDepartments() {
        return targetDepartments;
    }

    public void setTargetDepartments(
            String targetDepartments
    ) {
        this.targetDepartments = targetDepartments;
    }
}