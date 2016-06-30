package uk.gov.phe.erdst.sc.awag.dao.study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class StudyDaoImplTest
{
    public static final Long STUDY_ONE_ID = 10000L;
    private static final String STUDIES_LIKE_TERM = "Stu";
    private static final Long STUDY_TWO_ID = 10001L;
    private static final String STUDY_ONE_NAME = "Study 1";
    private static final String STUDY_FOUR_NAME = "Study 4";
    private static final String STUDY_ONE_NAME_NEW = "New study 2";
    private static final Long STUDY_STUDY_GROUP_1 = 10000L;
    private static final Long STUDY_STUDY_GROUP_2 = 10001L;
    private static final int EXPECTED_STUDIES_LIKE = 3;

    private static final Long ID_ANIMAL_IN_STUDY = 10000L;
    private static final Long ID_ANIMAL_NOT_IN_STUDY = 10002L;

    private StudyDao mStudyDao;
    private AnimalDao mAnimalDao;
    private StudyGroupDao mStudyGroupDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mStudyDao = (StudyDao) GlassfishTestsHelper.lookupMultiInterface("StudyDaoImpl", StudyDao.class);
        mAnimalDao = (AnimalDao) GlassfishTestsHelper.lookupMultiInterface("AnimalDaoImpl", AnimalDao.class);
        mStudyGroupDao = (StudyGroupDao) GlassfishTestsHelper.lookupMultiInterface("StudyGroupDaoImpl",
            StudyGroupDao.class);
    }

    @Test
    public void testGetStudyLike()
    {
        Collection<Study> studies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, null, null);
        Assert.assertNotNull(studies);
        Assert.assertEquals(studies.size(), EXPECTED_STUDIES_LIKE);
    }

    @Test
    public void testGetStudiesLikePagedNoLimit()
    {
        Collection<Study> allStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, null, null);
        Collection<Study> pagedStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allStudies, pagedStudies);
    }

    @Test
    public void testGetStudiesLikePagedNoOffset()
    {
        Collection<Study> allStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, null, null);
        Collection<Study> pagedStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allStudies, pagedStudies);
    }

    @Test
    public void testGetStudiesLikeValidOffsetLimit()
    {
        Collection<Study> allStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, null, null);
        Collection<Study> pagedStudies = mStudyDao.getEntitiesLike(STUDIES_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allStudies, pagedStudies);
    }

    @Test
    public void testStoreValidStudy() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Study study = new Study();
        study.setStudyNumber(STUDY_FOUR_NAME);
        study = mStudyDao.store(study);
        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, study.getId());
        deleteStudy(study);
    }

    @Test
    public void testUpdateStudy() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Study study = mStudyDao.getEntityById(STUDY_ONE_ID);
        study.setStudyNumber(STUDY_ONE_NAME_NEW);
        mStudyDao.update(study);
        study = mStudyDao.getEntityById(STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyNumber(), STUDY_ONE_NAME_NEW);
        study.setStudyNumber(STUDY_ONE_NAME);
        mStudyDao.update(study);
    }

    private Set<StudyGroup> getNoStudyGroupsInDb()
    {
        return new HashSet<StudyGroup>();
    }

    private Set<StudyGroup> getInitStudyGroupsInDb()
    {
        Set<StudyGroup> studyGroups = new HashSet<StudyGroup>();
        studyGroups.add(new StudyGroup(STUDY_STUDY_GROUP_1));
        return studyGroups;
    }

    private Set<StudyGroup> getAddedToStudyGroups()
    {
        Set<StudyGroup> studyGroups = new HashSet<StudyGroup>();
        studyGroups.add(new StudyGroup(STUDY_STUDY_GROUP_1));
        studyGroups.add(new StudyGroup(STUDY_STUDY_GROUP_2));
        return studyGroups;
    }

    @Test
    public void testClearStudyStudyGroups() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Set<StudyGroup> noStudyGroupsInDb = getNoStudyGroupsInDb();
        Set<StudyGroup> initStudyGroupsInDb = getInitStudyGroupsInDb();

        Study study = mStudyDao.getEntityById(STUDY_ONE_ID);
        study.setStudyGroups(noStudyGroupsInDb);
        study = mStudyDao.update(study);
        study = mStudyDao.getEntityById(STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().isEmpty(), true);
        study.setStudyGroups(initStudyGroupsInDb);
        mStudyDao.update(study);
    }

    @Test
    public void testAddStudyStudyGroup() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Set<StudyGroup> addedToStudyGroupsInDb = getAddedToStudyGroups();
        Set<StudyGroup> initStudyGroupsInDb = getInitStudyGroupsInDb();

        Study study = mStudyDao.getEntityById(STUDY_ONE_ID);
        study.setStudyGroups(addedToStudyGroupsInDb);
        mStudyDao.update(study);
        study = mStudyDao.getEntityById(STUDY_ONE_ID);
        Assert.assertEquals(study.getStudyGroups().size(), addedToStudyGroupsInDb.size());
        Assert.assertTrue(study.getStudyGroups().containsAll(addedToStudyGroupsInDb));
        study.setStudyGroups(initStudyGroupsInDb);
        mStudyDao.update(study);
    }

    @Test
    public void testGetStudy() throws AWNoSuchEntityException
    {
        Study study = mStudyDao.getEntityById(STUDY_ONE_ID);
        Assert.assertNotNull(study);
        Assert.assertEquals(study.getId(), STUDY_ONE_ID);
    }

    private void deleteStudy(Study study) throws AWNoSuchEntityException
    {
        mStudyDao.deleteEntityById(study.getId());
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueStudy() throws AWNonUniqueException
    {
        Study study = new Study();
        study.setStudyNumber(STUDY_ONE_NAME);
        mStudyDao.store(study);
    }

    @Test
    public void testGetStudyWithValidAnimal() throws AWNoSuchEntityException, AWMultipleResultException
    {
        Animal animal = mAnimalDao.getAnimal(ID_ANIMAL_IN_STUDY);
        Study study = mStudyDao.getStudyWithAnimal(animal);
        Assert.assertEquals(study.getId(), STUDY_ONE_ID);
    }

    @Test
    public void testGetStudyWithInvalidAnimal() throws AWMultipleResultException
    {
        Animal nonExistentAnimal = null;
        Assert.assertNull(mStudyDao.getStudyWithAnimal(nonExistentAnimal));
    }

    @Test
    public void testGetStudyWithAnimalNoStudyAssigned() throws AWNoSuchEntityException, AWMultipleResultException
    {
        Animal animal = mAnimalDao.getAnimal(ID_ANIMAL_NOT_IN_STUDY);
        Assert.assertNull(mStudyDao.getStudyWithAnimal(animal));
    }

    @Test(expectedExceptions = {AWMultipleResultException.class})
    public void testGetStudyWithAnimalMultipleStudyException() throws AWNoSuchEntityException, AWNonUniqueException,
        AWMultipleResultException
    {
        Animal animal = mAnimalDao.getAnimal(ID_ANIMAL_IN_STUDY);
        Study study = mStudyDao.getEntityById(STUDY_TWO_ID);

        // add study group 1 to study 2 to make an animal in two studies
        StudyGroup group = mStudyGroupDao.getStudyGroup(STUDY_STUDY_GROUP_1);
        study.getStudyGroups().add(group);

        Study updatedStudy = mStudyDao.update(study);

        try
        {
            mStudyDao.getStudyWithAnimal(animal);
        }
        finally
        {
            updatedStudy.setStudyGroups(getNoStudyGroupsInDb());
            mStudyDao.update(updatedStudy);
        }
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
