package uk.gov.phe.erdst.sc.awag.webapi.response.factor;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class FactorsDto implements ResponseDto
{
    public Collection<FactorDto> factors;
    public PagingInfo pagingInfo;
}
