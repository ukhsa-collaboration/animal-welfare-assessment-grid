package uk.gov.phe.erdst.sc.awag.service.factory.auth;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.UserAuthDto;

@Stateless
public class UserAuthDtoFactory
{
    public UserAuthDto create(UserAuth user)
    {
        UserAuthDto userDto = new UserAuthDto();

        if (user != null)
        {
            userDto.userName = user.getUsername();

            for (UserGroupAuth userGroup : user.getUserGroups())
            {
                if (userGroup.getGroup().equals(new GroupAuth(WebSecurityUtils.RolesAllowed.AW_ADMIN)))
                {
                    userDto.userGroup = WebSecurityUtils.RolesAllowed.AW_ADMIN;
                    break;
                }

                if (userGroup.getGroup().equals(new GroupAuth(WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER)))
                {
                    userDto.userGroup = WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER;
                }
            }
        }
        return userDto;
    }

    public Collection<UserAuthDto> createDtos(Collection<UserAuth> userAuths)
    {
        Collection<UserAuthDto> dtos = new ArrayList<>(userAuths.size());
        for (UserAuth userAuth : userAuths)
        {
            dtos.add(create(userAuth));
        }
        return dtos;
    }
}
