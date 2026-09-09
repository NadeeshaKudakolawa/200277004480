package com.ministry.training.repository;

import com.ministry.training.model.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominationRepository
        extends JpaRepository<Nomination, Long> {


    /*
     * TASK 1
     */
    boolean existsByOfficerIdAndTrainingProgrammeId(
            String officerId,
            String trainingProgrammeId
    );


    /*
     * TASK 2
     */
    long countByTrainingProgrammeIdAndStatus(
            String trainingProgrammeId,
            String status
    );


    List<Nomination>
    findByTrainingProgrammeIdAndStatusOrderByReceivedAtAscIdAsc(
            String trainingProgrammeId,
            String status
    );


    List<Nomination>
    findAllByOrderByReceivedAtAsc();


    /*
     * TASK 3 - previous participation
     */
    List<Nomination>
    findByOfficerIdOrderByTrainingDateDesc(
            String officerId
    );
}