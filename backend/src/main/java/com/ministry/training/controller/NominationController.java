package com.ministry.training.controller;

import com.ministry.training.model.Nomination;
import com.ministry.training.service.NominationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nominations")
public class NominationController {

    private final NominationService nominationService;

    public NominationController(NominationService nominationService) {
        this.nominationService = nominationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nomination createNomination(
            @Valid @RequestBody Nomination nomination
    ) {
        return nominationService.createNomination(nomination);
    }

    @GetMapping
    public List<Nomination> getAllNominations() {
        return nominationService.getAllNominations();
    }
}