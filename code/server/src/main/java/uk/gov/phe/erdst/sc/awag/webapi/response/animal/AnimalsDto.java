package uk.gov.phe.erdst.sc.awag.webapi.response.animal;

import java.util.List;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AnimalsDto implements ResponseDto
{
    public List<AnimalDto> animals;
    public PagingInfo pagingInfo;
}
