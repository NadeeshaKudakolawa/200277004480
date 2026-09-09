package com.ministry.training.repository;

import com.ministry.training.model.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NominationRepository extends JpaRepository<Nomination, Long> {

    boolean existsByOfficerIdAndTrainingProgrammeId(
            String officerId,
            String trainingProgrammeId
    );
}