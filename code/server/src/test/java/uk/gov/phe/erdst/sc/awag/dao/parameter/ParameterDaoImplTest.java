package uk.gov.phe.erdst.sc.awag.dao.parameter;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class ParameterDaoImplTest
{
    private static final Long EXPECTED_PARAMETER_1_ID = 10000L;
    private static final Long EXPECTED_PARAMETER_2_ID = 10001L;
    private static final long EXPECTED_NO_OF_PARAMETERS_RETRIEVED = 3;

    private ParameterDao mParameterDao;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mParameterDao = (ParameterDao) GlassfishTestsHelper.lookupMultiInterface("ParameterDaoImpl",
            ParameterDao.class);
    }

    @Test
    public void testGetParameterById() throws AWNoSuchEntityException
    {
        Assert.assertNotNull(mParameterDao.getEntityById(EXPECTED_PARAMETER_1_ID));
    }

    @Test
    public void testGetParameterByIds() throws Exception
    {
        Long[] ids = new Long[] {EXPECTED_PARAMETER_1_ID, EXPECTED_PARAMETER_2_ID};

        Assert.assertTrue(mParameterDao.getEntitiesByIds(ids).size() == ids.length);
    }

    @Test
    public void testGetAllParameters() throws Exception
    {
        Collection<Parameter> parameters = mParameterDao.getEntities(null, null);
        Assert.assertTrue(parameters.size() == EXPECTED_NO_OF_PARAMETERS_RETRIEVED);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
