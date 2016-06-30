package uk.gov.phe.erdst.sc.awag.dao.reason;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentReasonDaoImplTest
{
    public static final Long REASON_1_ID = 10000L;
    public static final Long REASON_2_ID = 10001L;
    private static final String REASON_1_NAME = "Reason 1";
    private static final String REASON_1_NEW_NAME = "Test Reason 1";
    private static final Long NON_EXISTENT_REASON_ID = 10004L;
    private static final String NON_EXISTENT_REASON_NAME = "Non existent reason";
    private static final String REASONS_LIKE_TERM = "Rea";
    private static final long EXPECTED_REASONS_LIKE = 3L;
    private static final long EXPECTED_ALL_REASONS = 3;

    private AssessmentReasonDao mAssessmentReasonDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentReasonDao = (AssessmentReasonDao) GlassfishTestsHelper.lookupMultiInterface(
            "AssessmentReasonDaoImpl", AssessmentReasonDao.class);
    }

    @Test
    public void testStoreReason() throws AWNonUniqueException, AWNoSuchEntityException
    {
        AssessmentReason reason = createReason(NON_EXISTENT_REASON_NAME);

        mAssessmentReasonDao.store(reason);

        Assert.assertNotEquals(reason.getId(), TestConstants.NON_PERSISTED_ID);
        Assert.assertEquals(reason.getName(), NON_EXISTENT_REASON_NAME);

        mAssessmentReasonDao.deleteEntityById(reason.getId());
    }

    @Test
    public void testUpdateReason() throws AWNoSuchEntityException, AWNonUniqueException
    {
        AssessmentReason reason = mAssessmentReasonDao.getEntityById(REASON_1_ID);
        reason.setName(REASON_1_NEW_NAME);
        mAssessmentReasonDao.update(reason);

        AssessmentReason updatedReason = mAssessmentReasonDao.getEntityById(REASON_1_ID);
        Assert.assertEquals(updatedReason.getName(), REASON_1_NEW_NAME);

        updatedReason.setName(REASON_1_NAME);
        mAssessmentReasonDao.update(updatedReason);
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueReason() throws AWNonUniqueException, AWNoSuchEntityException
    {
        AssessmentReason reason = createReason(REASON_1_NAME);
        mAssessmentReasonDao.store(reason);
    }

    @Test
    public void testGetReasons()
    {
        Assert.assertEquals(mAssessmentReasonDao.getEntities(null, null).size(), EXPECTED_ALL_REASONS);
    }

    @Test
    public void testGetReason() throws Exception
    {
        AssessmentReason reason = mAssessmentReasonDao.getEntityByNameField(REASON_1_NAME);

        Assert.assertNotNull(reason);
        Assert.assertEquals(reason.getName(), REASON_1_NAME);
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentReason() throws AWNoSuchEntityException
    {
        mAssessmentReasonDao.getEntityById(NON_EXISTENT_REASON_ID);
    }

    @Test
    public void testGetReasonsLike()
    {
        List<AssessmentReason> assessmentReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM, null, null);
        Assert.assertEquals(assessmentReasons.size(), EXPECTED_REASONS_LIKE);
    }

    @Test
    public void testGetReasonsLikeCount()
    {
        Long reasonsLikeCount = mAssessmentReasonDao.getEntityCountLike(REASONS_LIKE_TERM);
        Assert.assertEquals(reasonsLikeCount, Long.valueOf(EXPECTED_REASONS_LIKE));
    }

    @Test
    public void testGetReasonsLikeValidOffsetLimit()
    {
        List<AssessmentReason> allReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM, null, null);
        List<AssessmentReason> pagedReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM,
            TestConstants.TEST_OFFSET, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allReasons, pagedReasons);
    }

    @Test
    public void testGetReasonsLikePagedNoOffest()
    {
        List<AssessmentReason> allReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM, null, null);

        List<AssessmentReason> pagedReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM, null,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allReasons, pagedReasons);
    }

    @Test
    public void testGetReasonsLikePagedNoLimit()
    {
        List<AssessmentReason> allReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM, null, null);
        List<AssessmentReason> pagedReasons = mAssessmentReasonDao.getEntitiesLike(REASONS_LIKE_TERM,
            TestConstants.TEST_OFFSET, null);
        PageTestAsserter.assertPagedNoLimit(allReasons, pagedReasons);
    }

    private static AssessmentReason createReason(String name)
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setName(name);
        return reason;
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
