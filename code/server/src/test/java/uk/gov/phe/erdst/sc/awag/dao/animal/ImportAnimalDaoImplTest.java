package uk.gov.phe.erdst.sc.awag.dao.animal;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.ImportAnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.utils.ImportTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP,
        TestConstants.TESTNG_CONTAINER_ANIMAL_IMPORT_TESTS_GROUP})
public class ImportAnimalDaoImplTest
{
    private static final String TEST_IMPORT_ANIMAL_NUMBER_1 = "Import Animal Number 1";
    private static final String TEST_IMPORT_ANIMAL_NUMBER_2 = "Import Animal Number 2";
    private static final String TEST_IMPORT_ANIMAL_NUMBER_3 = "Import Animal Number 3";
    private static final String DEFAULT_ACTION = "DEFAULT";

    private ImportHeaderDao mImportHeaderDao;

    private ImportAnimalDao mImportAnimalDao;

    private Long mFirstHeaderImportHeaderId;

    private Long mSecondHeaderImportHeaderId;

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
        mImportAnimalDao = (ImportAnimalDao) GlassfishTestsHelper.lookupMultiInterface("ImportAnimalDaoImpl",
            ImportAnimalDao.class);
    }

    @Test(priority = 1)
    public void testStoreImportAnimal() throws AWNonUniqueException
    {
        // Add the first import
        ImportHeader importHeader1 = ImportTestUtils.createDummyImportHeader(TestConstants.INITIAL_USER_1_NAME,
            DEFAULT_ACTION);
        ImportAnimal importAnimal3 = ImportTestUtils.createDummyAnimal(TEST_IMPORT_ANIMAL_NUMBER_3);
        importHeader1.addImportAnimal(importAnimal3);
        importHeader1 = mImportHeaderDao.store(importHeader1);
        mFirstHeaderImportHeaderId = importHeader1.getImportheaderid();

        // Add a second import
        ImportHeader importHeader2 = ImportTestUtils.createDummyImportHeader(TestConstants.INITIAL_USER_2_NAME,
            DEFAULT_ACTION);
        ImportAnimal importAnimal1 = ImportTestUtils.createDummyAnimal(TEST_IMPORT_ANIMAL_NUMBER_1);
        ImportAnimal importAnimal2 = ImportTestUtils.createDummyAnimal(TEST_IMPORT_ANIMAL_NUMBER_2);
        importHeader2.addImportAnimal(importAnimal1);
        importHeader2.addImportAnimal(importAnimal2);
        importHeader2 = mImportHeaderDao.store(importHeader2);
        mSecondHeaderImportHeaderId = importHeader2.getImportheaderid();
        Assert.assertEquals(true, (mSecondHeaderImportHeaderId > 0));
        Assert.assertEquals(2, importHeader2.getImportAnimals().size());
        Assert.assertNotEquals(mFirstHeaderImportHeaderId, mSecondHeaderImportHeaderId);
    }

    @Test(priority = 2)
    public void testGetImportAnimal() throws AWNoSuchEntityException
    {
        ImportAnimal importAnimal = mImportAnimalDao.getImportAnimal(TEST_IMPORT_ANIMAL_NUMBER_2,
            mSecondHeaderImportHeaderId);
        Assert.assertEquals(importAnimal.getAnimalNumber(), TEST_IMPORT_ANIMAL_NUMBER_2);
        Assert.assertEquals(true, (importAnimal.getImportanimalid() > 0));
        Assert.assertEquals(importAnimal.getImportHeader().getImportheaderid(), mSecondHeaderImportHeaderId);
    }

    @Test(priority = 3)
    public void testRemoveImportAnimal()
    {
        mImportHeaderDao.realDelete(mSecondHeaderImportHeaderId);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
