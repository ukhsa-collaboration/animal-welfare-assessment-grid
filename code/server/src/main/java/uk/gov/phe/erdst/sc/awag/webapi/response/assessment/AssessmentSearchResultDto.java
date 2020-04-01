package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;

public class AssessmentSearchResultDto
{
    public Long assessmentId;
    public String assessmentDate;
    public String assessmentReason;
    public boolean isComplete;
    public String performedBy;
    public AnimalDto animal;
    public boolean isAllowZeroScores;
}
