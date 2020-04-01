package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AssessmentSimpleWrapperDto implements ResponseDto
{
    public Set<AssessmentSimpleDto> assessments;
    public PagingInfo pagingInfo;

    public AssessmentSimpleWrapperDto()
    {
    }

    public AssessmentSimpleWrapperDto(Set<AssessmentSimpleDto> assessmentDtos)
    {
        this.assessments = assessmentDtos;
    }
}
