package uk.gov.phe.erdst.sc.awag.service.factory.auth;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.dto.UserAuthDto;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;

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
                if (userGroup.getGroup().equals(new GroupAuth(ServletSecurityUtils.RolesAllowed.AW_ADMIN)))
                {
                    userDto.userGroup = ServletSecurityUtils.RolesAllowed.AW_ADMIN;
                    break;
                }

                if (userGroup.getGroup().equals(new GroupAuth(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER)))
                {
                    userDto.userGroup = ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER;
                }
            }
        }
        return userDto;
    }
}
