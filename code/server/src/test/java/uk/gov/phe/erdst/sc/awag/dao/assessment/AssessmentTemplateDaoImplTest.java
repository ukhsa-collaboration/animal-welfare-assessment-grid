package uk.gov.phe.erdst.sc.awag.dao.assessment;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentTemplateDaoImplTest
{
    private static final String ASSESSMENT_TEMPLATE_LIKE_TERM = "Tem";
    private static final int EXPECTED_TEMPLATE_LIKE = 3;

    private AssessmentTemplateDao mAssessmentTemplateDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentTemplateDao = (AssessmentTemplateDao) GlassfishTestsHelper.lookupMultiInterface(
            "AssessmentTemplateDaoImpl", AssessmentTemplateDao.class);
    }

    @Test
    public void testGetAssessmentTemplatesLike()
    {
        List<AssessmentTemplate> assessmentTemplates = mAssessmentTemplateDao.getEntitiesLike(
            ASSESSMENT_TEMPLATE_LIKE_TERM, null, null);
        Assert.assertEquals(assessmentTemplates.size(), EXPECTED_TEMPLATE_LIKE);
    }

    @Test
    public void testGetTemplatesLikePagedNoLimit()
    {
        List<AssessmentTemplate> allTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            null, null);
        List<AssessmentTemplate> pagedTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allTemplates, pagedTemplates);
    }

    @Test
    public void testGetTemplatesLikePagedNoOffset()
    {
        List<AssessmentTemplate> allTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            null, null);
        List<AssessmentTemplate> pagedTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allTemplates, pagedTemplates);
    }

    @Test
    public void testGetTemplatesLikeValidOffsetLimit()
    {
        List<AssessmentTemplate> allTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            null, null);
        List<AssessmentTemplate> pagedTemplates = mAssessmentTemplateDao.getEntitiesLike(ASSESSMENT_TEMPLATE_LIKE_TERM,
            TestConstants.TEST_OFFSET, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allTemplates, pagedTemplates);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
