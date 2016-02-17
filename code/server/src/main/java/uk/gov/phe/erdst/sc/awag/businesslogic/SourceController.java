package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SourceClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface SourceController
{
    Collection<Source> getSources(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    List<EntitySelectDto> getSourcesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    void storeSource(SourceClientData clientRequestData, ResponsePayload responsePayload, LoggedUser loggedUser);

    void updateSource(Long sourceId, SourceClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    void deleteSource(Long sourceId, LoggedUser loggedUser);
}
