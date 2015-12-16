package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ScaleClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.ScaleDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public interface ScaleController
{

    void updateScale(Long scaleId, ScaleClientData clientData, ResponsePayload responsePayload);

    void storeScale(ScaleClientData clientData, ResponsePayload responsePayload);

    Collection<Scale>
        getScales(Integer offset, Integer limit, ResponsePayload responsePayload, boolean includeMetadata);

    List<EntitySelectDto> getScalesLike(String like, Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata);

    ScaleDto getScale(Long scaleId) throws AWNoSuchEntityException;
}
