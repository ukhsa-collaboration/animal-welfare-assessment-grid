package uk.gov.phe.erdst.sc.awag.service.extractor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueReasonsExtractorTest extends AssessmentUniqueEntitiesExtractorTestTemplate
{
    @Override
    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected Class<?> getTestEntityClass()
    {
        return AssessmentReason.class;
    }

    @Test
    public void testWithIdenticalReason()
    {
        super.testWithIdentical();
    }

    @Test
    public void testWithDifferentReason()
    {
        super.testWithDifferent();
    }

    // CS:OFF: MagicNumber
    @Test(timeOut = 1000L)
    public void testWithNullReason()
    {
        super.testWithNull();
    }

    // CS:ON

    @Override
    protected Assessment createTestAssessment(Long reasonId, String reasonName)
    {
        Assessment assessment = new Assessment();
        AssessmentReason reason = new AssessmentReason();
        reason.setId(reasonId);
        reason.setName(reasonName);
        assessment.setReason(reason);
        return assessment;
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        assessment.setReason(null);
    }
}
