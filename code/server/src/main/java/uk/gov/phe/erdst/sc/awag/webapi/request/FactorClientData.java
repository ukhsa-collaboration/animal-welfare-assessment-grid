package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class FactorClientData
{
    @ValidId(message = ValidationConstants.FACTOR_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.FACTOR_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long factorId;

    @NotNull(message = ValidationConstants.FACTOR_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.FACTOR_NAME_SIZE_MIN, max = ValidationConstants.FACTOR_NAME_SIZE_MAX,
        message = ValidationConstants.FACTOR_NAME_PROPERTY + "|" + ValidationConstants.FACTOR_NAME_SIZE_MIN + "|"
            + ValidationConstants.FACTOR_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.FACTOR_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String factorName;

    // TODO validate
    public String factorDescription;

    public FactorClientData(Long factorId, String factorName, String factorDescription)
    {
        this.factorId = factorId;
        this.factorName = factorName;
        this.factorDescription = factorDescription;
    }

    public FactorClientData()
    {
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((factorId == null) ? 0 : factorId.hashCode());
        result = prime * result + ((factorName == null) ? 0 : factorName.hashCode());
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
        FactorClientData other = (FactorClientData) obj;
        if (factorId == null)
        {
            if (other.factorId != null)
            {
                return false;
            }
        }
        else if (!factorId.equals(other.factorId))
        {
            return false;
        }
        if (factorName == null)
        {
            if (other.factorName != null)
            {
                return false;
            }
        }
        else if (!factorName.equals(other.factorName))
        {
            return false;
        }
        return true;
    }
}
