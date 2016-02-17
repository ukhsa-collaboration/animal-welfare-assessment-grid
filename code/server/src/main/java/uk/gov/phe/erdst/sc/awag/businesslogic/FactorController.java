package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.client.FactorClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface FactorController
{
    Collection<Factor> getFactors(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    Factor getFactor(Long factorId) throws AWNoSuchEntityException;

    List<EntitySelectDto> getFactorsLike(String like, Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    void updateFactor(Long factorId, FactorClientData clientData, ResponsePayload responsePayload,
            LoggedUser loggedUser);

    void storeFactor(FactorClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);
}
