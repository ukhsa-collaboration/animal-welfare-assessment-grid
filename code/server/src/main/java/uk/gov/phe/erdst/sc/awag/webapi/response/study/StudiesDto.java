package uk.gov.phe.erdst.sc.awag.webapi.response.study;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class StudiesDto implements ResponseDto
{
    public Collection<StudyDto> studies;
    public PagingInfo pagingInfo;
}
