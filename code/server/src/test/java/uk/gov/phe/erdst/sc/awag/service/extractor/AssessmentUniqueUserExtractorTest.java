package uk.gov.phe.erdst.sc.awag.service.extractor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueUserExtractorTest extends AssessmentUniqueEntitiesExtractorTestTemplate
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
        return User.class;
    }

    @Test
    public void testWithIdenticalUser()
    {
        super.testWithIdentical();
    }

    @Test
    public void testWithDifferentUser()
    {
        super.testWithDifferent();
    }

    // CS:OFF: MagicNumber
    @Test(timeOut = 1000L)
    public void testWithNullUser()
    {
        super.testWithNull();
    }

    // CS:ON

    @Override
    protected Assessment createTestAssessment(Long userId, String userName)
    {
        Assessment assessment = new Assessment();
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        assessment.setPerformedBy(user);
        return assessment;
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        assessment.setStudy(null);
    }
}
