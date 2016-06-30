package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;

public class AssessmentDto
{
    // CS:OFF: VisibilityModifier
    public Long assessmentId;
    public String assessmentDate;
    public String assessmentReason;
    public AnimalDto animal;
    public Set<ParameterScoredDto> assessmentParameters;

    // CS:ON

    public AssessmentDto()
    {
    }

    public AssessmentDto(Long id)
    {
        this.assessmentId = id;
    }

    // CS:OFF: HiddenField
    public AssessmentDto(Long asseessmentId, String assessmentDate, AnimalDto animal, String assessmentReason,
        Set<ParameterScoredDto> assessmentParameters)
    {
        this.assessmentId = asseessmentId;
        this.assessmentDate = assessmentDate;
        this.animal = animal;
        this.assessmentReason = assessmentReason;
        this.assessmentParameters = assessmentParameters;
    }

    // CS:ON

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
        AssessmentDto other = (AssessmentDto) obj;
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
