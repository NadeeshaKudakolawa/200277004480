package com.ministry.training.repository;

import com.ministry.training.model.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {


    /*
     * TASK 1 DUPLICATE CHECK
     */
    boolean existsByOfficerIdAndTrainingProgrammeId(
            String officerId,
            String trainingProgrammeId
    );


    /*
     * USER BOOKING DUPLICATE CHECK
     */
    boolean existsByUserIdAndTrainingProgrammeId(
            String userId,
            String trainingProgrammeId
    );


    /*
     * TASK 2 CAPACITY
     */
    long countByTrainingProgrammeIdAndStatus(
            String trainingProgrammeId,
            String status
    );


    /*
     * FIFO WAITING LIST
     */
    Optional<Nomination>
    findFirstByTrainingProgrammeIdAndStatusOrderByReceivedAtAscIdAsc(
            String trainingProgrammeId,
            String status
    );


    List<Nomination>
    findAllByOrderByReceivedAtAsc();


    /*
     * TASK 1 - Officer records
     */
    List<Nomination>
    findByOfficerIdOrderByReceivedAtDesc(
            String officerId
    );


    /*
     * USER VIEW - User bookings
     */
    List<Nomination>
    findByUserIdOrderByReceivedAtDesc(
            String userId
    );
}