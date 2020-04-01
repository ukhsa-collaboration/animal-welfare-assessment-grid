package uk.gov.phe.erdst.sc.awag.webapi.response.reason;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AssessmentReasonsDto implements ResponseDto
{
    public Collection<AssessmentReasonDto> reasons;
    public PagingInfo pagingInfo;
}
