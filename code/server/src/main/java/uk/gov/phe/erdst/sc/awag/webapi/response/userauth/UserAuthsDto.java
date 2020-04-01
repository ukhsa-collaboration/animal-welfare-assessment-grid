package uk.gov.phe.erdst.sc.awag.webapi.response.userauth;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class UserAuthsDto implements ResponseDto
{
    public Collection<UserAuthDto> userAuths;
    public PagingInfo pagingInfo;
}
