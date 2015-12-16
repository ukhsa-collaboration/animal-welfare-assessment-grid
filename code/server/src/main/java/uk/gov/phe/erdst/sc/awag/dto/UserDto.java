package uk.gov.phe.erdst.sc.awag.dto;

public class UserDto
{
    public final Long userId;
    public final String userName;

    public UserDto(Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }

}
