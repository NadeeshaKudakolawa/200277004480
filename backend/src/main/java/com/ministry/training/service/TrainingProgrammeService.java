package com.ministry.training.service;

import com.ministry.training.model.TrainingProgramme;
import com.ministry.training.repository.TrainingProgrammeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TrainingProgrammeService {

    private static final Set<String> ALLOWED_TYPES =
            Set.of(
                    "FINANCIAL",
                    "TECHNICAL",
                    "MANAGEMENT",
                    "GENERAL"
            );


    private final TrainingProgrammeRepository programmeRepository;


    public TrainingProgrammeService(
            TrainingProgrammeRepository programmeRepository
    ) {

        this.programmeRepository =
                programmeRepository;
    }


    /*
     * =====================================================
     * CREATE PROGRAMME
     * =====================================================
     */
    public TrainingProgramme createProgramme(
            TrainingProgramme programme
    ) {

        validateNewProgramme(
                programme
        );


        String programmeId =
                programme
                        .getProgrammeId()
                        .trim()
                        .toUpperCase();


        if (
                programmeRepository.existsById(
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


        normalizeProgramme(
                programme
        );


        return programmeRepository.save(
                programme
        );
    }


    /*
     * =====================================================
     * UPDATE PROGRAMME TYPE + ELIGIBILITY RULES
     * =====================================================
     */
    public TrainingProgramme updateProgramme(
            String programmeId,
            TrainingProgramme updatedRules
    ) {

        if (
                programmeId == null ||
                        programmeId.isBlank()
        ) {

            throw new RuntimeException(
                    "Programme ID is required."
            );
        }


        String cleanProgrammeId =
                programmeId
                        .trim()
                        .toUpperCase();


        TrainingProgramme existing =
                programmeRepository
                        .findById(
                                cleanProgrammeId
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Training programme not found."
                                        )
                        );


        validateProgrammeType(
                updatedRules.getProgrammeType()
        );


        validateEligibilityRules(
                updatedRules
        );


        existing.setProgrammeType(
                updatedRules.getProgrammeType()
        );


        existing.setEligibleDepartments(
                updatedRules.getEligibleDepartments()
        );


        existing.setRequiredGrade(
                updatedRules.getRequiredGrade()
        );


        existing.setRequiredDesignation(
                updatedRules.getRequiredDesignation()
        );


        existing.setMinimumYearsOfService(
                updatedRules.getMinimumYearsOfService()
        );


        existing.setRepeatRestrictionMonths(
                updatedRules.getRepeatRestrictionMonths()
        );


        normalizeEligibilityRules(
                existing
        );


        return programmeRepository.save(
                existing
        );
    }


    /*
     * =====================================================
     * GET ALL
     * =====================================================
     */
    public List<TrainingProgramme> getAllProgrammes() {

        return programmeRepository
                .findAllByOrderByTrainingDateAsc();
    }


    /*
     * =====================================================
     * GET ONE
     * =====================================================
     */
    public TrainingProgramme getProgramme(
            String programmeId
    ) {

        if (
                programmeId == null ||
                        programmeId.isBlank()
        ) {

            throw new RuntimeException(
                    "Programme ID is required."
            );
        }


        return programmeRepository
                .findById(
                        programmeId
                                .trim()
                                .toUpperCase()
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
     * CREATE VALIDATION
     * =====================================================
     */
    private void validateNewProgramme(
            TrainingProgramme programme
    ) {

        if (
                programme.getProgrammeId() == null ||
                        programme.getProgrammeId().isBlank()
        ) {

            throw new RuntimeException(
                    "Programme ID is required."
            );
        }


        validateProgrammeType(
                programme.getProgrammeType()
        );


        if (
                programme.getTitle() == null ||
                        programme.getTitle().isBlank()
        ) {

            throw new RuntimeException(
                    "Programme title is required."
            );
        }


        if (
                programme.getTrainingDate() == null
        ) {

            throw new RuntimeException(
                    "Training date is required."
            );
        }


        if (
                programme
                        .getTrainingDate()
                        .isBefore(
                                LocalDate.now()
                        )
        ) {

            throw new RuntimeException(
                    "Training date cannot be in the past."
            );
        }


        if (
                programme.getVenue() == null ||
                        programme.getVenue().isBlank()
        ) {

            throw new RuntimeException(
                    "Venue is required."
            );
        }


        if (
                programme.getTrainer() == null ||
                        programme.getTrainer().isBlank()
        ) {

            throw new RuntimeException(
                    "Trainer / Resource Person is required."
            );
        }


        if (
                programme.getMaximumParticipants() == null ||
                        programme.getMaximumParticipants() < 1
        ) {

            throw new RuntimeException(
                    "Maximum participants must be at least 1."
            );
        }


        validateEligibilityRules(
                programme
        );
    }


    /*
     * =====================================================
     * PROGRAMME TYPE VALIDATION
     * =====================================================
     */
    private void validateProgrammeType(
            String programmeType
    ) {

        if (
                programmeType == null ||
                        programmeType.isBlank()
        ) {

            throw new RuntimeException(
                    "Programme type is required."
            );
        }


        String cleanType =
                programmeType
                        .trim()
                        .toUpperCase();


        if (
                !ALLOWED_TYPES.contains(
                        cleanType
                )
        ) {

            throw new RuntimeException(
                    "Programme type must be Financial, Technical, Management or General."
            );
        }
    }


    /*
     * =====================================================
     * TASK 3 RULE VALIDATION
     * =====================================================
     */
    private void validateEligibilityRules(
            TrainingProgramme programme
    ) {

        if (
                programme.getMinimumYearsOfService() != null &&
                        programme.getMinimumYearsOfService() < 0
        ) {

            throw new RuntimeException(
                    "Minimum years of service cannot be negative."
            );
        }


        if (
                programme.getRepeatRestrictionMonths() != null &&
                        programme.getRepeatRestrictionMonths() < 0
        ) {

            throw new RuntimeException(
                    "Repeat restriction months cannot be negative."
            );
        }
    }


    /*
     * =====================================================
     * NORMALIZE PROGRAMME
     * =====================================================
     */
    private void normalizeProgramme(
            TrainingProgramme programme
    ) {

        programme.setProgrammeType(
                programme
                        .getProgrammeType()
                        .trim()
                        .toUpperCase()
        );


        programme.setTitle(
                programme
                        .getTitle()
                        .trim()
        );


        programme.setVenue(
                programme
                        .getVenue()
                        .trim()
        );


        programme.setTrainer(
                programme
                        .getTrainer()
                        .trim()
        );


        normalizeEligibilityRules(
                programme
        );
    }


    /*
     * =====================================================
     * NORMALIZE ELIGIBILITY RULES
     * =====================================================
     */
    private void normalizeEligibilityRules(
            TrainingProgramme programme
    ) {

        if (
                programme.getProgrammeType() != null
        ) {

            programme.setProgrammeType(
                    programme
                            .getProgrammeType()
                            .trim()
                            .toUpperCase()
            );
        }


        /*
         * If departments are blank,
         * provide sensible defaults based on type.
         */
        if (
                programme.getEligibleDepartments() == null ||
                        programme.getEligibleDepartments().isBlank()
        ) {

            String type =
                    programme.getProgrammeType();


            if (
                    "FINANCIAL".equals(type)
            ) {

                programme.setEligibleDepartments(
                        "Finance, Budget, Planning"
                );

            } else if (
                    "TECHNICAL".equals(type)
            ) {

                programme.setEligibleDepartments(
                        "IT, ICT"
                );

            } else {

                programme.setEligibleDepartments(
                        "All Departments"
                );
            }

        } else {

            programme.setEligibleDepartments(
                    programme
                            .getEligibleDepartments()
                            .trim()
            );
        }


        if (
                programme.getRequiredGrade() == null ||
                        programme.getRequiredGrade().isBlank()
        ) {

            programme.setRequiredGrade(
                    "Any"
            );

        } else {

            programme.setRequiredGrade(
                    programme
                            .getRequiredGrade()
                            .trim()
            );
        }


        if (
                programme.getRequiredDesignation() == null ||
                        programme.getRequiredDesignation().isBlank()
        ) {

            programme.setRequiredDesignation(
                    "Any"
            );

        } else {

            programme.setRequiredDesignation(
                    programme
                            .getRequiredDesignation()
                            .trim()
            );
        }


        if (
                programme.getMinimumYearsOfService() == null
        ) {

            programme.setMinimumYearsOfService(
                    0
            );
        }


        if (
                programme.getRepeatRestrictionMonths() == null
        ) {

            programme.setRepeatRestrictionMonths(
                    12
            );
        }
    }
}