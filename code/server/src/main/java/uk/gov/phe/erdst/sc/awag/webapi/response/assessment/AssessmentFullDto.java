package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.housing.AnimalHousingDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.reason.AssessmentReasonDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudySimpleDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.user.UserDto;

public class AssessmentFullDto implements ResponseDto
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
    public boolean isAllowZeroScores;
}
