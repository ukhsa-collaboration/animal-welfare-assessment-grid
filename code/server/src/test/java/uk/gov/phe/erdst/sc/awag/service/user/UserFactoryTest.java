package uk.gov.phe.erdst.sc.awag.service.user;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class UserFactoryTest
{

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private UserFactory mUserFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate()
    {
        UserClientData clientData = (UserClientData) mRequestConverter.convert(TestConstants.DUMMY_NEW_USER_RAW_DATA,
            UserClientData.class);
        User user = mUserFactory.create(clientData);

        assertNonIdProperties(user, clientData);
    }

    private void assertNonIdProperties(User user, UserClientData clientData)
    {
        Assert.assertEquals(user.getName(), clientData.userName);
    }

}
