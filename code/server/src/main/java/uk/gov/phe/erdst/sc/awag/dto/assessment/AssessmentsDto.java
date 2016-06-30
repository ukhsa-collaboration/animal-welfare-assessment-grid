package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.Set;

public class AssessmentsDto
{
    // CS:OFF: VisibilityModifier
    public Set<AssessmentDto> assessments;

    // CS:ON

    public AssessmentsDto()
    {
    }

    public AssessmentsDto(Set<AssessmentDto> assessmentDtos)
    {
        this.assessments = assessmentDtos;
    }
}
