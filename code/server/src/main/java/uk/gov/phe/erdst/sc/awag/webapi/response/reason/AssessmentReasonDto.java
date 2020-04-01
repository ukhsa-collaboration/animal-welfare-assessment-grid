package uk.gov.phe.erdst.sc.awag.webapi.response.reason;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class AssessmentReasonDto implements ResponseDto
{
    public final Long reasonId;
    public final String reasonName;

    public AssessmentReasonDto(Long reasonId, String reasonName)
    {
        this.reasonId = reasonId;
        this.reasonName = reasonName;
    }
}
