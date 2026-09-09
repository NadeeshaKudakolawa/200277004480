package com.ministry.training.service;

import com.ministry.training.exception.DuplicateNominationException;
import com.ministry.training.model.Nomination;
import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.repository.NominationRepository;
import com.ministry.training.repository.TrainingProgrammeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NominationService {

    private static final String NOMINATED =
            "NOMINATED";

    private static final String CONFIRMED =
            "CONFIRMED";

    private static final String WAITING_LIST =
            "WAITING_LIST";

    private static final String CANCELLED =
            "CANCELLED";


    private final NominationRepository nominationRepository;

    private final TrainingProgrammeRepository programmeRepository;


    public NominationService(
            NominationRepository nominationRepository,
            TrainingProgrammeRepository programmeRepository
    ) {

        this.nominationRepository =
                nominationRepository;

        this.programmeRepository =
                programmeRepository;
    }


    /*
     * =====================================================
     * TASK 1 - ADMIN OFFICER NOMINATION
     * =====================================================
     */
    public Nomination createNomination(
            Nomination nomination
    ) {

        if (
                nomination.getOfficerId() == null ||
                        nomination.getOfficerId().isBlank()
        ) {

            throw new RuntimeException(
                    "Officer ID is required."
            );
        }


        if (
                nomination.getOfficerName() == null ||
                        nomination.getOfficerName().isBlank()
        ) {

            throw new RuntimeException(
                    "Officer name is required."
            );
        }


        if (
                nomination.getDepartment() == null ||
                        nomination.getDepartment().isBlank()
        ) {

            throw new RuntimeException(
                    "Department is required."
            );
        }


        if (
                nomination.getTrainingProgrammeId() == null ||
                        nomination.getTrainingProgrammeId().isBlank()
        ) {

            throw new RuntimeException(
                    "Training programme is required."
            );
        }


        String officerId =
                nomination
                        .getOfficerId()
                        .trim()
                        .toUpperCase();


        String programmeId =
                nomination
                        .getTrainingProgrammeId()
                        .trim()
                        .toUpperCase();


        /*
         * TASK 1 duplicate prevention
         */
        boolean duplicate =
                nominationRepository
                        .existsByOfficerIdAndTrainingProgrammeId(
                                officerId,
                                programmeId
                        );


        if (duplicate) {

            throw new DuplicateNominationException(
                    "Duplicate nomination detected. " +
                            "This officer is already nominated " +
                            "for this training programme."
            );
        }


        TrainingProgramme programme =
                getProgramme(
                        programmeId
                );


        nomination.setOfficerId(
                officerId
        );


        nomination.setOfficerName(
                nomination
                        .getOfficerName()
                        .trim()
        );


        nomination.setDepartment(
                nomination
                        .getDepartment()
                        .trim()
        );


        nomination.setUserId(
                null
        );


        applyProgrammeDetails(
                nomination,
                programme
        );


        /*
         * Task 1 only
         */
        nomination.setStatus(
                NOMINATED
        );


        return nominationRepository.save(
                nomination
        );
    }


    /*
     * =====================================================
     * TASK 2 - USER BOOKING
     * =====================================================
     */
    public Nomination createUserBooking(
            String userId,
            String programmeId
    ) {

        if (
                userId == null ||
                        userId.isBlank()
        ) {

            throw new RuntimeException(
                    "User ID is required."
            );
        }


        if (
                programmeId == null ||
                        programmeId.isBlank()
        ) {

            throw new RuntimeException(
                    "Training programme is required."
            );
        }


        String cleanUserId =
                userId
                        .trim()
                        .toUpperCase();


        String cleanProgrammeId =
                programmeId
                        .trim()
                        .toUpperCase();


        TrainingProgramme programme =
                getProgramme(
                        cleanProgrammeId
                );


        /*
         * -------------------------------------------------
         * RULE 1:
         * Same User + Same Programme cannot be booked twice
         * -------------------------------------------------
         */
        boolean duplicate =
                nominationRepository
                        .existsByUserIdAndTrainingProgrammeId(
                                cleanUserId,
                                cleanProgrammeId
                        );


        if (duplicate) {

            throw new DuplicateNominationException(
                    "You have already booked this training programme."
            );
        }


        /*
         * -------------------------------------------------
         * RULE 2:
         * User cannot have TWO active training programmes
         * on the SAME DATE.
         *
         * CANCELLED bookings do not block the date.
         * -------------------------------------------------
         */
        List<Nomination> existingBookings =
                nominationRepository
                        .findByUserIdOrderByReceivedAtDesc(
                                cleanUserId
                        );


        boolean sameDateBookingExists =
                existingBookings
                        .stream()
                        .anyMatch(
                                booking ->

                                        booking.getTrainingDate() != null

                                                &&

                                                booking
                                                        .getTrainingDate()
                                                        .equals(
                                                                programme.getTrainingDate()
                                                        )

                                                &&

                                                !CANCELLED.equals(
                                                        booking.getStatus()
                                                )
                        );


        if (
                sameDateBookingExists
        ) {

            throw new RuntimeException(
                    "You already have another training programme booked for " +
                            programme.getTrainingDate() +
                            ". Two programmes cannot be booked on the same date."
            );
        }


        Nomination booking =
                new Nomination();


        booking.setUserId(
                cleanUserId
        );


        booking.setOfficerId(
                null
        );


        booking.setOfficerName(
                null
        );


        booking.setDepartment(
                null
        );


        applyProgrammeDetails(
                booking,
                programme
        );


        assignCapacityStatus(
                booking,
                programme
        );


        return nominationRepository.save(
                booking
        );
    }


    /*
     * =====================================================
     * GET PROGRAMME
     * =====================================================
     */
    private TrainingProgramme getProgramme(
            String programmeId
    ) {

        return programmeRepository
                .findById(
                        programmeId
                )
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Training programme not found."
                                )
                );
    }


    /*
     * =====================================================
     * COPY PROGRAMME DETAILS
     * =====================================================
     */
    private void applyProgrammeDetails(
            Nomination nomination,
            TrainingProgramme programme
    ) {

        nomination.setTrainingProgrammeId(
                programme.getProgrammeId()
        );


        nomination.setTrainingProgrammeTitle(
                programme.getTitle()
        );


        nomination.setTrainingDate(
                programme.getTrainingDate()
        );


        nomination.setVenue(
                programme.getVenue()
        );


        nomination.setTrainer(
                programme.getTrainer()
        );


        nomination.setMaximumParticipants(
                programme.getMaximumParticipants()
        );


        nomination.setTargetDepartments(
                programme.getTargetDepartments()
        );


        nomination.setReceivedAt(
                LocalDateTime.now()
        );
    }


    /*
     * =====================================================
     * TASK 2 CAPACITY CHECK
     * =====================================================
     */
    private void assignCapacityStatus(
            Nomination booking,
            TrainingProgramme programme
    ) {

        long confirmedCount =
                nominationRepository
                        .countByTrainingProgrammeIdAndStatus(
                                programme.getProgrammeId(),
                                CONFIRMED
                        );


        int capacity =
                programme
                        .getMaximumParticipants();


        if (
                confirmedCount < capacity
        ) {

            booking.setStatus(
                    CONFIRMED
            );

        } else {

            booking.setStatus(
                    WAITING_LIST
            );
        }
    }


    /*
     * =====================================================
     * ALL DATA
     * =====================================================
     */
    public List<Nomination> getAllNominations() {

        return nominationRepository
                .findAllByOrderByReceivedAtAsc();
    }


    /*
     * =====================================================
     * USER BOOKINGS
     * =====================================================
     */
    public List<Nomination> getUserBookings(
            String userId
    ) {

        return nominationRepository
                .findByUserIdOrderByReceivedAtDesc(
                        userId
                                .trim()
                                .toUpperCase()
                );
    }


    /*
     * =====================================================
     * TASK 2 CAPACITY SUMMARY
     * =====================================================
     */
    public Map<String, Object> getProgrammeSummary(
            String programmeId
    ) {

        TrainingProgramme programme =
                getProgramme(
                        programmeId
                                .trim()
                                .toUpperCase()
                );


        long confirmed =
                nominationRepository
                        .countByTrainingProgrammeIdAndStatus(
                                programme.getProgrammeId(),
                                CONFIRMED
                        );


        long waiting =
                nominationRepository
                        .countByTrainingProgrammeIdAndStatus(
                                programme.getProgrammeId(),
                                WAITING_LIST
                        );


        long availableSeats =
                Math.max(
                        programme.getMaximumParticipants()
                                - confirmed,
                        0
                );


        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "programmeId",
                programme.getProgrammeId()
        );


        response.put(
                "title",
                programme.getTitle()
        );


        response.put(
                "capacity",
                programme.getMaximumParticipants()
        );


        response.put(
                "confirmed",
                confirmed
        );


        response.put(
                "waiting",
                waiting
        );


        response.put(
                "availableSeats",
                availableSeats
        );


        response.put(
                "available",
                availableSeats > 0
        );


        return response;
    }


    /*
     * =====================================================
     * ADMIN CANCEL
     * =====================================================
     */
    public Nomination cancelNomination(
            Long id
    ) {

        Nomination nomination =
                nominationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Record not found."
                                        )
                        );


        cancelAndPromote(
                nomination
        );


        return nomination;
    }


    /*
     * =====================================================
     * USER CANCEL OWN BOOKING
     * =====================================================
     */
    public Nomination cancelUserBooking(
            Long bookingId,
            String userId
    ) {

        if (
                userId == null ||
                        userId.isBlank()
        ) {

            throw new RuntimeException(
                    "User ID is required."
            );
        }


        Nomination booking =
                nominationRepository
                        .findById(
                                bookingId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Booking not found."
                                        )
                        );


        if (
                booking.getUserId() == null
        ) {

            throw new RuntimeException(
                    "This record is not a user booking."
            );
        }


        if (
                !booking
                        .getUserId()
                        .equalsIgnoreCase(
                                userId.trim()
                        )
        ) {

            throw new RuntimeException(
                    "You are not allowed to cancel this booking."
            );
        }


        cancelAndPromote(
                booking
        );


        return booking;
    }


    /*
     * =====================================================
     * CANCEL + AUTO PROMOTION
     * =====================================================
     */
    private void cancelAndPromote(
            Nomination nomination
    ) {

        if (
                CANCELLED.equals(
                        nomination.getStatus()
                )
        ) {

            throw new RuntimeException(
                    "This record is already cancelled."
            );
        }


        boolean wasConfirmed =
                CONFIRMED.equals(
                        nomination.getStatus()
                );


        String programmeId =
                nomination
                        .getTrainingProgrammeId();


        nomination.setStatus(
                CANCELLED
        );


        nominationRepository.save(
                nomination
        );


        if (
                wasConfirmed
        ) {

            nominationRepository
                    .findFirstByTrainingProgrammeIdAndStatusOrderByReceivedAtAscIdAsc(
                            programmeId,
                            WAITING_LIST
                    )
                    .ifPresent(
                            waitingUser -> {

                                waitingUser.setStatus(
                                        CONFIRMED
                                );


                                nominationRepository.save(
                                        waitingUser
                                );
                            }
                    );
        }
    }


    /*
     * =====================================================
     * WAITING POSITION
     * =====================================================
     */
    public long getWaitingPosition(
            Long nominationId
    ) {

        Nomination nomination =
                nominationRepository
                        .findById(
                                nominationId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Booking not found."
                                        )
                        );


        if (
                !WAITING_LIST.equals(
                        nomination.getStatus()
                )
        ) {

            return 0;
        }


        List<Nomination> waitingList =
                nominationRepository
                        .findAllByOrderByReceivedAtAsc()
                        .stream()

                        .filter(
                                item ->
                                        WAITING_LIST.equals(
                                                item.getStatus()
                                        )
                        )

                        .filter(
                                item ->
                                        item.getUserId() != null
                        )

                        .filter(
                                item ->
                                        nomination
                                                .getTrainingProgrammeId()
                                                .equals(
                                                        item.getTrainingProgrammeId()
                                                )
                        )

                        .toList();


        for (
                int i = 0;
                i < waitingList.size();
                i++
        ) {

            if (
                    waitingList
                            .get(i)
                            .getId()
                            .equals(
                                    nominationId
                            )
            ) {

                return i + 1;
            }
        }


        return 0;
    }
}