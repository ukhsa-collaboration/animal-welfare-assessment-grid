package uk.gov.phe.erdst.sc.awag.service.assessment;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentScoreUtils;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentScoreFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentFactoryTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto;

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
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter
            .convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentTemplate template = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);
        AssessmentScore assessmentScore = mAssessmentScoreFactory.create(template, clientData);
        AssessmentScoreComparisonResultDto comparison = AssessmentScoreUtils.isAssessmentScoreEqual(assessmentScore,
            clientData, null);
        Assert.assertTrue(comparison.isAssessmentScoreEqual);
    }

    @Test
    public void testUpdate()
    {
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter
            .convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentTemplate template = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);
        AssessmentScore assessmentScore = mAssessmentScoreFactory.create(template, clientData);

        AssessmentFactoryTestUtils.changeClientData(clientData);
        AssessmentTemplate templateChanged = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);

        assessmentScore = mAssessmentScoreFactory.update(assessmentScore, templateChanged, clientData);

        AssessmentScoreComparisonResultDto comparison = AssessmentScoreUtils.isAssessmentScoreEqual(assessmentScore,
            clientData, null);
        Assert.assertTrue(comparison.isAssessmentScoreEqual);
    }
}
