package uk.gov.phe.erdst.sc.awag.validation.species;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.SpeciesClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.StringHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class SpeciesValidationTest
{
    private static final String VALID_SPECIES_NAME = "Species 1";
    private static final String INVALID_PATTERN_SPECIES_NAME = "Spe$*&SHS^&*(";
    private static final String INVALID_MIN_SPECIES_NAME = "Sp";
    private static final String INVALID_MAX_SPECIES_NAME;
    private static final String INVALID_NULL_SPECIES_NAME = null;
    private Validator mValidator;
    private SpeciesClientData mSpeciesClientData;

    static
    {
        final int invalidNameLength = 256;
        INVALID_MAX_SPECIES_NAME = StringHelper.getStringOfLength(invalidNameLength);
    }

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidSpeciesClientData()
    {
        mSpeciesClientData = new SpeciesClientData(-1L, VALID_SPECIES_NAME);
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testValidSpeciesClientData()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(mSpeciesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testPatternInvalidSpeciesClientData()
    {
        int expectedNoViolations = 1;
        mSpeciesClientData.speciesName = INVALID_PATTERN_SPECIES_NAME;
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(mSpeciesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMaxInvalidSpeciesClientData()
    {
        int expectedNoViolations = 1;
        mSpeciesClientData.speciesName = INVALID_MAX_SPECIES_NAME;
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(mSpeciesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testMinInvalidSpeciesClientData()
    {
        int expectedNoViolations = 1;
        mSpeciesClientData.speciesName = INVALID_MIN_SPECIES_NAME;
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(mSpeciesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testNullInvalidSpeciesClientData()
    {
        int expectedNoViolations = 1;
        mSpeciesClientData.speciesName = INVALID_NULL_SPECIES_NAME;
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(mSpeciesClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
