package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface AnimalController
{
    Collection<AnimalDto> getNonDeletedAnimals(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    AssessmentTemplateDto getAnimalAssessmentTemplateDto(Long animalId) throws AWNoSuchEntityException;

    Animal getAnimal(Long animalId) throws AWNoSuchEntityException;

    AnimalDto getAnimalDto(Long animalId) throws AWNoSuchEntityException;

    void storeAnimal(AnimalClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    void updateAnimal(Long animalId, AnimalClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    void deleteAnimal(Long animalId, LoggedUser loggedUser) throws AWNoSuchEntityException;

    List<EntitySelectDto> getNonDeletedAnimalsLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    AnimalDto getNonDeletedAnimalDtoById(Long animalId);

    List<AnimalDto> getNonDeletedAnimalLikeFullDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    List<EntitySelectDto> getNonDeletedAnimalsLikeSexDtos(String like, String sex, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

}
