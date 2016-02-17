package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ParameterClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface ParameterController
{
    ParameterDto getParameterDto(Long parameterId) throws AWNoSuchEntityException;

    Collection<Parameter> getParameters(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    List<EntitySelectDto> getParametersLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    void updateParameter(Long parameterId, ParameterClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    void storeParameter(ParameterClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    Collection<Parameter> getParameters(Long... parametersIds);
}
