package com.ministry.training.service;

import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.repository.TrainingProgrammeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository repository;

    public TrainingProgrammeService(
            TrainingProgrammeRepository repository
    ) {
        this.repository = repository;
    }

    public TrainingProgramme createProgramme(
            TrainingProgramme programme
    ) {

        String programmeId =
                programme
                        .getProgrammeId()
                        .trim()
                        .toUpperCase();

        if (
                repository.existsById(
                        programmeId
                )
        ) {
            throw new RuntimeException(
                    "A training programme with this Programme ID already exists."
            );
        }

        programme.setProgrammeId(
                programmeId
        );

        programme.setTitle(
                programme.getTitle().trim()
        );

        programme.setVenue(
                programme.getVenue().trim()
        );

        programme.setTrainer(
                programme.getTrainer().trim()
        );

        programme.setTargetDepartments(
                programme
                        .getTargetDepartments()
                        .trim()
        );

        return repository.save(
                programme
        );
    }

    public List<TrainingProgramme> getAllProgrammes() {

        return repository
                .findAllByOrderByTrainingDateAsc();
    }

    public TrainingProgramme getProgramme(
            String programmeId
    ) {

        return repository
                .findById(
                        programmeId
                                .trim()
                                .toUpperCase()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Training programme not found."
                        )
                );
    }
}