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
                ),

                @UniqueConstraint(
                        name = "uk_user_programme",
                        columnNames = {
                                "user_id",
                                "training_programme_id"
                        }
                )
        }
)
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * TASK 1 - Admin Officer Nomination
     */
    @Column(name = "officer_id")
    private String officerId;

    private String officerName;

    private String department;


    /*
     * USER BOOKING
     */
    @Column(name = "user_id")
    private String userId;


    /*
     * PROGRAMME
     */
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

    @Column(nullable = false)
    private String targetDepartments;


    /*
     * TASK 2
     */
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime receivedAt;


    public Nomination() {
    }


    @PrePersist
    public void beforeSave() {

        if (receivedAt == null) {
            receivedAt =
                    LocalDateTime.now();
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

    public void setOfficerId(
            String officerId
    ) {
        this.officerId = officerId;
    }


    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(
            String officerName
    ) {
        this.officerName = officerName;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department
    ) {
        this.department = department;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(
            String userId
    ) {
        this.userId = userId;
    }


    public String getTrainingProgrammeId() {
        return trainingProgrammeId;
    }

    public void setTrainingProgrammeId(
            String trainingProgrammeId
    ) {
        this.trainingProgrammeId =
                trainingProgrammeId;
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

    public void setTrainingDate(
            LocalDate trainingDate
    ) {
        this.trainingDate = trainingDate;
    }


    public String getVenue() {
        return venue;
    }

    public void setVenue(
            String venue
    ) {
        this.venue = venue;
    }


    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(
            String trainer
    ) {
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


    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }


    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(
            LocalDateTime receivedAt
    ) {
        this.receivedAt = receivedAt;
    }
}