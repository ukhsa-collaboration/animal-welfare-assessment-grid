package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class AssessmentReasonClientData
{
    @ValidId(message = ValidationConstants.REASON_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.REASON_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long reasonId;

    @NotNull(message = ValidationConstants.REASON_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.REASON_NAME_SIZE_MIN,
        max = ValidationConstants.REASON_NAME_SIZE_MAX, message = ValidationConstants.REASON_NAME_PROPERTY + "|"
            + ValidationConstants.REASON_NAME_SIZE_MIN + "|" + ValidationConstants.REASON_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.REASON_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String reasonName;

    public AssessmentReasonClientData(Long reasonId, String reasonName)
    {
        this.reasonId = reasonId;
        this.reasonName = reasonName;
    }
}
