package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

public class AssessmentSimpleDto
{
    public Long assessmentId;
    public String assessmentDate;
    public String assessmentReason;
    public AnimalDto animal;
    public Set<ParameterScoredDto> assessmentParameters;

    public AssessmentSimpleDto()
    {
    }

    public AssessmentSimpleDto(Long id)
    {
        this.assessmentId = id;
    }

    public AssessmentSimpleDto(Long asseessmentId, String assessmentDate, AnimalDto animal,
        String assessmentReason, Set<ParameterScoredDto> assessmentParameters)
    {
        this.assessmentId = asseessmentId;
        this.assessmentDate = assessmentDate;
        this.animal = animal;
        this.assessmentReason = assessmentReason;
        this.assessmentParameters = assessmentParameters;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assessmentId == null) ? 0 : assessmentId.hashCode());
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
        AssessmentSimpleDto other = (AssessmentSimpleDto) obj;
        if (assessmentId == null)
        {
            if (other.assessmentId != null)
            {
                return false;
            }
        }
        else if (!assessmentId.equals(other.assessmentId))
        {
            return false;
        }
        return true;
    }
}
