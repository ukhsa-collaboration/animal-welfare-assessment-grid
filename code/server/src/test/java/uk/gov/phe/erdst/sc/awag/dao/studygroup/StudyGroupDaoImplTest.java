package uk.gov.phe.erdst.sc.awag.dao.studygroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class StudyGroupDaoImplTest
{
    public static final Long STUDY_GROUP_ONE_ID = 10000L;
    public static final Long STUDY_GROUP_TWO_ID = 10001L;

    private static final String STUDY_GROUP_LIKE_TERM = "Stu";
    private static final String STUDY_GROUP_ONE_NAME = "Study group 1";
    private static final String STUDY_GROUP_FOUR_NAME = "Study group 4";
    private static final String STUDY_GROUP_ONE_NAME_NEW = "New study group 2";
    private static final Long STUDY_GROUP_ANIMAL_1 = 10000L;
    private static final Long STUDY_GROUP_ANIMAL_2 = 10001L;
    private static final Long STUDY_GROUP_ANIMAL_3 = 10002L;
    private static final int EXPECTED_STUDY_GROUPS_LIKE = 3;

    private StudyGroupDao mStudyGroupDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mStudyGroupDao = (StudyGroupDao) GlassfishTestsHelper.lookupMultiInterface("StudyGroupDaoImpl",
            StudyGroupDao.class);
    }

    @Test
    private void testGetStudyGroupLike()
    {
        Set<StudyGroup> studyGroups = mStudyGroupDao.getStudyGroupsLike(STUDY_GROUP_LIKE_TERM, null, null);
        Assert.assertNotNull(studyGroups);
        Assert.assertEquals(studyGroups.size(), EXPECTED_STUDY_GROUPS_LIKE);
    }

    @Test
    private void testGetStudyGroupLikeCount()
    {
        Long studyGroupsLikeCount = mStudyGroupDao.getCountStudyGroupsLike(STUDY_GROUP_LIKE_TERM);
        Assert.assertEquals(studyGroupsLikeCount, Long.valueOf(EXPECTED_STUDY_GROUPS_LIKE));
    }

    @Test
    public void testGetStudyGroupsLikePagedNoLimit()
    {
        List<StudyGroup> allStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, null, null));
        List<StudyGroup> pagedStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, TestConstants.TEST_OFFSET, null));
        PageTestAsserter.assertPagedNoLimit(allStudyGroups, pagedStudyGroups);
    }

    @Test
    public void testGetStudyGroupsLikePagedNoOffset()
    {
        List<StudyGroup> allStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, null, null));
        List<StudyGroup> pagedStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, null, TestConstants.TEST_LIMIT));
        PageTestAsserter.assertPagedNoOffset(allStudyGroups, pagedStudyGroups);
    }

    @Test
    public void testGetStudyGroupsLikeValidOffsetLimit()
    {
        List<StudyGroup> allStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, null, null));
        List<StudyGroup> pagedStudyGroups = new ArrayList<StudyGroup>(mStudyGroupDao.getStudyGroupsLike(
            STUDY_GROUP_LIKE_TERM, TestConstants.TEST_OFFSET, TestConstants.TEST_LIMIT));
        PageTestAsserter.assertPagedValidOffsetLimit(allStudyGroups, pagedStudyGroups);
    }

    @Test
    private void testStoreValidStudyGroup() throws AWNonUniqueException, AWNoSuchEntityException
    {
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setStudyGroupNumber(STUDY_GROUP_FOUR_NAME);
        studyGroup = mStudyGroupDao.store(studyGroup);
        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, studyGroup.getId());
        deleteStudyGroup(studyGroup);
    }

    @Test
    private void testUpdateStudyGroup() throws AWNonUniqueException, AWNoSuchEntityException
    {
        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        studyGroup.setStudyGroupNumber(STUDY_GROUP_ONE_NAME_NEW);
        mStudyGroupDao.store(studyGroup);
        studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        Assert.assertEquals(studyGroup.getStudyGroupNumber(), STUDY_GROUP_ONE_NAME_NEW);
        studyGroup.setStudyGroupNumber(STUDY_GROUP_ONE_NAME);
        mStudyGroupDao.store(studyGroup);
    }

    private Set<Animal> getNoAnimalsInDb()
    {
        return new HashSet<Animal>();
    }

    private Set<Animal> getInitAnimalsInDb()
    {
        Set<Animal> animals = new HashSet<Animal>();
        animals.add(new Animal(STUDY_GROUP_ANIMAL_1));
        animals.add(new Animal(STUDY_GROUP_ANIMAL_2));
        return animals;
    }

    private Set<Animal> getAddedToAnimals()
    {
        Set<Animal> animals = new HashSet<Animal>();
        animals.add(new Animal(STUDY_GROUP_ANIMAL_1));
        animals.add(new Animal(STUDY_GROUP_ANIMAL_2));
        animals.add(new Animal(STUDY_GROUP_ANIMAL_3));
        return animals;
    }

    @Test
    private void testClearStudyGroupAnimals() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Set<Animal> noAnimalsInDb = getNoAnimalsInDb();
        Set<Animal> initAnimalsInDb = getInitAnimalsInDb();

        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        studyGroup.setAnimals(noAnimalsInDb);
        mStudyGroupDao.store(studyGroup);
        studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        Assert.assertEquals(studyGroup.getAnimals().isEmpty(), true);
        studyGroup.setAnimals(initAnimalsInDb);
        mStudyGroupDao.store(studyGroup);
    }

    @Test
    private void testAddStudyGroupAnimals() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Set<Animal> addedToAnimals = getAddedToAnimals();
        Set<Animal> initAnimals = getInitAnimalsInDb();

        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        studyGroup.setAnimals(addedToAnimals);
        mStudyGroupDao.store(studyGroup);
        studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        Assert.assertEquals(studyGroup.getAnimals().size(), addedToAnimals.size());
        Assert.assertTrue(studyGroup.getAnimals().containsAll(addedToAnimals));
        studyGroup.setAnimals(initAnimals);
        mStudyGroupDao.store(studyGroup);
    }

    @Test
    private void testGetStudyGroup() throws AWNoSuchEntityException
    {
        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(STUDY_GROUP_ONE_ID);
        Assert.assertNotNull(studyGroup);
        Assert.assertEquals(studyGroup.getId(), STUDY_GROUP_ONE_ID);
    }

    private void deleteStudyGroup(StudyGroup studyGroup) throws AWNoSuchEntityException
    {
        mStudyGroupDao.deleteStudyGroupById(studyGroup.getId());
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    private void testStoreNonUniqueStudyGroup() throws AWNonUniqueException
    {
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setStudyGroupNumber(STUDY_GROUP_ONE_NAME);
        mStudyGroupDao.store(studyGroup);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
