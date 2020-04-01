package uk.gov.phe.erdst.sc.awag.service.assessment;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentScoreUtils;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.service.mock.MockAssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentFactoryTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private MockAssessmentPartsFactory mAssessmentPartsFactory;

    @Inject
    private AssessmentFactory mAssessmentFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate() throws Exception
    {
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter
            .convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentTemplate template = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);

        AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);
        final boolean isComplete = true;
        Assessment assessment = mAssessmentFactory.create(clientData, template, assessmentParts, isComplete);

        Assert.assertNotEquals(assessment.getId(), clientData.id);
        assertNonIdProperties(assessment, clientData);
    }

    @Test
    public void testUpdate()
    {
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter
            .convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA, AssessmentClientData.class);

        AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);
        AssessmentTemplate template = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);

        boolean isComplete = false;
        Assessment assessment = mAssessmentFactory.create(clientData, template, assessmentParts, isComplete);

        AssessmentFactoryTestUtils.changeClientData(clientData);

        AssessmentTemplate changedTemplate = AssessmentFactoryTestUtils
            .createTemplateFromAssessmentClientData(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID, clientData);
        AssessmentParts changedAssessmentParts = mAssessmentPartsFactory.create(clientData);

        Long originalId = assessment.getId();
        boolean originalIsComplete = assessment.isComplete();
        isComplete = true;
        mAssessmentFactory.update(assessment, changedTemplate, clientData, changedAssessmentParts, isComplete);

        Assert.assertEquals(assessment.getId(), originalId);
        Assert.assertNotEquals(assessment.isComplete(), originalIsComplete);
        assertNonIdProperties(assessment, clientData);
    }

    private void assertNonIdProperties(Assessment assessment, AssessmentClientData clientData)
    {
        Assert.assertEquals(assessment.getAnimal().getId(), clientData.animalId);
        Assert.assertEquals(assessment.getReason().getName(), clientData.reason);
        Assert.assertEquals(assessment.getDate(), clientData.date);
        Assert.assertEquals(assessment.getAnimalHousing().getName(), clientData.animalHousing);
        Assert.assertEquals(assessment.getPerformedBy().getName(), clientData.performedBy);

        AssessmentScoreComparisonResultDto comparison = AssessmentScoreUtils
            .isAssessmentScoreEqual(assessment.getScore(), clientData, null);
        Assert.assertTrue(comparison.isAssessmentScoreEqual);
    }

}
