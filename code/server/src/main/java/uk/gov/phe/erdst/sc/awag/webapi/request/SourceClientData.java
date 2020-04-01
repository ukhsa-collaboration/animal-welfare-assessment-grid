package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class SourceClientData
{
    @ValidId(message = ValidationConstants.SOURCE_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.SOURCE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long sourceId;

    @NotNull(message = ValidationConstants.SOURCE_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.SOURCE_NAME_SIZE_MIN, max = ValidationConstants.SOURCE_NAME_SIZE_MAX,
        message = ValidationConstants.SOURCE_NAME_PROPERTY + "|" + ValidationConstants.SOURCE_NAME_SIZE_MIN + "|"
            + ValidationConstants.SOURCE_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.SOURCE_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String sourceName;

    public SourceClientData()
    {
    }

    public SourceClientData(Long sourceId, String sourceName)
    {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
    }
}
