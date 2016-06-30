package uk.gov.phe.erdst.sc.awag.service.extractor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueAnimalExtractorTest extends AssessmentUniqueEntitiesExtractorTestTemplate
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
        return Animal.class;
    }

    @Test
    public void testWithIdenticalAnimal()
    {
        super.testWithIdentical();
    }

    @Test
    public void testWithDifferentAnimal()
    {
        super.testWithDifferent();
    }

    // CS:OFF: MagicNumber
    @Test(timeOut = 1000L)
    public void testWithNullAnimal()
    {
        super.testWithNull();
    }

    // CS:ON

    @Override
    protected Assessment createTestAssessment(Long animalId, String animalNumber)
    {
        Assessment assessment = new Assessment();
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setAnimalNumber(animalNumber);
        assessment.setAnimal(animal);

        return assessment;
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        assessment.setAnimal(null);
    }
}
