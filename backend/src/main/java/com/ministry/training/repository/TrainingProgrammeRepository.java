package com.ministry.training.repository;

import com.ministry.training.model.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgrammeRepository
        extends JpaRepository<TrainingProgramme, String> {

    List<TrainingProgramme>
    findAllByOrderByTrainingDateAsc();
}