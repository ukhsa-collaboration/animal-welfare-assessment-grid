package uk.gov.phe.erdst.sc.awag.service.assessment;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentScoreUtils;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentScoreFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentFactoryTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentScoreFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private AssessmentScoreFactory mAssessmentScoreFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate()
    {
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter.convert(
            TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentTemplate template = AssessmentFactoryTestUtils.createTemplateFromAssessmentClientData(
            TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);
        AssessmentScore assessmentScore = mAssessmentScoreFactory.create(template, clientData);
        Assert.assertTrue(AssessmentScoreUtils.isAssessmentScoreEqual(assessmentScore, clientData));
    }

    @Test
    public void testUpdate()
    {
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter.convert(
            TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentTemplate template = AssessmentFactoryTestUtils.createTemplateFromAssessmentClientData(
            TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);
        AssessmentScore assessmentScore = mAssessmentScoreFactory.create(template, clientData);

        AssessmentFactoryTestUtils.changeClientData(clientData);
        AssessmentTemplate templateChanged = AssessmentFactoryTestUtils.createTemplateFromAssessmentClientData(
            TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);

        assessmentScore = mAssessmentScoreFactory.update(assessmentScore, templateChanged, clientData);

        Assert.assertTrue(AssessmentScoreUtils.isAssessmentScoreEqual(assessmentScore, clientData));
    }
}
