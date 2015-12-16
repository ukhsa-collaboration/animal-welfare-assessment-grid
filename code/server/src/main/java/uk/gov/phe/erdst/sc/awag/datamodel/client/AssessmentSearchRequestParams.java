package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.constraints.Min;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentSearchRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAssessmentSearchRequest
public class AssessmentSearchRequestParams
{
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long animalId;

    // Validated by the custom validator
    public String dateFrom;

    // Validated by the custom validator
    public String dateTo;

    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.USER_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long userId;

    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.REASON_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long reasonId;

    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.STUDY_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long studyId;

    public Boolean isComplete;

}
