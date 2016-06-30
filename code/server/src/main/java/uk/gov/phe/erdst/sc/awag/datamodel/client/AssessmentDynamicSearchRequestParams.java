package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.constraints.Min;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentSearchRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAssessmentSearchRequest
public class AssessmentDynamicSearchRequestParams extends AssessmentSearchRequestParams
{
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.STUDY_GROUP_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long studyGroupId;
}
