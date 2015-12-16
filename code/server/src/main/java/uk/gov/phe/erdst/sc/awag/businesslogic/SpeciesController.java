package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SpeciesClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;

public interface SpeciesController
{
    Collection<Species> getSpecies(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    List<EntitySelectDto> getSpeciesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    void storeSpecies(SpeciesClientData clientRequestData, ResponsePayload responsePayload);

    void updateSpecies(Long speciesId, SpeciesClientData clientData, ResponsePayload responsePayload);

    void deleteSpecies(Long speciesId);
}
