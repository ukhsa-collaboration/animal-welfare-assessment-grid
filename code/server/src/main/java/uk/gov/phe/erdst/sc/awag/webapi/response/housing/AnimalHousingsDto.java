package uk.gov.phe.erdst.sc.awag.webapi.response.housing;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AnimalHousingsDto implements ResponseDto
{
    public Collection<AnimalHousingDto> housings;
    public PagingInfo pagingInfo;
}
