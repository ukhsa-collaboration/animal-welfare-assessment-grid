package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.StudySimpleDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface StudyController
{
    void storeStudy(StudyClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    void updateStudy(Long studyId, StudyClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    List<EntitySelectDto> getStudyLikeDtos(String like, Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    StudyClientData getStudy(Long studyId) throws AWNoSuchEntityException;

    /**
     * Retrieves a study an animal is assigned to.
     * @param animal
     * @return a study if the given animal is assigned to a study group which is part of the study. <br />
     *         <strong>null</strong> if the given animal is not assigned to a study
     * @throws AWMultipleResultException
     *             if the given animal is assigned to more than one study, which should not be
     *             possible
     */
    Study getStudyWithAnimal(Long animalId) throws AWNoSuchEntityException, AWMultipleResultException;

    StudySimpleDto getStudyWithAnimalDto(Long animalId) throws AWNoSuchEntityException;

    Collection<Study>
        getStudies(Integer offset, Integer limit, ResponsePayload responsePayload, boolean includeMetadata);
}
