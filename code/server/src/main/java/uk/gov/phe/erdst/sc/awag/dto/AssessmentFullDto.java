package uk.gov.phe.erdst.sc.awag.dto;

import java.util.Set;

public class AssessmentFullDto
{
    public Long assessmentId;
    public boolean isComplete;
    public String assessmentDate;
    public AnimalDto animal;
    public StudySimpleDto study;
    public AssessmentReasonDto reason;
    public AnimalHousingDto housing;
    public UserDto performedBy;
    public Set<ParameterScoredDto> assessmentParameters;
}
