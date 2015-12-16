package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWTemplateInUseException;

public interface AssessmentTemplateController
{
    AssessmentTemplate getAssessmentTemplateByAnimalId(Long animalId) throws AWNoSuchEntityException;

    AssessmentTemplateDto getAssessmentTemplateDtoByAnimalId(Long animalId) throws AWNoSuchEntityException;

    Collection<AssessmentTemplateDto> getAssessmentTemplatesDtos(Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    List<EntitySelectDto> getAssessmentTemplatesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    AssessmentTemplateDto getAssessmentTemplateDtoById(Long templateId) throws AWNoSuchEntityException;

    void storeAssessmentTemplate(AssessmentTemplateClientData clientData, ResponsePayload responsePayload);

    void updateAssessmentTemplate(Long assessmentTemplateId, AssessmentTemplateClientData clientData,
        ResponsePayload responsePayload);

    void deleteTemplateParameter(Long templateId, Long parameterId)
        throws AWNoSuchEntityException, AWTemplateInUseException;
}
