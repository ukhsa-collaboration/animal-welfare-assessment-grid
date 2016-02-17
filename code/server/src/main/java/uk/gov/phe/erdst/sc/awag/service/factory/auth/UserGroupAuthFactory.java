package uk.gov.phe.erdst.sc.awag.service.factory.auth;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuthPK;

@Stateless
public class UserGroupAuthFactory
{
    public UserGroupAuthFactory()
    {
    }

    public List<UserGroupAuth> create(UserAuth user, List<GroupAuth> groups)
    {
        List<UserGroupAuth> userGroups = new ArrayList<UserGroupAuth>(groups.size());
        for (GroupAuth group : groups)
        {
            UserGroupAuthPK userGroupPk = new UserGroupAuthPK();
            userGroupPk.setUsername(user.getUsername());
            userGroupPk.setGroupName(group.getGroupName());
            UserGroupAuth userGroup = new UserGroupAuth();
            userGroup.setId(userGroupPk);
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroups.add(userGroup);
        }
        return userGroups;
    }
}
