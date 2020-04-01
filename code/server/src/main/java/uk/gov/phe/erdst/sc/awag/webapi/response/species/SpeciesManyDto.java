package uk.gov.phe.erdst.sc.awag.webapi.response.species;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class SpeciesManyDto implements ResponseDto
{
    public Collection<SpeciesDto> species;
    public PagingInfo pagingInfo;
}
