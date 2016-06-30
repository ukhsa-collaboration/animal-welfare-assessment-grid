package uk.gov.phe.erdst.sc.awag.dao.animal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AnimalDaoImplTest
{
    public static final Long INITIAL_ANIMAL_1_ID = 10000L;
    public static final long INITIAL_ANIMAL_2_ID = 10001L;
    public static final long INITIAL_ANIMAL_3_ID = 10002L;
    public static final long INITIAL_ANIMAL_5_ID = 10004L;
    private static final long TEST_NON_EXISTENT_ANIMAL_ID = -1L;
    private static final int EXPECTED_NO_OF_ANIMALS_RETRIVED = 4;
    private static final String NON_UNIQUE_ANIMAL_NUMBER = "Animal 1";
    private static final String NEW_ANIMAL_ANIMAL_NUMBER = "Animal 5";
    private static final String ANIMAL_DATE_OF_BIRTH = "2012-02-01T00:00:00.000Z";
    private static final long ANIMAL_ASSESSMENT_TEMPLATE_ID = 10000L;
    private static final long ANIMAL_SEX_ID = 10000L;
    private static final long ANIMAL_SPECIES_ID = 10000L;
    private static final long ANIMAL_SOURCE_ID = 10000L;
    private static final int EXPECTED_NO_OF_ANIMALS_IN_GET_MULTIPLE_IDS = 2;
    private static final String ANIMALS_LIKE_TERM = "Anim";
    private static final long EXPECTED_ANIMALS_LIKE = 4L;

    private AnimalDao mAnimalDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAnimalDao = (AnimalDao) GlassfishTestsHelper.lookupMultiInterface("AnimalDaoImpl", AnimalDao.class);
    }

    @Test
    public void testGetAnimals()
    {
        Assert.assertEquals(mAnimalDao.getAnimals().size(), EXPECTED_NO_OF_ANIMALS_RETRIVED);
    }

    @Test
    public void testGetAnimal() throws AWNoSuchEntityException
    {
        Animal animal = mAnimalDao.getAnimal(INITIAL_ANIMAL_1_ID);

        Assert.assertNotNull(animal);
        Assert.assertEquals(animal.getId(), INITIAL_ANIMAL_1_ID);
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentAnimal() throws AWNoSuchEntityException
    {
        Animal animal = mAnimalDao.getAnimal(TEST_NON_EXISTENT_ANIMAL_ID);

        Assert.assertNull(animal);
    }

    @Test
    public void testGetAnimalsByIds()
    {
        List<Long> parentIds = new ArrayList<Long>(2);
        parentIds.add(INITIAL_ANIMAL_1_ID);
        parentIds.add(INITIAL_ANIMAL_2_ID);
        Collection<Animal> animals = mAnimalDao.getAnimals(parentIds);
        Assert.assertEquals(animals.size(), EXPECTED_NO_OF_ANIMALS_IN_GET_MULTIPLE_IDS);
    }

    @Test
    public void testGetAnimalsLike()
    {
        List<Animal> animals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, null, null);
        Assert.assertEquals(animals.size(), EXPECTED_ANIMALS_LIKE);
    }

    @Test
    public void testGetAnimalsLikeCount()
    {
        Long animalsLikeCount = mAnimalDao.getCountNonDeletedAnimalsLike(ANIMALS_LIKE_TERM);
        Assert.assertEquals(animalsLikeCount, Long.valueOf(EXPECTED_ANIMALS_LIKE));
    }

    @Test
    public void testGetAnimalsLikeValidOffsetLimit()
    {
        List<Animal> allAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, null, null);
        List<Animal> pagedAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allAnimals, pagedAnimals);
    }

    @Test
    public void testGetAnimalsLikePagedNoOffest()
    {
        List<Animal> allAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, null, null);

        List<Animal> pagedAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, null,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allAnimals, pagedAnimals);
    }

    @Test
    public void testGetAnimalsLikePagedNoLimit()
    {
        List<Animal> allAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, null, null);
        List<Animal> pagedAnimals = mAnimalDao.getNonDeletedAnimalsLike(ANIMALS_LIKE_TERM, TestConstants.TEST_OFFSET,
            null);
        PageTestAsserter.assertPagedNoLimit(allAnimals, pagedAnimals);
    }

    @Test
    public void testGetNonDeletedAnimalById()
    {
        Animal animal = mAnimalDao.getNonDeletedAnimalById(INITIAL_ANIMAL_1_ID);
        Assert.assertNotNull(animal);
        Assert.assertFalse(animal.isDeleted());
    }

    @Test
    public void testStoreValidAnimal() throws AWNonUniqueException
    {
        Animal animal = AnimalTestUtils.createAnimal(INITIAL_ANIMAL_5_ID, NEW_ANIMAL_ANIMAL_NUMBER,
            ANIMAL_DATE_OF_BIRTH, new Sex(ANIMAL_SEX_ID), new Source(ANIMAL_SOURCE_ID), new Species(ANIMAL_SPECIES_ID),
            new AssessmentTemplate(ANIMAL_ASSESSMENT_TEMPLATE_ID), null, null, true, true);
        animal = mAnimalDao.store(animal);
        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, animal.getId());
        mAnimalDao.realDelete(INITIAL_ANIMAL_5_ID);
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueAnimal() throws AWNonUniqueException
    {
        Animal animal = AnimalTestUtils.createAnimalAutoId(NON_UNIQUE_ANIMAL_NUMBER, ANIMAL_DATE_OF_BIRTH, new Sex(
            ANIMAL_SEX_ID), new Source(ANIMAL_SOURCE_ID), new Species(ANIMAL_SPECIES_ID), new AssessmentTemplate(
            ANIMAL_ASSESSMENT_TEMPLATE_ID), null, null, true, true);
        mAnimalDao.store(animal);
    }

    @Test
    public void testRemoveAnimal() throws AWNoSuchEntityException
    {
        mAnimalDao.updateIsDeleted(INITIAL_ANIMAL_3_ID, true);
        Animal animal = mAnimalDao.getAnimal(INITIAL_ANIMAL_3_ID);

        Assert.assertNotEquals(animal.isDeleted(), false);
        mAnimalDao.updateIsDeleted(INITIAL_ANIMAL_3_ID, false);
    }

    @Test
    public void testUpdateAnimal() throws AWNoSuchEntityException, AWNonUniqueException
    {
        boolean initialAnimalAlive = true;
        Animal animal = mAnimalDao.getAnimal(INITIAL_ANIMAL_2_ID);
        animal.setIsAlive(!initialAnimalAlive);
        animal = mAnimalDao.store(animal);

        Assert.assertNotEquals(initialAnimalAlive, animal.isAlive());
        mAnimalDao.updateIsAlive(INITIAL_ANIMAL_2_ID, initialAnimalAlive);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
