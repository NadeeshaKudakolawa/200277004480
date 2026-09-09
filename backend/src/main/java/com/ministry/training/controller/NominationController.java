package com.ministry.training.controller;

import com.ministry.training.model.Nomination;
import com.ministry.training.service.NominationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nominations")
public class NominationController {

    private final NominationService
            nominationService;


    public NominationController(
            NominationService nominationService
    ) {

        this.nominationService =
                nominationService;
    }


    /*
     * ADMIN - TASK 1
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nomination createNomination(
            @RequestBody
            Nomination nomination
    ) {

        return nominationService
                .createNomination(
                        nomination
                );
    }


    /*
     * USER BOOKING
     *
     * {
     *   "userId": "USER001",
     *   "programmeId": "TR001"
     * }
     */
    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public Nomination createUserBooking(
            @RequestBody
            Map<String, String> request
    ) {

        return nominationService
                .createUserBooking(
                        request.get("userId"),
                        request.get("programmeId")
                );
    }


    /*
     * ALL NOMINATIONS / BOOKINGS
     */
    @GetMapping
    public List<Nomination> getAllNominations() {

        return nominationService
                .getAllNominations();
    }


    /*
     * USER'S BOOKINGS
     */
    @GetMapping("/user/{userId}")
    public List<Nomination> getUserBookings(
            @PathVariable
            String userId
    ) {

        return nominationService
                .getUserBookings(
                        userId
                );
    }


    /*
     * CAPACITY SUMMARY
     */
    @GetMapping(
            "/programme/{programmeId}/summary"
    )
    public Map<String, Object> getProgrammeSummary(
            @PathVariable
            String programmeId
    ) {

        return nominationService
                .getProgrammeSummary(
                        programmeId
                );
    }


    /*
     * WAITING POSITION
     */
    @GetMapping(
            "/{id}/waiting-position"
    )
    public Map<String, Long> getWaitingPosition(
            @PathVariable
            Long id
    ) {

        return Map.of(
                "position",
                nominationService
                        .getWaitingPosition(
                                id
                        )
        );
    }


    /*
     * ADMIN CANCEL
     */
    @PutMapping(
            "/{id}/cancel"
    )
    public Map<String, Object> cancelNomination(
            @PathVariable
            Long id
    ) {

        Nomination cancelled =
                nominationService
                        .cancelNomination(
                                id
                        );


        return Map.of(
                "message",
                "Nomination cancelled successfully.",

                "nomination",
                cancelled
        );
    }


    /*
     * USER CANCEL OWN BOOKING
     *
     * {
     *   "userId": "USER001"
     * }
     */
    @PutMapping(
            "/bookings/{id}/cancel"
    )
    public Map<String, Object> cancelUserBooking(
            @PathVariable
            Long id,

            @RequestBody
            Map<String, String> request
    ) {

        Nomination cancelled =
                nominationService
                        .cancelUserBooking(
                                id,
                                request.get(
                                        "userId"
                                )
                        );


        return Map.of(
                "message",
                "Booking cancelled successfully. " +
                        "If the cancelled booking was confirmed, " +
                        "the first waiting participant was automatically promoted.",

                "booking",
                cancelled
        );
    }
}