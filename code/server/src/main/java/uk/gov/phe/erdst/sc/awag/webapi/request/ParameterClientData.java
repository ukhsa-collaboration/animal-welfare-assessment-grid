package uk.gov.phe.erdst.sc.awag.webapi.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class ParameterClientData
{
    @ValidId(message = ValidationConstants.PARAMETER_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.PARAMETER_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long parameterId;

    @NotNull(message = ValidationConstants.PARAMETER_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.PARAMETER_NAME_SIZE_MIN,
        max = ValidationConstants.PARAMETER_NAME_SIZE_MAX, message = ValidationConstants.PARAMETER_NAME_PROPERTY + "|"
            + ValidationConstants.PARAMETER_NAME_SIZE_MIN + "|" + ValidationConstants.PARAMETER_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.PARAMETER_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String parameterName;

    @Valid
    public Set<FactorClientData> parameterFactors = new HashSet<FactorClientData>();

    public ParameterClientData(Long parameterId, String parameterName)
    {
        this.parameterId = parameterId;
        this.parameterName = parameterName;
    };

    public ParameterClientData()
    {
    }

    public void setParameterFactors(Set<FactorClientData> parameterFactors)
    {
        this.parameterFactors = parameterFactors;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parameterId == null) ? 0 : parameterId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParameterClientData other = (ParameterClientData) obj;
        if (parameterId == null)
        {
            if (other.parameterId != null)
                return false;
        }
        else if (!parameterId.equals(other.parameterId))
            return false;
        return true;
    }
}
