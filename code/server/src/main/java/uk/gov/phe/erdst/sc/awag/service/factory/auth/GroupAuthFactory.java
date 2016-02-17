package uk.gov.phe.erdst.sc.awag.service.factory.auth;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;

@Stateless
public class GroupAuthFactory
{
    public GroupAuthFactory()
    {
    }

    public List<GroupAuth> create(UserAuthClientData clientData)
    {
        List<GroupAuth> groups = new ArrayList<GroupAuth>();
        if (clientData != null)
        {
            String lowerGroupName = clientData.groupName.toLowerCase();

            if (lowerGroupName.equals(ServletSecurityUtils.RolesAllowed.AW_ADMIN))
            {
                groups.add(getGroup(ServletSecurityUtils.RolesAllowed.AW_ADMIN));
                groups.add(getGroup(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER));
            }

            if (lowerGroupName.equals(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER))
            {
                groups.add(getGroup(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER));
            }
        }
        return groups;
    }

    private GroupAuth getGroup(String groupName)
    {
        GroupAuth group = new GroupAuth();
        group.setGroupName(groupName);
        return group;
    }
}
