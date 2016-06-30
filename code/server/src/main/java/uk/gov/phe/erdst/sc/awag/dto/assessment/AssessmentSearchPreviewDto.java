package uk.gov.phe.erdst.sc.awag.dto.assessment;

import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;

public class AssessmentSearchPreviewDto
{
    public Long assessmentId;
    public String assessmentDate;
    public String assessmentReason;
    public boolean isComplete;
    public String performedBy;
    public AnimalDto animal;

    public AssessmentSearchPreviewDto()
    {
    }
}
