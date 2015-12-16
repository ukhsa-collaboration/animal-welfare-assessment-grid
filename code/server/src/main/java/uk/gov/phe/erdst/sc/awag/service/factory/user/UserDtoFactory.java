package uk.gov.phe.erdst.sc.awag.service.factory.user;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.dto.UserDto;

@Stateless
public class UserDtoFactory
{
    public UserDto createUserDto(User user)
    {
        return new UserDto(user.getId(), user.getName());
    }
}
