package uk.gov.phe.erdst.sc.awag.dao.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.animal.AnimalDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.reason.AssessmentReasonDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.study.StudyDaoImplTest;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentProvider;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.AssessmentBasedTestsUtils;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentDaoImplTest
{
    private static final String DATE_C = "2014-08-15T00:00:00.000Z";
    private static final String DATE_B = "2014-08-14T00:00:00.000Z";
    private static final String DATE_A = "2014-08-13T00:00:00.000Z";
    private static final int ASSESSMENTS_IN_GET_PREV_ASSESSMENT_TEST = 3;
    private static final int EXPECTED_NO_OF_ASSESSMENTS_IN_CREATION_TESTS = 1;

    private AssessmentProvider mAssessmentProvider;
    private AssessmentDao mAssessmentDao;
    private AssessmentTemplateDao mAssessmentTemplateDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentProvider = (AssessmentProvider) GlassfishTestsHelper.lookup("AssessmentProvider");

        mAssessmentDao = (AssessmentDao) GlassfishTestsHelper.lookupMultiInterface("AssessmentDaoImpl",
            AssessmentDao.class);

        mAssessmentTemplateDao = (AssessmentTemplateDao) GlassfishTestsHelper.lookupMultiInterface(
            "AssessmentTemplateDaoImpl", AssessmentTemplateDao.class);
    }

    @AfterMethod
    public void tearDownMethod()
    {
        AssessmentBasedTestsUtils.deleteAllAssessments(mAssessmentDao);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    // private void deleteAssessments(Collection<Assessment> assessments)
    // {
    // for (Assessment a : assessments)
    // {
    // mAssessmentDao.deleteAssessment(a);
    // }
    // }

    @Test
    public void testStoreAssessment() throws Exception
    {
        Assessment assessment = createDefaultAssessment();
        mAssessmentDao.store(assessment);

        Collection<Assessment> assessments = mAssessmentDao.getAssessments(null, null);
        Assert.assertEquals(assessments.size(), EXPECTED_NO_OF_ASSESSMENTS_IN_CREATION_TESTS);
    }

    @Test
    public void testUpdateAssessment() throws Exception
    {
        String initialDate = DATE_A;
        String changedDate = DATE_C;

        Assessment assessment = createDefaultAssessment();

        assessment.setDate(initialDate);
        mAssessmentDao.store(assessment);

        assessment.setDate(changedDate);
        mAssessmentDao.update(assessment);

        Assert.assertNotEquals(initialDate, assessment.getDate());
    }

    @Test
    public void testDeleteAssessment() throws Exception
    {
        Assessment assessment = createDefaultAssessment();
        mAssessmentDao.store(assessment);

        mAssessmentDao.deleteAssessment(mAssessmentDao.getAssessment(assessment.getId()));

        Collection<Assessment> assessments = mAssessmentDao.getAssessments(null, null);
        Assert.assertEquals(assessments.size(), 0);
    }

    @Test
    public void testGetPreviousAssessment() throws Exception
    {
        Assessment prevAssessment = mAssessmentDao.getPreviousAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);

        Assert.assertNull(prevAssessment);

        for (int i = 0; i < ASSESSMENTS_IN_GET_PREV_ASSESSMENT_TEST; i++)
        {
            Assessment assessment = createDefaultAssessment();
            mAssessmentDao.store(assessment);
        }
        Collection<Assessment> assessments = mAssessmentDao.getAssessments(null, null);
        Assert.assertEquals(assessments.size(), ASSESSMENTS_IN_GET_PREV_ASSESSMENT_TEST);

        List<Long> ids = new ArrayList<Long>(ASSESSMENTS_IN_GET_PREV_ASSESSMENT_TEST);
        for (Assessment a : assessments)
        {
            ids.add(a.getId());
        }
        Collections.sort(ids);
        Long expectedId = ids.get(ids.size() - 1);

        prevAssessment = mAssessmentDao.getPreviousAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);

        Assert.assertNotNull(prevAssessment);
        Assert.assertEquals(prevAssessment.getId(), expectedId);
    }

    @Test
    public void testGetPreviouseAssessmentByDate() throws Exception
    {
        final Long animalId = AnimalDaoImplTest.INITIAL_ANIMAL_1_ID;
        Assessment prevAssessment = mAssessmentDao.getPreviousAssessment(animalId);
        String dateA = "2013-02-01T00:00:00.000Z";
        String dateB = "2013-02-02T00:00:00.000Z";
        String dateC = "2013-02-03T00:00:00.000Z";
        String[] dates = {dateA, dateB, dateC};

        Map<String, Long> ids = new HashMap<>();

        Assert.assertNull(prevAssessment);

        for (int i = 0; i < ASSESSMENTS_IN_GET_PREV_ASSESSMENT_TEST; i++)
        {
            String date = dates[i];
            Assessment assessment = createDefaultAssessment();
            assessment.setDate(date);

            mAssessmentDao.store(assessment);

            ids.put(date, assessment.getId());
        }

        Assert.assertNull(mAssessmentDao.getPreviousAssessmentByDate(animalId, dateA, ids.get(dateA)));

        Assessment assessment = mAssessmentDao.getPreviousAssessmentByDate(animalId, dateC, ids.get(dateC));
        Assert.assertEquals(assessment.getDate(), dateB);

        assessment = createDefaultAssessment();
        assessment.setDate(dateB);

        mAssessmentDao.store(assessment);
        final Long expectedId = assessment.getId();

        Assert.assertEquals(mAssessmentDao.getPreviousAssessmentByDate(animalId, dateC, ids.get(dateC)).getId(),
            expectedId);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByUnspecifiedFilters() throws Exception
    {
        final int expectedResults = 3;
        Collection<Assessment> result = mAssessmentDao.getAssessments(null, null, null, null, null, null, null, null,
            null);
        Assert.assertTrue(result.isEmpty());

        for (int i = 0; i < expectedResults; i++)
        {
            Assessment assessment = createDefaultAssessment();
            mAssessmentDao.store(assessment);
        }

        result = mAssessmentDao.getAssessments(null, null, null, null, null, null, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByAnimal() throws Exception
    {
        final Long id = AnimalDaoImplTest.INITIAL_ANIMAL_1_ID;
        final int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(id, null, null, null, null, null, null, null,
            null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.getAnimal().setId(id);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(id, null, null, null, null, null, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByDates() throws Exception
    {
        String dateFrom = DATE_A;
        String assesmentDate = DATE_B;
        String dateTo = DATE_C;

        final int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(null, dateFrom, dateTo, null, null, null, null,
            null, null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.setDate(assesmentDate);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(null, dateFrom, dateTo, null, null, null, null, null, null);
        Assert.assertEquals(result.size(), expectedResults);

        result = mAssessmentDao.getAssessments(null, dateFrom, dateFrom, null, null, null, null, null, null);
        Assert.assertTrue(result.isEmpty());

        result = mAssessmentDao.getAssessments(null, dateFrom, assesmentDate, null, null, null, null, null, null);
        Assert.assertEquals(result.size(), expectedResults);

        assessment = createDefaultAssessment();
        assessment.setDate(dateFrom);
        mAssessmentDao.store(assessment);

        int singleDateExpectedResults = 2;
        result = mAssessmentDao.getAssessments(null, dateFrom, null, null, null, null, null, null, null);
        Assert.assertEquals(result.size(), singleDateExpectedResults);

        assessment = createDefaultAssessment();
        assessment.setDate(dateTo);
        mAssessmentDao.store(assessment);

        // CS:OFF: MagicNumber
        singleDateExpectedResults = 3;
        result = mAssessmentDao.getAssessments(null, null, dateTo, null, null, null, null, null, null);
        Assert.assertEquals(result.size(), singleDateExpectedResults);
        // CS:ON
    }

    @Test
    public void testGetAssessmentsWithCriteriaByReason() throws Exception
    {
        final Long id = AssessmentReasonDaoImplTest.REASON_1_ID;
        final int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(null, null, null, null, id, null, null, null,
            null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.getReason().setId(id);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(null, null, null, null, id, null, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByStudy() throws Exception
    {
        final Long id = StudyDaoImplTest.STUDY_ONE_ID;
        final int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(null, null, null, null, null, id, null, null,
            null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.getStudy().setId(id);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(null, null, null, null, null, id, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithMixedCriteria() throws Exception
    {
        final Long animalId = AnimalDaoImplTest.INITIAL_ANIMAL_1_ID;
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long reasonId = AssessmentReasonDaoImplTest.REASON_1_ID;
        String dateTo = DATE_C;
        String assesmentDate = DATE_B;

        final int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(animalId, null, dateTo, null, reasonId, studyId,
            null, null, null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.getStudy().setId(studyId);
        assessment.getReason().setId(reasonId);
        assessment.setDate(assesmentDate);
        assessment.getAnimal().setId(animalId);

        mAssessmentDao.store(assessment);

        assessment = createDefaultAssessment();
        assessment.getAnimal().setId(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(animalId, null, dateTo, null, reasonId, studyId, null, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByCompletenessStatus() throws Exception
    {
        boolean isComplete = true;
        int expectedResults = 1;

        Collection<Assessment> result = mAssessmentDao.getAssessments(null, null, null, null, null, null, isComplete,
            null, null);
        Assert.assertTrue(result.isEmpty());

        Assessment assessment = createDefaultAssessment();
        assessment.setIsComplete(isComplete);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessments(null, null, null, null, null, null, isComplete, null, null);
        Assert.assertEquals(result.size(), expectedResults);

        isComplete = false;
        final int incompleteAssessments = 2;
        expectedResults = incompleteAssessments;
        for (int i = 0; i < incompleteAssessments; i++)
        {
            assessment = createDefaultAssessment();
            assessment.setIsComplete(isComplete);

            mAssessmentDao.store(assessment);
        }

        result = mAssessmentDao.getAssessments(null, null, null, null, null, null, isComplete, null, null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaCount() throws Exception
    {
        final int expectedResults = 3;
        Long result = mAssessmentDao.getAssessmentsCount(null, null, null, null, null, null, null);
        Assert.assertEquals(result.longValue(), 0);

        Collection<Assessment> assessments = new ArrayList<>(expectedResults);
        for (int i = 0; i < expectedResults; i++)
        {
            Assessment assessment = createDefaultAssessment();
            assessments.add(assessment);
            mAssessmentDao.store(assessment);
        }

        result = mAssessmentDao.getAssessmentsCount(null, null, null, null, null, null, null);

        Assert.assertEquals(result.longValue(), expectedResults);
    }

    @Test
    public void testGetAssessmentsWithCriteriaByCompletenessStatusCount() throws Exception
    {
        boolean isComplete = true;
        int expectedResults = 1;

        Long result = mAssessmentDao.getAssessmentsCount(null, null, null, null, null, null, isComplete);
        Assert.assertEquals(result.longValue(), 0);

        Assessment assessment = createDefaultAssessment();
        assessment.setIsComplete(isComplete);

        mAssessmentDao.store(assessment);

        result = mAssessmentDao.getAssessmentsCount(null, null, null, null, null, null, isComplete);
        Assert.assertEquals(result.longValue(), expectedResults);

        isComplete = false;
        final int incompleteAssessments = 2;
        expectedResults = incompleteAssessments;
        for (int i = 0; i < incompleteAssessments; i++)
        {
            assessment = createDefaultAssessment();
            assessment.setIsComplete(isComplete);

            mAssessmentDao.store(assessment);
        }

        result = mAssessmentDao.getAssessmentsCount(null, null, null, null, null, null, isComplete);

        Assert.assertEquals(result.longValue(), expectedResults);
    }

    @Test
    public void testDeleteAssessmentScore() throws Exception
    {
        Collection<Assessment> assessments = mAssessmentDao.getAssessments(null, null);
        Collection<AssessmentScore> scores = mAssessmentDao.getAssessmentScores();
        Assert.assertTrue(assessments.isEmpty() && scores.isEmpty());

        Assessment assessment = createDefaultAssessment();
        mAssessmentDao.store(assessment);

        scores = mAssessmentDao.getAssessmentScores();
        Assert.assertFalse(scores.isEmpty());

        AssessmentScore score = assessment.getScore();
        assessment.setScore(null);
        mAssessmentDao.update(assessment);

        mAssessmentDao.deleteAssessmentScore(score);
        scores = mAssessmentDao.getAssessmentScores();
        Assert.assertTrue(scores.isEmpty());
    }

    @Test
    public void testGetCountAssessmentsByAnimalId()
    {
        // CS:OFF: MagicNumber
        long assessmentCountForAnimal = mAssessmentDao.getCountAnimalAssessments(10000L);
        Assert.assertEquals(assessmentCountForAnimal, 0);
        // CS:ON
    }

    @Test
    public void testGetCountAssessmentsByTemplateId()
    {
        // CS:OFF: MagicNumber
        long assessmentCountForTemplate = mAssessmentDao.getCountAssessmentsByTemplateId(10000L);
        Assert.assertEquals(assessmentCountForTemplate, 0);
        // CS:ON
    }

    private Assessment createDefaultAssessment() throws AWNoSuchEntityException, AWAssessmentCreationException
    {
        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID);
        return mAssessmentProvider.createAssessment(template);
    }
}
