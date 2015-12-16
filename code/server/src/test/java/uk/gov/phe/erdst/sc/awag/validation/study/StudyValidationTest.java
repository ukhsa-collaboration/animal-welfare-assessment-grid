package uk.gov.phe.erdst.sc.awag.validation.study;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyClientData;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class StudyValidationTest
{
    private static final String VALID_STUDY_NUMBER = "Study 1";
    private static final String INVALID_PATTERN_STUDY_NUMBER = "Study$*&SHS^&*(";
    private static final String INVALID_NULL_STUDY_NUMBER = null;

    private Validator mValidator;
    private StudyClientData mStudyClientData;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidStudyClientData()
    {
        mStudyClientData = new StudyClientData(-1L, VALID_STUDY_NUMBER);
    }

    @Test
    private void testValidStudy()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(mStudyClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidStudyNumberNull()
    {
        int expectedNoViolations = 1;
        mStudyClientData.studyName = INVALID_NULL_STUDY_NUMBER;
        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(mStudyClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidStudyNumberRegex()
    {
        int expectedNoViolations = 1;
        mStudyClientData.studyName = INVALID_PATTERN_STUDY_NUMBER;
        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(mStudyClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
