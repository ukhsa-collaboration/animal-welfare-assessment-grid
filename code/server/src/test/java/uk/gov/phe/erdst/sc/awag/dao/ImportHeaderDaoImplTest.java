package uk.gov.phe.erdst.sc.awag.dao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.utils.ImportTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP})
public class ImportHeaderDaoImplTest
{
    private ImportHeaderDao mImportHeaderDao;

    private Long initialImportAnimalHeaderId = null;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mImportHeaderDao = (ImportHeaderDao) GlassfishTestsHelper.lookupMultiInterface("ImportHeaderDaoImpl",
            ImportHeaderDao.class);
    }

    @Test(priority = 1)
    public void testStoreImportHeader() throws AWNonUniqueException
    {
        ImportHeader importHeader = ImportTestUtils.createDummyImportHeader(TestConstants.INITIAL_USER_1_NAME,
            "DEFAULT");
        importHeader = mImportHeaderDao.store(importHeader);
        initialImportAnimalHeaderId = importHeader.getImportheaderid();
        Assert.assertEquals(true, (initialImportAnimalHeaderId > 0));
    }

    @Test(priority = 2)
    public void testRemoveImportHeader()
    {
        mImportHeaderDao.realDelete(initialImportAnimalHeaderId);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

}
