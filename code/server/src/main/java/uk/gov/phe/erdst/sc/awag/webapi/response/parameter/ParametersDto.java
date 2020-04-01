package uk.gov.phe.erdst.sc.awag.webapi.response.parameter;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class ParametersDto implements ResponseDto
{
    public Collection<ParameterDto> parameters;
    public PagingInfo pagingInfo;
}
