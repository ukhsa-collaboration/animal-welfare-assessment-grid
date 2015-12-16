package uk.gov.phe.erdst.sc.awag.dto;

import java.util.Set;

public class AssessmentsDto
{
    public Set<AssessmentDto> assessments;

    public AssessmentsDto()
    {
    }

    public AssessmentsDto(Set<AssessmentDto> assessmentDtos)
    {
        this.assessments = assessmentDtos;
    }
}
