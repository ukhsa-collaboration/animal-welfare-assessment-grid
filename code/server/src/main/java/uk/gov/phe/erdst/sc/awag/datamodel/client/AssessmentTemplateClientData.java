package uk.gov.phe.erdst.sc.awag.datamodel.client;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class AssessmentTemplateClientData
{
    @ValidId(message = ValidationConstants.TEMPLATE_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.TEMPLATE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long templateId;

    @NotNull(message = ValidationConstants.TEMPLATE_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.TEMPLATE_NAME_SIZE_MIN,
        max = ValidationConstants.TEMPLATE_NAME_SIZE_MAX, message = ValidationConstants.TEMPLATE_NAME_PROPERTY + "|"
            + ValidationConstants.TEMPLATE_NAME_SIZE_MIN + "|" + ValidationConstants.TEMPLATE_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.TEMPLATE_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String templateName;

    @NotNull(message = ValidationConstants.SCALE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.SCALE_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    public Long templateScale;

    @Valid
    public Set<ParameterClientData> templateParameters = new HashSet<ParameterClientData>();

    public AssessmentTemplateClientData()
    {
    }
}
