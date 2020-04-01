package uk.gov.phe.erdst.sc.awag.validation.animal;

/*package uk.gov.phe.erdst.sc.awag.validation;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.validation.impl.AnimalValidator;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AnimalValidationTest
{
    private RequestConverter mAnimalRequestConverter;
    private AnimalValidator mAnimalValidator;

        @Inject
        private AnimalValidator mAnimalValidator;

    // private ValidatorErrorFactory mAnimalValidatorErrorFactory;
    // private RequestConverter mAnimalRequestConverter;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAnimalRequestConverter = (RequestConverter) GlassfishTestsHelper
            .lookup("RequestConverter");
        mAnimalValidator = (AnimalValidator) GlassfishTestsHelper.lookup("AnimalValidator");
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    private AnimalClientData getAnimalClientData(String rawData)
    {
        return (AnimalClientData) mAnimalRequestConverter.convert(rawData, AnimalClientData.class);
    }

    @Test
    public void testValidAnimal()
    {
        DummyContext context = new DummyContext();
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_VALID_RAW_DATA);
        // mAnimalValidator.isValid(clientData, context);
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AnimalClientData>> animalConstraintViolations = validator
            .validate(clientData);

         mAnimalValidator.isValid(clientData, context);
        Assert.assertEquals(animalConstraintViolations.size(), 0);
    }

    @Test
    public void testNullAnimalNumberFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_NULL_ANIMAL_NUMBER_RAW_DATA);

    }

    @Test
    public void testMinFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_MIN_ANIMAL_NUMBER_LENGTH_RAW_DATA);
    }

    @Test
    public void testMaxFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_MAX_ANIMAL_NUMBER_LENGTH_RAW_DATA);
    }

    @Test
    public void testDobFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_DOB_INVALID_RAW_DATA);
    }

    @Test
    public void testParentsSameAsEachOtherFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_PARENT_SAME_RAW_DATA);
    }

    @Test
    public void TestParentsSameAsAnimalFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_PARENT_ANIMAL_SAME_RAW_DATA);
    }

    @Test
    public void testParentDamNotFemaleFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_ANIMAL_DAM_NOT_FEMALE_RAW_DATA);
    }

    @Test
    public void testParentFatherNotMaleFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_ANIMAL_FATHER_NOT_MALE_RAW_DATA);
    }

    @Test
    public void testParentAnimalOlderDamFail()
    {
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_PARENT_DAM_DOB_INVALID_RAW_DATA);
    }

    @Test
    public void testParentAnimalOlderFatherFail()
    {
    // CS:OFF: LineLength
        AnimalClientData clientData = getAnimalClientData(TestConstants.DUMMY_ANIMAL_PARENT_FATHER_DOB_INVALID_RAW_DATA);
    // CS:ON
    }
}
*/