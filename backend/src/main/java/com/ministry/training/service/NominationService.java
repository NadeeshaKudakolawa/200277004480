package com.ministry.training.service;

import com.ministry.training.exception.DuplicateNominationException;
import com.ministry.training.model.Nomination;
import com.ministry.training.repository.NominationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;

    public NominationService(NominationRepository nominationRepository) {
        this.nominationRepository = nominationRepository;
    }

    public Nomination createNomination(Nomination nomination) {

        String officerId = nomination.getOfficerId().trim();
        String programmeId = nomination.getTrainingProgrammeId().trim();

        boolean duplicate =
                nominationRepository.existsByOfficerIdAndTrainingProgrammeId(
                        officerId,
                        programmeId
                );

        if (duplicate) {
            throw new DuplicateNominationException(
                    "Duplicate nomination detected. " +
                            "This officer is already nominated for this training programme."
            );
        }

        nomination.setOfficerId(officerId);
        nomination.setOfficerName(nomination.getOfficerName().trim());
        nomination.setDepartment(nomination.getDepartment().trim());
        nomination.setTrainingProgrammeId(programmeId);
        nomination.setTrainingProgrammeTitle(
                nomination.getTrainingProgrammeTitle().trim()
        );

        nomination.setVenue(nomination.getVenue().trim());
        nomination.setTrainer(nomination.getTrainer().trim());
        nomination.setTargetDepartments(
                nomination.getTargetDepartments().trim()
        );

        return nominationRepository.save(nomination);
    }

    public List<Nomination> getAllNominations() {
        return nominationRepository.findAll();
    }
}