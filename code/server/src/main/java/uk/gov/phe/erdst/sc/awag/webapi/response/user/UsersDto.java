package uk.gov.phe.erdst.sc.awag.webapi.response.user;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class UsersDto implements ResponseDto
{
    public Collection<UserDto> users;
    public PagingInfo pagingInfo;
}
