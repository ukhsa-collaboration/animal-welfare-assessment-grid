package uk.gov.phe.erdst.sc.awag.dao.source;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.PageTestAsserter;
import uk.gov.phe.erdst.sc.awag.dao.SourceDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class SourceDaoImplTest
{
    public static final long SOURCE_ONE_ID = 10000L;
    private static final String SOURCES_LIKE_TERM = "Sou";
    private static final int EXPECTED_SOURCES_LIKE = 3;
    private static final String SOURCE_ONE_NAME = "Source 1";
    private static final String SOURCE_ONE_NEW_NAME = "Source 1 new";
    private static final String SOURCE_FOUR_NAME = "Source 4";

    private SourceDao mSourceDao;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mSourceDao = (SourceDao) GlassfishTestsHelper.lookupMultiInterface("SourceDaoImpl", SourceDao.class);
    }

    @Test
    public void testGetSourcesLike()
    {
        List<Source> sources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, null, null);
        Assert.assertEquals(sources.size(), EXPECTED_SOURCES_LIKE);
    }

    @Test
    public void testGetSourcesLikePagedNoLimit()
    {
        List<Source> allSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, null, null);
        List<Source> pagedSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, TestConstants.TEST_OFFSET, null);

        PageTestAsserter.assertPagedNoLimit(allSources, pagedSources);
    }

    @Test
    public void testGetSourcesLikePagedNoOffset()
    {
        List<Source> allSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, null, null);
        List<Source> pagedSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, null, TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedNoOffset(allSources, pagedSources);
    }

    @Test
    public void testGetSourcesLikeValidOffsetLimit()
    {
        List<Source> allSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, null, null);
        List<Source> pagedSources = mSourceDao.getSourcesLike(SOURCES_LIKE_TERM, TestConstants.TEST_OFFSET,
            TestConstants.TEST_LIMIT);

        PageTestAsserter.assertPagedValidOffsetLimit(allSources, pagedSources);
    }

    @Test
    public void testStoreValidSource() throws AWNonUniqueException
    {
        Source source = new Source();
        source.setName(SOURCE_FOUR_NAME);
        source = mSourceDao.store(source);
        Assert.assertNotEquals(TestConstants.NON_PERSISTED_ID, source.getId());
        deleteSource(source);
    }

    private void deleteSource(Source source)
    {
        if (source.getId() != null)
        {
            mSourceDao.realDelete(source.getId());
        }
    }

    @Test(expectedExceptions = {AWNonUniqueException.class})
    public void testStoreNonUniqueSource() throws AWNonUniqueException
    {
        Source source = new Source();
        source.setName(SOURCE_ONE_NAME);
        mSourceDao.store(source);
    }

    @Test
    public void testUpdateSource() throws AWNonUniqueException, AWNoSuchEntityException
    {
        Source source = mSourceDao.getSource(SOURCE_ONE_ID);
        source.setName(SOURCE_ONE_NEW_NAME);
        source = mSourceDao.store(source);
        Assert.assertNotEquals(SOURCE_ONE_NAME, source.getName());
        source.setName(SOURCE_ONE_NAME);
        source = mSourceDao.store(source);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
