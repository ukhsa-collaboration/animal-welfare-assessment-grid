package uk.gov.phe.erdst.sc.awag.dto;

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
