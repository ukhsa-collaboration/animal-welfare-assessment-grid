package uk.gov.phe.erdst.sc.awag.service.extractor;

import java.util.ArrayList;
import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDtoCollection;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueEntitiesExtractorGeneralTest extends AssessmentUniqueEntitiesExtractorTestTemplate
{
    @Override
    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Test
    public void testNoAssessments()
    {
        Collection<Assessment> data = new ArrayList<>();
        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, getTestEntityClass(),
            mEntitySelectDtoFactory);
        Assert.assertTrue(extractedValues.values.isEmpty());
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testUnsupportedClass()
    {
        Collection<Assessment> data = new ArrayList<>();
        data.add(new Assessment());
        mExtractor.extract(data, Assessment.class, mEntitySelectDtoFactory);
    }

    @Override
    protected Class<?> getTestEntityClass()
    {
        // This test class doesn't test any entity class in particular
        return Object.class;
    }

    @Override
    protected Assessment createTestAssessment(Long studyId, String studyNumber)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        throw new UnsupportedOperationException();
    }
}
