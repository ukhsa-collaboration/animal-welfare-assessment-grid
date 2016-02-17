package uk.gov.phe.erdst.sc.awag.service.auth;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.GroupAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.UserAuthTestUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class GroupAuthFactoryTest
{
    @Inject
    private GroupAuthFactory mGroupAuthFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testCreateUserGroupAuthForAdmin()
    {
        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData(null, null, null,
            ServletSecurityUtils.RolesAllowed.AW_ADMIN);

        List<GroupAuth> groups = mGroupAuthFactory.create(clientData);

        Assert.assertEquals(groups.size(), 2);

        for (GroupAuth group : groups)
        {
            Assert.assertTrue(group.getGroupName().equals(ServletSecurityUtils.RolesAllowed.AW_ADMIN)
                || group.getGroupName().equals(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER));
        }
    }

    @Test
    private void testCreateUserGroupAuthForAssessmentUser()
    {
        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData(null, null, null,
            ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER);

        List<GroupAuth> groups = mGroupAuthFactory.create(clientData);

        Assert.assertEquals(groups.size(), 1);
        Assert.assertTrue(groups.get(0).getGroupName().equals(ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER));
    }

}
