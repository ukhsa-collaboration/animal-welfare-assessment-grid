package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class AnimalHousingClientData
{
    @ValidId(message = ValidationConstants.ANIMAL_HOUSING_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.ANIMAL_HOUSING_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long housingId;

    @NotNull(message = ValidationConstants.ANIMAL_HOUSING_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.ANIMAL_HOUSING_NAME_SIZE_MIN,
        max = ValidationConstants.ANIMAL_HOUSING_NAME_SIZE_MAX,
        message = ValidationConstants.ANIMAL_HOUSING_NAME_PROPERTY + "|"
            + ValidationConstants.ANIMAL_HOUSING_NAME_SIZE_MIN + "|" + ValidationConstants.ANIMAL_HOUSING_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.ANIMAL_HOUSING_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String housingName;

    public AnimalHousingClientData(Long housingId, String housingName)
    {
        this.housingId = housingId;
        this.housingName = housingName;
    }
}
