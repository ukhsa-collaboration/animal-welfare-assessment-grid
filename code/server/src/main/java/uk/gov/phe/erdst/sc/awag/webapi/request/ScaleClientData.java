package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidScaleMinMax;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidScaleMinMax
public class ScaleClientData
{
    @ValidId(message = ValidationConstants.SCALE_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.SCALE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long scaleId;

    @NotNull(message = ValidationConstants.SCALE_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.SCALE_NAME_SIZE_MIN,
        max = ValidationConstants.SCALE_NAME_SIZE_MAX, message = ValidationConstants.SCALE_NAME_PROPERTY + "|"
            + ValidationConstants.SCALE_NAME_SIZE_MIN + "|" + ValidationConstants.SCALE_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.SCALE_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String scaleName;

    @NotNull(message = ValidationConstants.SCALE_MIN_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = 0, message = ValidationConstants.SCALE_MIN_PROPERTY,
        payload = ValidationConstants.NonNegativeInteger.class)
    public Integer scaleMin;

    @NotNull(message = ValidationConstants.SCALE_MAX_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = 0, message = ValidationConstants.SCALE_MAX_PROPERTY,
        payload = ValidationConstants.NonNegativeInteger.class)
    public Integer scaleMax;

    public ScaleClientData(Long scaleId, String scaleName, Integer scaleMin, Integer scaleMax)
    {
        this.scaleId = scaleId;
        this.scaleName = scaleName;
        this.scaleMin = scaleMin;
        this.scaleMax = scaleMax;
    }

    public ScaleClientData()
    {
    }
}
