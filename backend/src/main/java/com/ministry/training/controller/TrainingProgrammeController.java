package com.ministry.training.controller;

import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.service.TrainingProgrammeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /*
     * CREATE
     */
    @PostMapping
    public ResponseEntity<TrainingProgramme>
    createProgramme(
            @RequestBody TrainingProgramme programme
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        programmeService
                                .createProgramme(
                                        programme
                                )
                );
    }


    /*
     * GET ALL
     */
    @GetMapping
    public ResponseEntity<List<TrainingProgramme>>
    getAllProgrammes() {

        return ResponseEntity.ok(
                programmeService
                        .getAllProgrammes()
        );
    }


    /*
     * GET ONE
     */
    @GetMapping("/{programmeId}")
    public ResponseEntity<TrainingProgramme>
    getProgramme(
            @PathVariable String programmeId
    ) {

        return ResponseEntity.ok(
                programmeService
                        .getProgramme(
                                programmeId
                        )
        );
    }


    /*
     * TASK 3
     * UPDATE ELIGIBILITY RULES
     */
    @PutMapping("/{programmeId}")
    public ResponseEntity<TrainingProgramme>
    updateProgramme(
            @PathVariable String programmeId,
            @RequestBody TrainingProgramme programme
    ) {

        return ResponseEntity.ok(
                programmeService
                        .updateProgramme(
                                programmeId,
                                programme
                        )
        );
    }
}