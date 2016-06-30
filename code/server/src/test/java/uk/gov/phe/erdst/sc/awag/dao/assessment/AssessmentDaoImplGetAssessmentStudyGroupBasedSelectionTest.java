package uk.gov.phe.erdst.sc.awag.dao.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.dao.animal.AnimalDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.reason.AssessmentReasonDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.study.StudyDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.studygroup.StudyGroupDaoImplTest;
import uk.gov.phe.erdst.sc.awag.dao.user.UserDaoImplTest;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentProvider;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.AssessmentBasedTestsUtils;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentDaoImplGetAssessmentStudyGroupBasedSelectionTest
{
    private AssessmentProvider mAssessmentProvider;
    private AssessmentDao mAssessmentDao;
    private AssessmentTemplateDao mAssessmentTemplateDao;
    private StudyDao mStudyDao;
    private StudyGroupDao mStudyGroupDao;
    private AnimalDao mAnimalDao;
    private AssessmentReasonDao mAssessmentReasonDao;
    private UserDao mUserDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentProvider = (AssessmentProvider) GlassfishTestsHelper.lookup("AssessmentProvider");

        mAssessmentDao = (AssessmentDao) GlassfishTestsHelper.lookupMultiInterface("AssessmentDaoImpl",
            AssessmentDao.class);

        mAssessmentTemplateDao = (AssessmentTemplateDao) GlassfishTestsHelper.lookupMultiInterface(
            "AssessmentTemplateDaoImpl", AssessmentTemplateDao.class);

        mStudyDao = (StudyDao) GlassfishTestsHelper.lookupMultiInterface("StudyDaoImpl", StudyDao.class);

        mStudyGroupDao = (StudyGroupDao) GlassfishTestsHelper.lookupMultiInterface("StudyGroupDaoImpl",
            StudyGroupDao.class);

        mAnimalDao = (AnimalDao) GlassfishTestsHelper.lookupMultiInterface("AnimalDaoImpl", AnimalDao.class);

        mAssessmentReasonDao = (AssessmentReasonDao) GlassfishTestsHelper.lookupMultiInterface(
            "AssessmentReasonDaoImpl", AssessmentReasonDao.class);

        mUserDao = (UserDao) GlassfishTestsHelper.lookupMultiInterface("UserDaoImpl", UserDao.class);
    }

    @AfterMethod
    public void tearDownMethod()
    {
        AssessmentBasedTestsUtils.deleteAllAssessments(mAssessmentDao);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    @Test
    public void testSameGroup() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;
        final int expectedResults = 2;

        checkPreconditionNoAssessmentsPresent();

        Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);

        Study study = assessment.getStudy();
        StudyGroup[] studyGroups = study.getStudyGroups().toArray(new StudyGroup[] {});
        Assert.assertTrue(studyGroups.length == 1 && studyGroups[0].getId().equals(studyGroupId));

        assessment.getStudy().setId(studyId);
        mAssessmentDao.store(assessment);

        assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
        assessment.getStudy().setId(studyId);
        mAssessmentDao.store(assessment);

        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, null, null, null,
            null);

        Assert.assertEquals(result.size(), expectedResults);
    }

    @Test
    public void testDiffGroups() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final List<Long> groupsIds = new ArrayList<>();
        groupsIds.add(StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID);
        groupsIds.add(StudyGroupDaoImplTest.STUDY_GROUP_TWO_ID);

        checkPreconditionNoAssessmentsPresent();

        StudyGroup studyGroup2 = mStudyGroupDao.getStudyGroup(StudyGroupDaoImplTest.STUDY_GROUP_TWO_ID);
        Animal animal2 = mAnimalDao.getAnimal(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
        studyGroup2.getAnimals().add(animal2);
        mStudyGroupDao.store(studyGroup2);

        StudyGroup studyGroup1 = mStudyGroupDao.getStudyGroup(StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID);
        studyGroup1.getAnimals().remove(animal2);
        mStudyGroupDao.store(studyGroup1);

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);
        study.getStudyGroups().add(studyGroup2);
        mStudyDao.update(study);

        Set<StudyGroup> studyGroups = study.getStudyGroups();
        final int expectedGroups = 2;
        Assert.assertEquals(studyGroups.size(), expectedGroups);
        for (StudyGroup group : studyGroups)
        {
            Assert.assertTrue(groupsIds.contains(group.getId()));
        }

        final int studyGroup1Assessments = 31;
        for (int i = 0; i < studyGroup1Assessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            mAssessmentDao.store(assessment);
        }

        final int studyGroup2Assessments = 17;
        for (int i = 0; i < studyGroup2Assessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
            assessment.getStudy().setId(study.getId());
            mAssessmentDao.store(assessment);
        }

        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId,
            StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID, null, null, null, null, null);

        Assert.assertEquals(result.size(), studyGroup1Assessments);

        result = mAssessmentDao.getAssessments(studyId, StudyGroupDaoImplTest.STUDY_GROUP_TWO_ID, null, null, null,
            null, null);

        Assert.assertEquals(result.size(), studyGroup2Assessments);

        cleanUpAfterTestDiffGroups(study, studyGroup2, animal2, studyGroup1);
    }

    private void
        cleanUpAfterTestDiffGroups(Study study, StudyGroup studyGroup2, Animal animal2, StudyGroup studyGroup1)
            throws Exception
    {
        study.getStudyGroups().remove(studyGroup2);
        studyGroup2.getAnimals().remove(animal2);
        studyGroup1.getAnimals().add(animal2);

        mStudyDao.update(study);
        mStudyGroupDao.store(studyGroup1);
        mStudyGroupDao.store(studyGroup2);
    }

    @Test
    public void testGroupNoAssessment() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;

        checkPreconditionNoAssessmentsPresent();

        Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);

        Study study = assessment.getStudy();
        StudyGroup[] studyGroups = study.getStudyGroups().toArray(new StudyGroup[] {});
        Assert.assertTrue(studyGroups.length == 1
            && studyGroups[0].getId().equals(StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID));

        Collection<Assessment> secondaryData = new ArrayList<>();
        assessment.getStudy().setId(studyId);
        mAssessmentDao.store(assessment);
        secondaryData.add(assessment);

        assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
        assessment.getStudy().setId(studyId);
        mAssessmentDao.store(assessment);
        secondaryData.add(assessment);

        Assert.assertTrue(mAssessmentDao.getAssessments(studyId, StudyGroupDaoImplTest.STUDY_GROUP_TWO_ID, null, null,
            null, null, null).isEmpty());
    }

    @Test
    public void testNonExistentEntities() throws Exception
    {
        final Long nonExistentStudyId = 123L;
        final Long nonExistentStudyGroupId = nonExistentStudyId;
        Collection<Assessment> result = mAssessmentDao.getAssessments(nonExistentStudyId, nonExistentStudyGroupId,
            null, null, null, null, null);

        Assert.assertTrue(result.isEmpty());
    }

    /*
     * test selection with:
     *      animal
     *      reason
     *      user
     *      date from and to
     *      
     *      
     *      create a number of assessments with variant a
     *      one with variant b
     *      search for variant b
     */

    @Test
    public void testGroupWithSpecificAnimal() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;

        checkPreconditionNoAssessmentsPresent();

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().size(), 1);

        final int extraAssessments = 31;
        for (int i = 0; i < extraAssessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            mAssessmentDao.store(assessment);
        }

        Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_2_ID);
        assessment.getStudy().setId(study.getId());
        mAssessmentDao.store(assessment);

        final int expected = 1;
        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId,
            AnimalDaoImplTest.INITIAL_ANIMAL_2_ID, null, null, null, null);

        Assert.assertEquals(result.size(), expected);
    }

    private void checkPreconditionNoAssessmentsPresent()
    {
        Assert.assertTrue(mAssessmentDao.getCountAssessments().equals(0L));
    }

    @Test
    public void testGroupWithSpecificReason() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;

        AssessmentReason reason1 = mAssessmentReasonDao.getEntityById(AssessmentReasonDaoImplTest.REASON_1_ID);

        AssessmentReason reason2 = mAssessmentReasonDao.getEntityById(AssessmentReasonDaoImplTest.REASON_2_ID);

        checkPreconditionNoAssessmentsPresent();

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().size(), 1);

        final int extraAssessments = 31;
        for (int i = 0; i < extraAssessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            assessment.setReason(reason1);
            mAssessmentDao.store(assessment);
        }

        Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
        assessment.getStudy().setId(study.getId());
        assessment.setReason(reason2);
        mAssessmentDao.store(assessment);

        final int expected = 1;
        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, null, null, null,
            reason2.getId());

        Assert.assertEquals(result.size(), expected);
    }

    @Test
    public void testGroupWithSpecificUser() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;

        User user1 = mUserDao.getEntityById(UserDaoImplTest.USER_1_ID);
        User user2 = mUserDao.getEntityById(UserDaoImplTest.USER_2_ID);

        checkPreconditionNoAssessmentsPresent();

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().size(), 1);

        final int extraAssessments = 31;
        for (int i = 0; i < extraAssessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            assessment.setPerformedBy(user1);
            mAssessmentDao.store(assessment);
        }

        Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
        assessment.getStudy().setId(study.getId());
        assessment.setPerformedBy(user2);
        mAssessmentDao.store(assessment);

        final int expected = 1;
        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, null, null,
            user2.getId(), null);

        Assert.assertEquals(result.size(), expected);
    }

    @Test
    public void testGroupWithSpecificDates() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;

        final String dateA = "2014-08-01T00:00:00.000Z";
        final String dateB = "2014-08-15T00:00:00.000Z";
        final String dateC = "2014-08-20T00:00:00.000Z";
        final String dateD = "2014-08-30T00:00:00.000Z";

        checkPreconditionNoAssessmentsPresent();

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().size(), 1);

        Assessment assessment = createAssessment(studyId, AnimalDaoImplTest.INITIAL_ANIMAL_1_ID, dateA);
        mAssessmentDao.store(assessment);

        assessment = createAssessment(studyId, AnimalDaoImplTest.INITIAL_ANIMAL_1_ID, dateB);
        mAssessmentDao.store(assessment);

        assessment = createAssessment(studyId, AnimalDaoImplTest.INITIAL_ANIMAL_1_ID, dateC);
        mAssessmentDao.store(assessment);

        assessment = createAssessment(studyId, AnimalDaoImplTest.INITIAL_ANIMAL_1_ID, dateD);
        mAssessmentDao.store(assessment);

        // CS:OFF: MagicNumber
        int expectedResult = 3;
        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, dateB, null, null,
            null);
        Assert.assertEquals(result.size(), expectedResult);

        expectedResult = 3;
        result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, null, dateC, null, null);
        Assert.assertEquals(result.size(), expectedResult);
        // CS:ON

        expectedResult = 2;
        result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, dateB, dateC, null, null);
        Assert.assertEquals(result.size(), expectedResult);

        expectedResult = 1;
        result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, dateD, dateD, null, null);
        Assert.assertEquals(result.size(), expectedResult);
    }

    @Test
    public void testGroupByCompletenessStatus() throws Exception
    {
        final Long studyId = StudyDaoImplTest.STUDY_ONE_ID;
        final Long studyGroupId = StudyGroupDaoImplTest.STUDY_GROUP_ONE_ID;

        checkPreconditionNoAssessmentsPresent();

        Study study = mStudyDao.getEntityById(StudyDaoImplTest.STUDY_ONE_ID);

        final int completeAssessments = 47;
        for (int i = 0; i < completeAssessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            assessment.setIsComplete(true);
            mAssessmentDao.store(assessment);
        }

        final int incompleteAssessments = 17;
        for (int i = 0; i < incompleteAssessments; i++)
        {
            Assessment assessment = createAssessment(AnimalDaoImplTest.INITIAL_ANIMAL_1_ID);
            assessment.getStudy().setId(study.getId());
            assessment.setIsComplete(false);
            mAssessmentDao.store(assessment);
        }

        final int expectedResult = completeAssessments;
        Collection<Assessment> result = mAssessmentDao.getAssessments(studyId, studyGroupId, null, null, null, null,
            null);

        Assert.assertEquals(result.size(), expectedResult);
    }

    private Assessment createAssessment(Long studyId, Long animalId, String date) throws Exception
    {
        Assessment assessment = createAssessment(animalId);
        assessment.getStudy().setId(studyId);
        assessment.setDate(date);
        return assessment;
    }

    private Assessment createAssessment(Long animalId) throws Exception
    {
        Animal animal = AnimalTestUtils.createAnimal(animalId);
        Assessment assessment = createDefaultAssessment();
        assessment.setAnimal(animal);
        return assessment;
    }

    private Assessment createDefaultAssessment() throws AWNoSuchEntityException, AWAssessmentCreationException
    {
        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(TestConstants.TEST_ASSESSMENT_TEMPLATE_ID);
        return mAssessmentProvider.createAssessment(template);
    }
}
