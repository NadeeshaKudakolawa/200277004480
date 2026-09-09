package com.ministry.training.controller;

import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.service.TrainingProgrammeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmes")
public class TrainingProgrammeController {

    private final TrainingProgrammeService programmeService;

    public TrainingProgrammeController(
            TrainingProgrammeService programmeService
    ) {
        this.programmeService =
                programmeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingProgramme createProgramme(
            @Valid
            @RequestBody
            TrainingProgramme programme
    ) {

        return programmeService
                .createProgramme(
                        programme
                );
    }

    @GetMapping
    public List<TrainingProgramme> getAllProgrammes() {

        return programmeService
                .getAllProgrammes();
    }

    @GetMapping("/{programmeId}")
    public TrainingProgramme getProgramme(
            @PathVariable
            String programmeId
    ) {

        return programmeService
                .getProgramme(
                        programmeId
                );
    }
}