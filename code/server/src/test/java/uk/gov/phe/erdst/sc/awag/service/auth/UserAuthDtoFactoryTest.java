package uk.gov.phe.erdst.sc.awag.service.auth;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuthPK;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.UserAuthDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class UserAuthDtoFactoryTest
{
    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_PASSWORD = "D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892";

    @Inject
    private UserAuthDtoFactory mUserAuthDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testUserAuthCreateAdminDto()
    {
        UserAuth user = new UserAuth();
        user.setUsername(TEST_USER_NAME);
        user.setPassword(TEST_PASSWORD);
        List<GroupAuth> groups = new ArrayList<GroupAuth>(2);
        GroupAuth adminGroup = new GroupAuth();
        adminGroup.setGroupName(WebSecurityUtils.RolesAllowed.AW_ADMIN);
        GroupAuth assessmentUserGroup = new GroupAuth();
        assessmentUserGroup.setGroupName(WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER);
        groups.add(adminGroup);
        groups.add(assessmentUserGroup);

        List<UserGroupAuth> userGroups = getUserGroups(user, groups);

        user.setUserGroups(userGroups);
        UserAuthDto dto = mUserAuthDtoFactory.create(user);
        assertDtoValues(dto, WebSecurityUtils.RolesAllowed.AW_ADMIN);
    }

    @Test
    private void testUserAuthCreateAssessmentUserDto()
    {
        UserAuth user = new UserAuth();
        user.setUsername(TEST_USER_NAME);
        user.setPassword(TEST_PASSWORD);
        GroupAuth assessmentUserGroup = new GroupAuth();
        assessmentUserGroup.setGroupName(WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER);
        List<GroupAuth> groups = new ArrayList<GroupAuth>(1);
        groups.add(assessmentUserGroup);

        List<UserGroupAuth> userGroups = getUserGroups(user, groups);

        user.setUserGroups(userGroups);
        UserAuthDto dto = mUserAuthDtoFactory.create(user);
        assertDtoValues(dto, WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER);
    }

    private List<UserGroupAuth> getUserGroups(UserAuth user, List<GroupAuth> groups)
    {
        List<UserGroupAuth> userGroups = new ArrayList<UserGroupAuth>(groups.size());
        for (GroupAuth group : groups)
        {
            UserGroupAuthPK userGroupId = new UserGroupAuthPK();
            userGroupId.setGroupName(group.getGroupName());
            userGroupId.setUsername(TEST_USER_NAME);

            UserGroupAuth userGroup = new UserGroupAuth();
            userGroup.setId(userGroupId);
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroups.add(userGroup);
        }
        return userGroups;
    }

    private void assertDtoValues(UserAuthDto dto, String userType)
    {
        Assert.assertEquals(dto.userName, TEST_USER_NAME);
        Assert.assertEquals(dto.userGroup, userType);
    }
}
