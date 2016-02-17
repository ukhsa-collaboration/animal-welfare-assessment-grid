package uk.gov.phe.erdst.sc.awag.service.auth;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.UserAuthTestUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class UserAuthFactoryTest
{
    private static final String PASSWORD_TO_HASH = "adminadmin";
    private static final String UPDATED_PASSWORD_TO_HASH = "adminadmin2";
    private static final String EXPECTED_PASSWORD_HASH = "D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892";
    private static final String EXPECTED_UPDATED_PASSWORD_HASH = "A6E8639A2E40E9F9B3032B9A4615BF98CD217AB61E332DDFF7DBDD32BA25F67D";

    @Inject
    private UserAuthFactory mUserAuthFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testCreateUserAuth()
    {
        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData(ServletSecurityUtils.AW_ADMIN_USER,
            PASSWORD_TO_HASH, PASSWORD_TO_HASH, ServletSecurityUtils.RolesAllowed.AW_ADMIN);

        UserAuth userAuth = mUserAuthFactory.create(clientData);

        Assert.assertEquals(userAuth.getUsername(), clientData.userName);
        Assert.assertEquals(userAuth.getPassword(), EXPECTED_PASSWORD_HASH);
    }

    @Test
    private void testUpdateUserAuth()
    {
        UserAuth user = new UserAuth();
        user.setUsername(ServletSecurityUtils.AW_ADMIN_USER + 2);
        user.setPassword("D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892");

        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData(
            String.valueOf(ServletSecurityUtils.AW_ADMIN_USER + 2), UPDATED_PASSWORD_TO_HASH, UPDATED_PASSWORD_TO_HASH,
            ServletSecurityUtils.RolesAllowed.AW_ADMIN);

        mUserAuthFactory.update(user, clientData);

        Assert.assertEquals(user.getUsername(), clientData.userName);
        Assert.assertEquals(user.getPassword(), EXPECTED_UPDATED_PASSWORD_HASH);
    }

}
