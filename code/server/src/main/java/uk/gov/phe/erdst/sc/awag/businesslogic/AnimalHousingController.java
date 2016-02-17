package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface AnimalHousingController
{
    Collection<AnimalHousing> getAnimalHousings(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    AnimalHousing getAnimalHousing(String name) throws AWNoSuchEntityException;

    void storeAnimalHousing(AnimalHousingClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    void updateAnimalHousing(Long animalHousingId, AnimalHousingClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    List<EntitySelectDto> getHousingLike(String like, Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    AnimalHousing getAnimalHousing(Long housingId) throws AWNoSuchEntityException;
}
