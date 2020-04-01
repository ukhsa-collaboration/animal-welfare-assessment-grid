package uk.gov.phe.erdst.sc.awag.validation.studygroup;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class StudyGroupValidationTest
{
    private static final String VALID_STUDY_GROUP_NUMBER = "Study group 1";
    private static final String INVALID_PATTERN_STUDY_GROUP_NUMBER = "Study$*&SHS^&*(";

    private Validator mValidator;
    private StudyGroupClientData mStudyGroupClientData;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @BeforeMethod
    private void resetValidStudyGroupClientData()
    {
        mStudyGroupClientData = new StudyGroupClientData(-1L, VALID_STUDY_GROUP_NUMBER);
    }

    @Test
    private void testValidStudyGroup()
    {
        int expectedNoViolations = 0;
        Set<ConstraintViolation<StudyGroupClientData>> constraintViolations = mValidator
            .validate(mStudyGroupClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidStudyGroupNumberNull()
    {
        int expectedNoViolations = 1;
        mStudyGroupClientData.studyGroupName = null;
        Set<ConstraintViolation<StudyGroupClientData>> constraintViolations = mValidator
            .validate(mStudyGroupClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }

    @Test
    private void testInvalidStudyGroupNumberRegex()
    {
        int expectedNoViolations = 1;
        mStudyGroupClientData.studyGroupName = INVALID_PATTERN_STUDY_GROUP_NUMBER;
        Set<ConstraintViolation<StudyGroupClientData>> constraintViolations = mValidator
            .validate(mStudyGroupClientData);
        Assert.assertEquals(constraintViolations.size(), expectedNoViolations);
    }
}
