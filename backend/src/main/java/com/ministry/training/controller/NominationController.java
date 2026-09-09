package com.ministry.training.controller;

import com.ministry.training.model.Nomination;
import com.ministry.training.service.NominationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nominations")
public class NominationController {

    private final NominationService nominationService;


    public NominationController(
            NominationService nominationService
    ) {

        this.nominationService =
                nominationService;
    }


    /*
     * Task 1 + Task 3 + Task 2
     */
    @PostMapping
    public ResponseEntity<Nomination>
    createNomination(
            @RequestBody Nomination nomination
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        nominationService
                                .createNomination(
                                        nomination
                                )
                );
    }


    /*
     * ALL NOMINATIONS
     */
    @GetMapping
    public ResponseEntity<List<Nomination>>
    getAllNominations() {

        return ResponseEntity.ok(
                nominationService
                        .getAllNominations()
        );
    }


    /*
     * CAPACITY SUMMARY
     */
    @GetMapping(
            "/programme/{programmeId}/summary"
    )
    public ResponseEntity<Map<String, Object>>
    getProgrammeSummary(
            @PathVariable String programmeId
    ) {

        return ResponseEntity.ok(
                nominationService
                        .getProgrammeSummary(
                                programmeId
                        )
        );
    }


    /*
     * WAITING POSITION
     */
    @GetMapping(
            "/{id}/waiting-position"
    )
    public ResponseEntity<Map<String, Object>>
    getWaitingPosition(
            @PathVariable Long id
    ) {

        long position =
                nominationService
                        .getWaitingPosition(
                                id
                        );


        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "nominationId",
                id
        );


        response.put(
                "waitingPosition",
                position
        );


        return ResponseEntity.ok(
                response
        );
    }


    /*
     * CANCEL + AUTO PROMOTION
     */
    @PutMapping(
            "/{id}/cancel"
    )
    public ResponseEntity<Nomination>
    cancelNomination(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                nominationService
                        .cancelNomination(
                                id
                        )
        );
    }
}