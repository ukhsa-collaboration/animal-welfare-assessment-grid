package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.AnimalHousingDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentReasonDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.dto.StudySimpleDto;
import uk.gov.phe.erdst.sc.awag.dto.UserDto;

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
