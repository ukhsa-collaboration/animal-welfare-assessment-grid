package uk.gov.phe.erdst.sc.awag.service.animal;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AnimalFactoryTest
{
    private static final String TEST_ANIMAL_1 = "Test Animal 1";

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private AnimalFactory mAnimalFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate()
    {
        AnimalClientData clientData = (AnimalClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_ANIMAL_RAW_DATA, AnimalClientData.class);
        Animal animal = mAnimalFactory.create(clientData);

        assertNonIdProperties(animal, clientData);
    }

    @Test
    public void testCreateFromImportAnimal()
    {
        // TODO complete test
        ImportAnimal importAnimal = new ImportAnimal();
        importAnimal.setAnimalNumber(TEST_ANIMAL_1);
        Animal animal = mAnimalFactory.create(importAnimal);
        Assert.assertEquals(animal.getAnimalNumber(), TEST_ANIMAL_1);
    }

    @Test
    public void testUpdate()
    {
        Animal blankAnimal = AnimalTestUtils.createAnimalWithIdOnly(2L);

        AnimalClientData animalDiedClientData = (AnimalClientData) mRequestConverter
            .convert(TestConstants.DUMMY_UPDATE_ANIMAL_RAW_DATA, AnimalClientData.class);

        mAnimalFactory.update(blankAnimal, animalDiedClientData);

        assertNonIdProperties(blankAnimal, animalDiedClientData);
    }

    private void assertNonIdProperties(Animal animal, AnimalClientData clientData)
    {
        Assert.assertEquals(animal.getAnimalNumber(), clientData.number);
        Assert.assertEquals(animal.getDateOfBirth(), clientData.dob);
        Assert.assertEquals(animal.getAssessmentTemplate().getId(), clientData.assessmentTemplate);
        Assert.assertEquals(Boolean.valueOf(animal.isAlive()), clientData.isAlive);
        Assert.assertEquals(animal.isAssessed(), clientData.isAssessed);
        Assert.assertEquals(animal.getSex().getId(), clientData.sex);
        Assert.assertEquals(animal.getSpecies().getId(), clientData.species);
        Assert.assertEquals(animal.getSource().getId(), clientData.source);
        Assert.assertEquals(animal.getDam().getId(), clientData.dam);
        Assert.assertEquals(animal.getFather().getId(), clientData.father);
    }
}
