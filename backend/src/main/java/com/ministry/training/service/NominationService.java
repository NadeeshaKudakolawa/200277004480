package com.ministry.training.service;

import com.ministry.training.exception.DuplicateNominationException;
import com.ministry.training.model.Nomination;
import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.repository.NominationRepository;
import com.ministry.training.repository.TrainingProgrammeRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NominationService {

    private static final String CONFIRMED =
            "CONFIRMED";

    private static final String WAITING_LIST =
            "WAITING_LIST";

    private static final String CANCELLED =
            "CANCELLED";

    private static final String NOT_ELIGIBLE =
            "NOT_ELIGIBLE";


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
     * COMPLETE NOMINATION PROCESS
     * =====================================================
     *
     * Task 1 -> Duplicate
     * Task 3 -> Eligibility
     * Task 2 -> Capacity
     */
    public Nomination createNomination(
            Nomination nomination
    ) {

        validateOfficerInput(
                nomination
        );


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
         * =================================================
         * TASK 1 - DUPLICATE CHECK
         * =================================================
         */
        if (
                nominationRepository
                        .existsByOfficerIdAndTrainingProgrammeId(
                                officerId,
                                programmeId
                        )
        ) {

            throw new DuplicateNominationException(
                    "Duplicate nomination detected. " +
                            "This officer has already been nominated " +
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


        if (
                nomination.getGrade() != null
        ) {

            nomination.setGrade(
                    nomination
                            .getGrade()
                            .trim()
            );
        }


        if (
                nomination.getDesignation() != null
        ) {

            nomination.setDesignation(
                    nomination
                            .getDesignation()
                            .trim()
            );
        }


        applyProgrammeDetails(
                nomination,
                programme
        );


        /*
         * =================================================
         * TASK 3 - ELIGIBILITY
         * =================================================
         */
        String eligibilityError =
                checkEligibility(
                        nomination,
                        programme
                );


        if (
                eligibilityError != null
        ) {

            throw new RuntimeException(
                    "Officer is not eligible. " +
                            eligibilityError
            );
        }


        /*
         * =================================================
         * TASK 2 - CAPACITY
         * =================================================
         */
        assignCapacityStatus(
                nomination,
                programme
        );


        nomination.setStatusReason(
                null
        );


        return nominationRepository.save(
                nomination
        );
    }


    /*
     * =====================================================
     * INPUT VALIDATION
     * =====================================================
     */
    private void validateOfficerInput(
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
                    "Department / Division is required."
            );
        }


        if (
                nomination.getYearsOfService() == null
        ) {

            throw new RuntimeException(
                    "Years of service is required."
            );
        }


        if (
                nomination.getYearsOfService() < 0
        ) {

            throw new RuntimeException(
                    "Years of service cannot be negative."
            );
        }


        if (
                nomination.getTrainingProgrammeId() == null ||
                        nomination
                                .getTrainingProgrammeId()
                                .isBlank()
        ) {

            throw new RuntimeException(
                    "Training programme is required."
            );
        }
    }


    /*
     * =====================================================
     * TASK 3 - ELIGIBILITY ENGINE
     * =====================================================
     */
    private String checkEligibility(
            Nomination nomination,
            TrainingProgramme programme
    ) {

        /*
         * -------------------------------
         * DEPARTMENT
         * -------------------------------
         */
        String eligibleDepartments =
                programme.getEligibleDepartments();


        if (
                eligibleDepartments != null &&
                        !eligibleDepartments.isBlank() &&
                        !eligibleDepartments.equalsIgnoreCase(
                                "All Departments"
                        )
        ) {

            String officerDepartment =
                    normalizeText(
                            nomination.getDepartment()
                    );


            String[] departments =
                    eligibleDepartments
                            .split("[,;]");


            boolean allowed =
                    false;


            for (
                    String department :
                    departments
            ) {

                if (
                        officerDepartment.equals(
                                normalizeText(
                                        department
                                )
                        )
                ) {

                    allowed =
                            true;

                    break;
                }
            }


            if (!allowed) {

                return "Officer department is not eligible for this programme.";
            }
        }


        /*
         * -------------------------------
         * GRADE
         * -------------------------------
         */
        String requiredGrade =
                programme.getRequiredGrade();


        if (
                requiredGrade != null &&
                        !requiredGrade.isBlank() &&
                        !requiredGrade.equalsIgnoreCase(
                                "Any"
                        )
        ) {

            if (
                    nomination.getGrade() == null ||
                            nomination.getGrade().isBlank()
            ) {

                return "Officer grade is required for this programme.";
            }


            if (
                    !normalizeText(
                            nomination.getGrade()
                    ).equals(
                            normalizeText(
                                    requiredGrade
                            )
                    )
            ) {

                return "Required grade is " +
                        requiredGrade +
                        ".";
            }
        }


        /*
         * -------------------------------
         * DESIGNATION
         * -------------------------------
         */
        String requiredDesignation =
                programme
                        .getRequiredDesignation();


        if (
                requiredDesignation != null &&
                        !requiredDesignation.isBlank() &&
                        !requiredDesignation.equalsIgnoreCase(
                                "Any"
                        )
        ) {

            if (
                    nomination.getDesignation() == null ||
                            nomination.getDesignation().isBlank()
            ) {

                return "Officer designation is required for this programme.";
            }


            if (
                    !normalizeText(
                            nomination.getDesignation()
                    ).equals(
                            normalizeText(
                                    requiredDesignation
                            )
                    )
            ) {

                return "Required designation is " +
                        requiredDesignation +
                        ".";
            }
        }


        /*
         * -------------------------------
         * YEARS OF SERVICE
         * -------------------------------
         */
        int minimumYears =
                programme
                        .getMinimumYearsOfService() == null
                        ?
                        0
                        :
                        programme
                                .getMinimumYearsOfService();


        if (
                nomination.getYearsOfService() <
                        minimumYears
        ) {

            return "Minimum " +
                    minimumYears +
                    " years of service is required.";
        }


        /*
         * -------------------------------
         * PREVIOUS PARTICIPATION
         * -------------------------------
         */
        int restrictionMonths =
                programme
                        .getRepeatRestrictionMonths() == null
                        ?
                        0
                        :
                        programme
                                .getRepeatRestrictionMonths();


        if (
                restrictionMonths > 0
        ) {

            List<Nomination> history =
                    nominationRepository
                            .findByOfficerIdOrderByTrainingDateDesc(
                                    nomination.getOfficerId()
                            );


            LocalDate currentTrainingDate =
                    programme.getTrainingDate();


            LocalDate earliestAllowedDate =
                    currentTrainingDate
                            .minusMonths(
                                    restrictionMonths
                            );


            for (
                    Nomination previous :
                    history
            ) {

                /*
                 * Only previous confirmed
                 * participation is considered.
                 */
                if (
                        !CONFIRMED.equals(
                                previous.getStatus()
                        )
                ) {

                    continue;
                }


                if (
                        previous.getTrainingDate() == null ||
                                previous.getTrainingProgrammeTitle() == null
                ) {

                    continue;
                }


                boolean sameTraining =
                        normalizeText(
                                previous
                                        .getTrainingProgrammeTitle()
                        ).equals(
                                normalizeText(
                                        programme.getTitle()
                                )
                        );


                if (!sameTraining) {

                    continue;
                }


                boolean beforeCurrentProgramme =
                        previous
                                .getTrainingDate()
                                .isBefore(
                                        currentTrainingDate
                                );


                boolean insideRestrictionPeriod =
                        !previous
                                .getTrainingDate()
                                .isBefore(
                                        earliestAllowedDate
                                );


                if (
                        beforeCurrentProgramme &&
                                insideRestrictionPeriod
                ) {

                    return "Officer participated in the same training within the previous " +
                            restrictionMonths +
                            " months.";
                }
            }
        }


        return null;
    }


    /*
     * =====================================================
     * TASK 2 - CAPACITY
     * =====================================================
     */
    private void assignCapacityStatus(
            Nomination nomination,
            TrainingProgramme programme
    ) {

        long confirmedCount =
                nominationRepository
                        .countByTrainingProgrammeIdAndStatus(
                                programme.getProgrammeId(),
                                CONFIRMED
                        );


        if (
                confirmedCount <
                        programme.getMaximumParticipants()
        ) {

            nomination.setStatus(
                    CONFIRMED
            );

        } else {

            nomination.setStatus(
                    WAITING_LIST
            );
        }
    }


    /*
     * =====================================================
     * PROGRAMME SNAPSHOT
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


        nomination.setReceivedAt(
                LocalDateTime.now()
        );
    }


    /*
     * =====================================================
     * PROGRAMME
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
     * GET ALL
     * =====================================================
     */
    public List<Nomination> getAllNominations() {

        return nominationRepository
                .findAllByOrderByReceivedAtAsc();
    }


    /*
     * =====================================================
     * TASK 2 SUMMARY
     * =====================================================
     */
    public Map<String, Object>
    getProgrammeSummary(
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
                        programme
                                .getMaximumParticipants()
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
     * TASK 2 CANCEL
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
                                                "Nomination not found."
                                        )
                        );


        if (
                CANCELLED.equals(
                        nomination.getStatus()
                )
        ) {

            throw new RuntimeException(
                    "Nomination is already cancelled."
            );
        }


        if (
                !CONFIRMED.equals(
                        nomination.getStatus()
                ) &&
                        !WAITING_LIST.equals(
                                nomination.getStatus()
                        )
        ) {

            throw new RuntimeException(
                    "This nomination cannot be cancelled."
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


        nomination.setStatusReason(
                "Cancelled by training coordinator."
        );


        nominationRepository.save(
                nomination
        );


        /*
         * Only confirmed cancellation creates
         * an available seat.
         */
        if (wasConfirmed) {

            promoteFirstEligibleWaitingOfficer(
                    programmeId
            );
        }


        return nomination;
    }


    /*
     * =====================================================
     * FIRST ELIGIBLE WAITING OFFICER
     * =====================================================
     */
    private void promoteFirstEligibleWaitingOfficer(
            String programmeId
    ) {

        TrainingProgramme programme =
                getProgramme(
                        programmeId
                );


        List<Nomination> waitingList =
                nominationRepository
                        .findByTrainingProgrammeIdAndStatusOrderByReceivedAtAscIdAsc(
                                programmeId,
                                WAITING_LIST
                        );


        for (
                Nomination waitingOfficer :
                waitingList
        ) {

            /*
             * Rules may have changed,
             * therefore eligibility is checked again.
             */
            String eligibilityError =
                    checkEligibility(
                            waitingOfficer,
                            programme
                    );


            if (
                    eligibilityError == null
            ) {

                waitingOfficer.setStatus(
                        CONFIRMED
                );


                waitingOfficer.setStatusReason(
                        "Automatically promoted from the waiting list."
                );


                nominationRepository.save(
                        waitingOfficer
                );


                return;

            } else {

                /*
                 * Waiting officer no longer
                 * satisfies current eligibility rules.
                 */
                waitingOfficer.setStatus(
                        NOT_ELIGIBLE
                );


                waitingOfficer.setStatusReason(
                        eligibilityError
                );


                nominationRepository.save(
                        waitingOfficer
                );
            }
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
                                                "Nomination not found."
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
                        .findByTrainingProgrammeIdAndStatusOrderByReceivedAtAscIdAsc(
                                nomination
                                        .getTrainingProgrammeId(),
                                WAITING_LIST
                        );


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


    /*
     * =====================================================
     * NORMALIZE TEXT
     * =====================================================
     */
    private String normalizeText(
            String value
    ) {

        if (
                value == null
        ) {

            return "";
        }


        return value
                .trim()
                .toUpperCase()

                /*
                 * Finance Division = Finance
                 */
                .replaceAll(
                        "\\b(DIVISION|DEPARTMENT|DEPT)\\b",
                        ""
                )

                .replaceAll(
                        "[^A-Z0-9]",
                        ""
                );
    }
}