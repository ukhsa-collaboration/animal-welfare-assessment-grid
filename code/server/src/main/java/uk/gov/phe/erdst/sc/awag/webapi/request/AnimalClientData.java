package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAnimal;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidDate;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAnimal
public class AnimalClientData
{
    @NotNull(message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @ValidId(message = ValidationConstants.ANIMAL_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    public Long id;

    @NotNull(message = ValidationConstants.ANIMAL_NUMBER_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.ANIMAL_NO_SIZE_MIN, max = ValidationConstants.ANIMAL_NO_SIZE_MAX,
        message = ValidationConstants.ANIMAL_NUMBER_PROPERTY + "|" + ValidationConstants.ANIMAL_NO_SIZE_MIN + "|"
            + ValidationConstants.ANIMAL_NO_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.ANIMAL_NUMBER_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String number;

    @ValidDate(message = ValidationConstants.DOB, payload = ValidationConstants.SimpleDateFormatTmpl.class)
    public String dob;

    @NotNull(message = ValidationConstants.SEX_ENTITY_NAME, payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.SEX_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    public Long sex;

    @NotNull(message = ValidationConstants.SPECIES_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.SPECIES_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    public Long species;

    @NotNull(message = ValidationConstants.SOURCE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.SPECIES_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    public Long source;

    @NotNull(message = ValidationConstants.ASSESSMENT_TEMPLATE_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.ASSESSMENT_TEMPLATE_ENTITY_NAME,
        payload = ValidationConstants.EntityMinId.class)
    public Long assessmentTemplate;

    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.DAM,
        payload = ValidationConstants.EntityMinId.class)
    public Long dam;

    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.FATHER,
        payload = ValidationConstants.EntityMinId.class)
    public Long father;

    @NotNull(message = ValidationConstants.ANIMAL_IS_ALIVE_NULL_ERR)
    public Boolean isAlive;

    public Boolean isAssessed;

    public AnimalClientData()
    {
    }

    public AnimalClientData(Long id)
    {
        this.id = id;
    }

    public AnimalClientData(Long id, String animalNumber)
    {
        this.id = id;
        this.number = animalNumber;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        AnimalClientData other = (AnimalClientData) obj;
        if (id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!id.equals(other.id))
        {
            return false;
        }
        return true;
    }
}
