package uk.gov.phe.erdst.sc.awag.dao.species;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.SpeciesDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class SpeciesDaoImplTest
{
    public static final Long SPECIES_ONE_ID = 10000L;
    private static final String SPECIES_LIKE_TERM = "Spe";
    private static final int EXPECTED_SPECIES_LIKE = 3;
    private static final String SPECIES_ONE_NAME = "Species 1";
    private static final String SPECIES_ONE_NEW_NAME = "Jonny 5";
    private static final String SPECIES_FOUR_NAME = "Species 4";

    private SpeciesDao mSpeciesDao;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mSpeciesDao = (SpeciesDao) GlassfishTestsHelper.lookupMultiInterface("SpeciesDaoImpl", SpeciesDao.class);
    }

    @Test
    public void testGetSpeciesLike()
    {
        List<Species> species = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, null, null);
        Assert.assertEquals(species.size(), EXPECTED_SPECIES_LIKE);
    }

    @Test
    public void testGetSpeciesLikePagedNoLimit()
    {
        List<Species> allSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, null, null);
        List<Species> pagedSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allSpecies, pagedSpecies);
    }

    @Test
    public void testGetSourcesLikePagedNoOffset()
    {
        List<Species> allSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, null, null);
        List<Species> pagedSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allSpecies, pagedSpecies);
    }

    @Test
    public void testGetSourcesLikeValidOffsetLimit()
    {
        List<Species> allSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, null, null);
        List<Species> pagedSpecies = mSpeciesDao.getSpeciesLike(SPECIES_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allSpecies, pagedSpecies);
    }

    @Test
    public void testStoreValidSpecies() throws AWNonUniqueException
    {
        Species species = new Species();
        species.setName(SPECIES_FOUR_NAME);
        species = mSpeciesDao.store(species);

        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, species.getId());
        deleteSpecies(species);
    }

    private void deleteSpecies(Species species)
    {
        if (species.getId() != null)
        {
            mSpeciesDao.realDelete(species.getId());
        }
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueSpecies() throws AWNonUniqueException
    {
        Species species = new Species();
        species.setName(SPECIES_ONE_NAME);
        mSpeciesDao.store(species);
    }

    @Test
    public void testRemoveSpecies() throws AWNoSuchEntityException
    {
        mSpeciesDao.updateIsDeleted(SPECIES_ONE_ID, true);
        Species species = mSpeciesDao.getSpecies(SPECIES_ONE_ID);

        Assert.assertNotEquals(species.isDeleted(), false);
        mSpeciesDao.updateIsDeleted(SPECIES_ONE_ID, false);
    }

    @Test
    public void testUpdateSpecies() throws AWNoSuchEntityException, AWNonUniqueException
    {
        Species species = mSpeciesDao.getSpecies(SPECIES_ONE_ID);
        species.setName(SPECIES_ONE_NEW_NAME);
        species = mSpeciesDao.store(species);
        Assert.assertNotEquals(SPECIES_ONE_NAME, species.getName());
        species.setName(SPECIES_ONE_NAME);
        mSpeciesDao.store(species);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
