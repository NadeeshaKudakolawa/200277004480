package com.ministry.training.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "training_programmes")
public class TrainingProgramme {

    @Id
    private String programmeId;

    /*
     * FINANCIAL
     * TECHNICAL
     * MANAGEMENT
     * GENERAL
     */
    private String programmeType;

    private String title;

    private LocalDate trainingDate;

    private String venue;

    private String trainer;

    private Integer maximumParticipants;

    /*
     * TASK 3 configurable rules
     */
    private String eligibleDepartments;

    private String requiredGrade;

    private String requiredDesignation;

    private Integer minimumYearsOfService;

    private Integer repeatRestrictionMonths;


    public TrainingProgramme() {
    }


    public String getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(String programmeId) {
        this.programmeId = programmeId;
    }


    public String getProgrammeType() {
        return programmeType;
    }

    public void setProgrammeType(String programmeType) {
        this.programmeType = programmeType;
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

    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }


    public String getEligibleDepartments() {
        return eligibleDepartments;
    }

    public void setEligibleDepartments(String eligibleDepartments) {
        this.eligibleDepartments = eligibleDepartments;
    }


    public String getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(String requiredGrade) {
        this.requiredGrade = requiredGrade;
    }


    public String getRequiredDesignation() {
        return requiredDesignation;
    }

    public void setRequiredDesignation(String requiredDesignation) {
        this.requiredDesignation = requiredDesignation;
    }


    public Integer getMinimumYearsOfService() {
        return minimumYearsOfService;
    }

    public void setMinimumYearsOfService(Integer minimumYearsOfService) {
        this.minimumYearsOfService = minimumYearsOfService;
    }


    public Integer getRepeatRestrictionMonths() {
        return repeatRestrictionMonths;
    }

    public void setRepeatRestrictionMonths(Integer repeatRestrictionMonths) {
        this.repeatRestrictionMonths = repeatRestrictionMonths;
    }
}