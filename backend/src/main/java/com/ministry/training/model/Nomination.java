package com.ministry.training.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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


    @Column(name = "officer_id", nullable = false)
    private String officerId;

    @Column(nullable = false)
    private String officerName;

    @Column(nullable = false)
    private String department;

    private String grade;

    private String designation;

    private Integer yearsOfService;


    @Column(
            name = "training_programme_id",
            nullable = false
    )
    private String trainingProgrammeId;

    @Column(nullable = false)
    private String trainingProgrammeTitle;

    @Column(nullable = false)
    private LocalDate trainingDate;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private String trainer;

    @Column(nullable = false)
    private Integer maximumParticipants;


    /*
     * CONFIRMED
     * WAITING_LIST
     * CANCELLED
     * NOT_ELIGIBLE
     */
    @Column(nullable = false)
    private String status;


    private String statusReason;


    @Column(nullable = false)
    private LocalDateTime receivedAt;


    public Nomination() {
    }


    @PrePersist
    public void beforeSave() {

        if (receivedAt == null) {
            receivedAt = LocalDateTime.now();
        }
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


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }


    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}