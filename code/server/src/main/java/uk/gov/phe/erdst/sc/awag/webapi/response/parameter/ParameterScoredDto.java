package uk.gov.phe.erdst.sc.awag.webapi.response.parameter;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorScoredDto;

public class ParameterScoredDto
{
    public Long parameterId;
    public String parameterName;
    public double parameterAverage;
    public String parameterComment;
    public Set<FactorScoredDto> parameterFactors;

    public ParameterScoredDto()
    {
    }

    public ParameterScoredDto(Long id)
    {
        this.parameterId = id;
    }

    public ParameterScoredDto(Long parameterId, String parameterName, double parameterAverage, String parameterComment,
        Set<FactorScoredDto> parameterFactors)
    {
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.parameterAverage = parameterAverage;
        this.parameterComment = parameterComment;
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
        ParameterScoredDto other = (ParameterScoredDto) obj;
        if (parameterId == null)
        {
            if (other.parameterId != null)
            {
                return false;
            }
        }
        else if (!parameterId.equals(other.parameterId))
        {
            return false;
        }
        return true;
    }

}
