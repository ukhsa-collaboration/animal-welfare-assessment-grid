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

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

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

    @Test
    private void testInvalidStudyGroupAnimals()
    {
        AnimalClientData animal1 = new AnimalClientData(1L, "animal 1");
        AnimalClientData animal2 = new AnimalClientData(2L, "animal 2");
        AnimalClientData animal3 = new AnimalClientData(3L, "animal 3");
        AnimalClientData animal4 = new AnimalClientData(4L, "animal 4");
        AnimalClientData animal5 = new AnimalClientData(5L, "animal 5");
        AnimalClientData animal6 = new AnimalClientData(6L, "animal 6");
        AnimalClientData animal7 = new AnimalClientData(7L, "animal 7");
        AnimalClientData animal8 = new AnimalClientData(8L, "animal 8");

        StudyGroupClientData studyGroup1 = new StudyGroupClientData();
        studyGroup1.studyGroupId = 1L;
        studyGroup1.studyGroupName = "study group 1";
        studyGroup1.studyGroupAnimals.add(animal1);
        studyGroup1.studyGroupAnimals.add(animal2);
        studyGroup1.studyGroupAnimals.add(animal3);
        studyGroup1.studyGroupAnimals.add(animal4);
        studyGroup1.studyGroupAnimals.add(animal5);

        StudyGroupClientData studyGroup2 = new StudyGroupClientData();
        studyGroup2.studyGroupId = 2L;
        studyGroup2.studyGroupName = "study group 2";
        studyGroup2.studyGroupAnimals.add(animal4);
        studyGroup2.studyGroupAnimals.add(animal5);
        studyGroup2.studyGroupAnimals.add(animal6);
        studyGroup2.studyGroupAnimals.add(animal7);
        studyGroup2.studyGroupAnimals.add(animal8);

        mStudyClientData.studyGroups.add(studyGroup1);
        mStudyClientData.studyGroups.add(studyGroup2);

        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(mStudyClientData);
        Assert.assertEquals(constraintViolations.size(), 1);
    }

}
