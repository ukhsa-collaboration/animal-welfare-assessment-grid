package uk.gov.phe.erdst.sc.awag.dao.auth;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.UserAuthDao;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class UserAuthDaoImplTest
{
    private UserAuthDao mUserAuthDao;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mUserAuthDao = (UserAuthDao) GlassfishTestsHelper.lookupMultiInterface("UserAuthDaoImpl", UserAuthDao.class);
    }

    @Test
    public void testStoreUserAuth() throws AWNonUniqueException, AWNoSuchEntityException
    {
        String newUsername = "newuser";
        String newPassword = "D82494F05D6917BA02F7AAA29689CCB444BB73F20380876CB05D1F37537B7892";
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(newUsername);
        userAuth.setPassword(newPassword);
        mUserAuthDao.store(userAuth);

        UserAuth storedUser = mUserAuthDao.getEntityById(newUsername);

        Assert.assertNotNull(storedUser);
        Assert.assertEquals(storedUser.getUsername(), newUsername);
        Assert.assertEquals(storedUser.getPassword(), newPassword);

        mUserAuthDao.deleteEntityById(storedUser.getUsername());
    }

    @Test
    private void testGetUserAuthLike()
    {
        List<UserAuth> users = mUserAuthDao.getEntitiesLike("adm", null, null);
        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 1);
        Assert.assertEquals(users.get(0).getUsername(), "admin");
    }

    @Test
    private void testGetUserAuth() throws AWNoSuchEntityException
    {
        String adminUserName = "admin";
        UserAuth user = mUserAuthDao.getEntityById(adminUserName);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), adminUserName);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
