package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentsGetRequest;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAssessmentsGetRequest
public class AssessmentsGetRequestParams
{
    @Valid
    @NotNull(message = ValidationConstants.DATE_FROM, payload = ValidationConstants.PropertyMustBeProvided.class)
    public String dateFrom;

    @Valid
    @NotNull(message = ValidationConstants.DATE_TO, payload = ValidationConstants.PropertyMustBeProvided.class)
    public String dateTo;

    @Valid
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    @NotNull(message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long animalId;
}
