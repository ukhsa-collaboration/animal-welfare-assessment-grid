package uk.gov.phe.erdst.sc.awag.webapi.response.scale;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class ScalesDto implements ResponseDto
{
    public Collection<ScaleDto> scales;
    public PagingInfo pagingInfo;
}
