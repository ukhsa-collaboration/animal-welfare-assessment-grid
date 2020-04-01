package uk.gov.phe.erdst.sc.awag.webapi.response.source;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class SourcesDto implements ResponseDto
{
    public Collection<SourceDto> sources;
    public PagingInfo pagingInfo;
}
