package uk.gov.phe.erdst.sc.awag.service.factory.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.webapi.response.user.UserDto;

@Stateless
public class UserDtoFactory
{
    public UserDto createUserDto(User user)
    {
        return new UserDto(user.getId(), user.getName());
    }

    public Collection<UserDto> createDtos(Collection<User> users)
    {
        Collection<UserDto> dtos = new ArrayList<>(users.size());
        for (User user : users)
        {
            dtos.add(createUserDto(user));
        }
        return dtos;
    }
}
