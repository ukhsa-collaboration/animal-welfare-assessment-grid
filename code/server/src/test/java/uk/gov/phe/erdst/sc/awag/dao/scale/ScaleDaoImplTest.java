package uk.gov.phe.erdst.sc.awag.dao.scale;

import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class ScaleDaoImplTest
{
    private static final String SCALES_LIKE_TERM = "1 to";
    private static final int EXPECTED_SCALES_LIKE = 3;
    private static final Long EXPECTED_COUNT_SCALES_LIKE = 3L;
    private static final Long EXISTENT_SCALE_ID = 10001L;
    private static final Long NON_EXISTENT_SCALE_ID = 10004L;
    private ScaleDao mScaleDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mScaleDao = (ScaleDao) GlassfishTestsHelper.lookupMultiInterface("ScaleDaoImpl", ScaleDao.class);
    }

    @Test
    public void testStoreValidScale() throws Exception
    {
        Scale scale = new Scale();
        scale.setId(NON_EXISTENT_SCALE_ID);
        scale.setName("1 to 8");
        scale.setMin(1);
        scale.setMax(8);
        Scale newScale = mScaleDao.store(scale);

        Long scaleId = newScale.getId();
        Assert.assertNotNull(scaleId);

        final int expected = EXPECTED_SCALES_LIKE + 1;
        Collection<Scale> scales = mScaleDao.getEntities(null, null);
        Assert.assertEquals(scales.size(), expected);

        mScaleDao.deleteEntityById(scaleId);
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueScale() throws AWNonUniqueException
    {
        Scale scale = new Scale();
        scale.setId(NON_EXISTENT_SCALE_ID);
        scale.setName("1 to 10");
        scale.setMin(1);
        scale.setMax(10);
        mScaleDao.store(scale);
    }

    @Test
    public void testGetScale() throws AWNoSuchEntityException
    {
        Scale scale = mScaleDao.getEntityById(EXISTENT_SCALE_ID);
        Assert.assertNotNull(scale);
        Assert.assertEquals(scale.getId(), EXISTENT_SCALE_ID);
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentScale() throws AWNoSuchEntityException
    {
        Scale scale = mScaleDao.getEntityById(NON_EXISTENT_SCALE_ID);
    }

    @Test
    public void testGetCountScalesLike()
    {
        Long scalesCount = mScaleDao.getEntityCountLike(SCALES_LIKE_TERM);
        Assert.assertEquals(scalesCount, EXPECTED_COUNT_SCALES_LIKE);
    }

    @Test
    public void testGetScalesLike()
    {
        List<Scale> scales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, null, null);
        Assert.assertEquals(scales.size(), EXPECTED_SCALES_LIKE);
    }

    @Test
    public void testGetScalesLikePagedNoLimit()
    {
        List<Scale> allScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, null, null);
        List<Scale> pagedScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allScales, pagedScales);
    }

    @Test
    public void testGetScalesLikePagedNoOffset()
    {
        List<Scale> allScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, null, null);
        List<Scale> pagedScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allScales, pagedScales);
    }

    @Test
    public void testGetScalesLikeValidOffsetLimit()
    {
        List<Scale> allScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, null, null);
        List<Scale> pagedScales = mScaleDao.getEntitiesLike(SCALES_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allScales, pagedScales);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
