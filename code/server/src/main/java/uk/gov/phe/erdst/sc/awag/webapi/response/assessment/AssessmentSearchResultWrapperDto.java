package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AssessmentSearchResultWrapperDto implements ResponseDto
{
    public Set<AssessmentSearchResultDto> assessments;
    public PagingInfo pagingInfo;

    public AssessmentSearchResultWrapperDto(Set<AssessmentSearchResultDto> assessmentDtos)
    {
        this.assessments = assessmentDtos;
    }
}
