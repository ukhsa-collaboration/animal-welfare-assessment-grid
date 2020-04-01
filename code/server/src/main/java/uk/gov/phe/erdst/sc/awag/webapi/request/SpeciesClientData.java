package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class SpeciesClientData
{
    @ValidId(message = ValidationConstants.SPECIES_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.SPECIES_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long speciesId;

    @NotNull(message = ValidationConstants.SPECIES_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.SPECIES_NAME_SIZE_MIN, max = ValidationConstants.SPECIES_NAME_SIZE_MAX,
        message = ValidationConstants.SPECIES_NAME_PROPERTY + "|" + ValidationConstants.SPECIES_NAME_SIZE_MIN + "|"
            + ValidationConstants.SPECIES_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.SPECIES_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String speciesName;

    public SpeciesClientData()
    {
    }

    public SpeciesClientData(Long speciesId, String speciesName)
    {
        this.speciesId = speciesId;
        this.speciesName = speciesName;
    }
}
