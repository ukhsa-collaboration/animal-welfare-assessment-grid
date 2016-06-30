package uk.gov.phe.erdst.sc.awag.dao.factor;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.FactorDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class FactorDaoImplTest
{
    private static final Long FACTOR_1_ID = 10000L;
    private static final String FACTOR_1_NAME = "Factor 1";
    private static final String FACTOR_1_NEW_NAME = "Factor 1 (new)";
    private static final Long NON_EXISTENT_FACTOR_ID = 10004L;
    private static final String NON_EXISTENT_FACTOR_NAME = "Non existent factor";
    private static final String FACTORS_LIKE_TERM = "Factor";
    private static final long EXPECTED_FACTORS_LIKE = 4L;
    private static final long EXPECTED_ALL_FACTORS = 4;

    private FactorDao mFactorDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mFactorDao = (FactorDao) GlassfishTestsHelper.lookupMultiInterface("FactorDaoImpl", FactorDao.class);
    }

    @Test
    public void testStoreFactor() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Factor factor = createFactor(NON_EXISTENT_FACTOR_NAME);

        mFactorDao.store(factor);

        Assert.assertNotEquals(factor.getId(), TestConstants.NON_PERSISTED_ID);
        Assert.assertEquals(factor.getName(), NON_EXISTENT_FACTOR_NAME);

        mFactorDao.deleteEntityById(factor.getId());
    }

    @Test
    public void testUpdateFactor() throws AWNoSuchEntityException, AWNonUniqueException
    {
        Factor factor = createFactor(FACTOR_1_NEW_NAME);
        factor.setId(FACTOR_1_ID);

        mFactorDao.update(factor);

        Factor updatedFactor = mFactorDao.getEntityById(FACTOR_1_ID);
        Assert.assertEquals(updatedFactor.getName(), FACTOR_1_NEW_NAME);

        factor.setName(FACTOR_1_NAME);
        mFactorDao.update(factor);
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueFactor() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Factor factor = createFactor(FACTOR_1_NAME);
        mFactorDao.store(factor);
    }

    @Test
    public void testGetFactors()
    {
        Assert.assertEquals(mFactorDao.getEntities(null, null).size(), EXPECTED_ALL_FACTORS);
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentFactor() throws AWNoSuchEntityException
    {
        mFactorDao.getEntityById(NON_EXISTENT_FACTOR_ID);
    }

    @Test
    public void testGetFactorsLike()
    {
        List<Factor> assessmentFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, null, null);
        Assert.assertEquals(assessmentFactors.size(), EXPECTED_FACTORS_LIKE);
    }

    @Test
    public void testGetFactorsLikeCount()
    {
        Long factorsLikeCount = mFactorDao.getEntityCountLike(FACTORS_LIKE_TERM);
        Assert.assertEquals(factorsLikeCount, Long.valueOf(EXPECTED_FACTORS_LIKE));
    }

    @Test
    public void testGetFactorsLikeValidOffsetLimit()
    {
        List<Factor> allFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, null, null);
        List<Factor> pagedFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allFactors, pagedFactors);
    }

    @Test
    public void testGetFactorsLikePagedNoOffest()
    {
        List<Factor> allFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, null, null);

        List<Factor> pagedFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allFactors, pagedFactors);
    }

    @Test
    public void testGetFactorsLikePagedNoLimit()
    {
        List<Factor> allFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, null, null);
        List<Factor> pagedFactors = mFactorDao.getEntitiesLike(FACTORS_LIKE_TERM, TestConstants.TEST_OFFSET, null);
        PageTestAsserter.assertPagedNoLimit(allFactors, pagedFactors);
    }

    private static Factor createFactor(String name)
    {
        Factor factor = new Factor();
        factor.setName(name);
        return factor;
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
