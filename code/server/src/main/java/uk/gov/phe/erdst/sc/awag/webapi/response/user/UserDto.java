package uk.gov.phe.erdst.sc.awag.webapi.response.user;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class UserDto implements ResponseDto
{
    public final Long userId;
    public final String userName;

    public UserDto(Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }

}
