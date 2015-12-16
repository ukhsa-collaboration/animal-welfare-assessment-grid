package uk.gov.phe.erdst.sc.awag.dto;

public class AssessmentReasonDto
{
    public final Long reasonId;
    public final String reasonName;

    public AssessmentReasonDto(Long reasonId, String reasonName)
    {
        this.reasonId = reasonId;
        this.reasonName = reasonName;
    }
}
