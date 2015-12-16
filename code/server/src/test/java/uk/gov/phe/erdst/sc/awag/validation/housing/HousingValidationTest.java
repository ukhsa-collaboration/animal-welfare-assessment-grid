package uk.gov.phe.erdst.sc.awag.validation.housing;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class HousingValidationTest
{
    private static final String VALID_ANIMAL_HOUSING_NAME = "Housing 1";
    private static final String INVALID_PATTERN_ANIMAL_HOUSING_NAME = "Hou$*&SHS^&*(";
    private static final String INVALID_MIN_ANIMAL_HOUSING_NAME = "Ho";
    private static final String INVALID_MAX_ANIMAL_HOUSING_NAME;
    private static final String INVALID_NULL_ANIMAL_HOUSING_NAME = null;
    private Validator mValidator;
    private AnimalHousingClientData mAnimalHousingClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_ANIMAL_HOUSING_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidAnimalHousingClientData()
    {
        mAnimalHousingClientData = new AnimalHousingClientData(-1L, VALID_ANIMAL_HOUSING_NAME);
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testValidAnimalHousingClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator
            .validate(mAnimalHousingClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidAnimalHousingClientData()
    {
        int expectedNoViolations = 1;
        mAnimalHousingClientData.housingName = INVALID_PATTERN_ANIMAL_HOUSING_NAME;
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator
            .validate(mAnimalHousingClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidAnimalHousingClientData()
    {
        int expectedNoViolations = 1;
        mAnimalHousingClientData.housingName = INVALID_MAX_ANIMAL_HOUSING_NAME;
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator
            .validate(mAnimalHousingClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidAnimalHousingClientData()
    {
        int expectedNoViolations = 1;
        mAnimalHousingClientData.housingName = INVALID_MIN_ANIMAL_HOUSING_NAME;
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator
            .validate(mAnimalHousingClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidAnimalHousingClientData()
    {
        int expectedNoViolations = 1;
        mAnimalHousingClientData.housingName = INVALID_NULL_ANIMAL_HOUSING_NAME;
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator
            .validate(mAnimalHousingClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
