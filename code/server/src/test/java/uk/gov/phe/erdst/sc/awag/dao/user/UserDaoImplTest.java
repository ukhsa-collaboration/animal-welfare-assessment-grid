package uk.gov.phe.erdst.sc.awag.dao.user;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class UserDaoImplTest
{
    public static final Long USER_1_ID = 10000L;
    public static final Long USER_2_ID = 10001L;
    private static final String INITIAL_USER_1_NAME = "User 1";
    private static final String TEST_USER_1_NAME = "Test User 1";
    private static final String TEST_NON_EXISTENT_USER_NAME = "Non existent user";
    private static final long EXPECTED_NO_OF_INITIAL_USERS = 3;
    private static final Long EXPECTED_NO_OF_USERS_IN_CREATION_TESTS = 4L;
    private static final String LIKE_TERM = "Use";

    private UserDao mUserDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mUserDao = (UserDao) GlassfishTestsHelper.lookupMultiInterface("UserDaoImpl", UserDao.class);
    }

    @Test
    public void testGetUsers()
    {
        Assert.assertEquals(mUserDao.getEntities(null, null).size(), EXPECTED_NO_OF_INITIAL_USERS);
    }

    @Test
    public void testStoreUser() throws AWNonUniqueException, AWNoSuchEntityException
    {
        User user = createUser(TEST_USER_1_NAME);
        mUserDao.store(user);

        Assert.assertEquals(mUserDao.getEntityCount(), EXPECTED_NO_OF_USERS_IN_CREATION_TESTS);
        Assert.assertNotEquals(user.getId(), TestConstants.NON_PERSISTED_ID);
        Assert.assertEquals(user.getName(), TEST_USER_1_NAME);

        mUserDao.deleteEntityByNameField(user.getName());
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreDuplicateUser() throws AWNonUniqueException, AWNoSuchEntityException
    {
        User user = createUser(INITIAL_USER_1_NAME);
        mUserDao.store(user);
    }

    @Test
    public void testRemoveUser() throws AWNoSuchEntityException, AWNonUniqueException
    {
        User user = createUser(TEST_USER_1_NAME);
        mUserDao.store(user);
        mUserDao.deleteEntityByNameField(user.getName());

        Assert.assertEquals(mUserDao.getEntities(null, null).size(), EXPECTED_NO_OF_INITIAL_USERS);
    }

    @Test
    public void testGetUser() throws AWNoSuchEntityException
    {
        try
        {
            User user = mUserDao.getEntityByNameField(INITIAL_USER_1_NAME);
            Assert.assertEquals(user.getName(), INITIAL_USER_1_NAME);
            Assert.assertNotEquals(user.getId(), TestConstants.NON_PERSISTED_ID);
        }
        catch (AWNoSuchEntityException ex)
        {
            Assert.fail();
        }
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentUser() throws AWNoSuchEntityException
    {
        mUserDao.getEntityByNameField(TEST_NON_EXISTENT_USER_NAME);
    }

    @Test
    public void testGetUsersLike()
    {
        List<User> users = mUserDao.getEntitiesLike(LIKE_TERM, null, null);
        Assert.assertEquals(users.size(), EXPECTED_NO_OF_INITIAL_USERS);
    }

    @Test
    public void testGetUsersLikeCount()
    {
        long count = mUserDao.getEntityCountLike(LIKE_TERM);
        Assert.assertEquals(count, EXPECTED_NO_OF_INITIAL_USERS);
    }

    @Test
    public void testGetUsersLikeValidOffsetLimit()
    {
        testGetUsersLike(TestConstants.TEST_OFFSET, TestConstants.TEST_LIMIT);
    }

    @Test
    public void testGetUsersLikePagedNoOffest()
    {
        testGetUsersLike(TestConstants.TEST_NO_OFFSET, TestConstants.TEST_LIMIT);
    }

    @Test
    public void testGetUsersLikePagedNoLimit()
    {
        testGetUsersLike(TestConstants.TEST_OFFSET, TestConstants.TEST_NO_LIMIT);
    }

    private void testGetUsersLike(Integer offset, Integer limit)
    {
        List<User> allUsers = mUserDao.getEntitiesLike(LIKE_TERM, null, null);
        List<User> pagedUsers = mUserDao.getEntitiesLike(LIKE_TERM, offset, limit);

        if (offset == null)
        {
            PageTestAsserter.assertPagedNoOffset(allUsers, pagedUsers);
        }
        else if (limit == null)
        {
            PageTestAsserter.assertPagedNoLimit(allUsers, pagedUsers);
        }
        else
        {
            PageTestAsserter.assertPagedValidOffsetLimit(allUsers, pagedUsers);
        }
    }

    private static User createUser(String name)
    {
        User user = new User();
        user.setName(name);

        return user;
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
