package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface AssessmentReasonController
{
    Collection<AssessmentReason> getReasons(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    AssessmentReason getReason(String reasonName) throws AWNoSuchEntityException;

    void storeReason(AssessmentReasonClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    void updateReason(Long reasonId, AssessmentReasonClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    Collection<EntitySelectDto> getReasonsLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    AssessmentReason getReason(Long reasonId) throws AWNoSuchEntityException;
}
