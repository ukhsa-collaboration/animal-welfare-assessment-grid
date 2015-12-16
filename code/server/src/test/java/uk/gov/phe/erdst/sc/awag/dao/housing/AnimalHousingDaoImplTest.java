package uk.gov.phe.erdst.sc.awag.dao.housing;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AnimalHousingDaoImplTest
{
    private static final Long HOUSING_ONE_ID = 10000L;
    private static final String HOUSING_LIKE_TERM = "Hous";
    private static final int EXPECTED_HOUSING_LIKE = 3;
    private static final String HOUSING_ONE_NAME = "Housing 1";
    private static final String HOUSING_ONE_NEW_NAME = "Housing 1 new";
    private static final String HOUSING_FOUR_NAME = "Housing 4";
    private static final Long NON_EXISTENT_HOUSING = 10004L;

    private AnimalHousingDao mAnimalHousingDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAnimalHousingDao = (AnimalHousingDao) GlassfishTestsHelper.lookupMultiInterface("AnimalHousingDaoImpl",
            AnimalHousingDao.class);
    }

    @Test
    public void testGetAnimalHousingLike()
    {
        List<AnimalHousing> housings = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM, null, null);
        Assert.assertEquals(housings.size(), EXPECTED_HOUSING_LIKE);
    }

    @Test
    public void testGetAnimalHousingLikePagedNoLimit()
    {
        List<AnimalHousing> allHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM, null, null);
        List<AnimalHousing> pagedHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM,
            TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allHousing, pagedHousing);
    }

    @Test
    public void testGetAnimalHousingLikePagedNoOffset()
    {
        List<AnimalHousing> allHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM, null, null);
        List<AnimalHousing> pagedHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM, null,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allHousing, pagedHousing);
    }

    @Test
    public void testGetAnimalHousingLikeValidOffsetLimit()
    {
        List<AnimalHousing> allHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM, null, null);
        List<AnimalHousing> pagedHousing = mAnimalHousingDao.getEntitiesLike(HOUSING_LIKE_TERM,
            TestConstants.TEST_OFFSET, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allHousing, pagedHousing);
    }

    @Test
    public void testStoreValidAnimalHousing() throws AWNonUniqueException, AWNoSuchEntityException
    {
        AnimalHousing animalHousing = new AnimalHousing();
        animalHousing.setName(HOUSING_FOUR_NAME);
        animalHousing = mAnimalHousingDao.store(animalHousing);

        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, animalHousing.getId());

        mAnimalHousingDao.deleteEntityByNameField(animalHousing.getName());
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueAnimalHousing() throws AWNonUniqueException, AWNoSuchEntityException
    {
        AnimalHousing housing = create(HOUSING_ONE_NAME);
        mAnimalHousingDao.store(housing);
    }

    @Test
    public void testUpdateAnimalHousing() throws AWNoSuchEntityException, AWNonUniqueException
    {
        AnimalHousing animalHousing = mAnimalHousingDao.getEntityById(HOUSING_ONE_ID);
        animalHousing.setName(HOUSING_ONE_NEW_NAME);
        animalHousing = mAnimalHousingDao.update(animalHousing);

        AnimalHousing updatedHousing = mAnimalHousingDao.getEntityById(HOUSING_ONE_ID);
        Assert.assertEquals(updatedHousing.getName(), HOUSING_ONE_NEW_NAME);

        mAnimalHousingDao.deleteEntityByNameField(HOUSING_ONE_NEW_NAME);
    }

    @Test
    public void testGetAnimalHousing() throws AWNoSuchEntityException
    {
        AnimalHousing animalHousing = mAnimalHousingDao.getEntityById(HOUSING_ONE_ID);
        Assert.assertNotNull(animalHousing);
    }

    @Test(expectedExceptions = {AWNoSuchEntityException.class})
    public void testGetNonExistentAnimalHousing() throws AWNoSuchEntityException
    {
        mAnimalHousingDao.getEntityById(NON_EXISTENT_HOUSING);
    }

    private static AnimalHousing create(String name)
    {
        AnimalHousing housing = new AnimalHousing();
        housing.setName(name);
        return housing;
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
