package uk.gov.phe.erdst.sc.awag.dao.assessment;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessment;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.utils.ImportTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP,
        TestConstants.TESTNG_CONTAINER_ASSESSMENT_IMPORT_TESTS_GROUP})
public class ImportAssessmentDaoImplTest
{
    private static final String DEFAULT_ACTION = "DEFAULT";
    private static final String TEST_ANIMAL_NUMBER_1 = "Animal 1";

    private ImportHeaderDao importHeaderDao;

    private Long importHeaderId = null;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        importHeaderDao = (ImportHeaderDao) GlassfishTestsHelper.lookupMultiInterface("ImportHeaderDaoImpl",
            ImportHeaderDao.class);
    }

    @Test(priority = 1)
    public void testStoreImportAssessment() throws AWNonUniqueException
    {
        ImportHeader importHeader = ImportTestUtils.createDummyImportHeader(TestConstants.INITIAL_USER_1_NAME,
            DEFAULT_ACTION);

        ImportAssessment importAssessment = ImportTestUtils
            .createDummyAssessmentForAnimal(TestConstants.TEST_ASSESSMENT_TEMPLATE_3_ID, TEST_ANIMAL_NUMBER_1);
        importHeader.addImportAssessment(importAssessment);
        importHeader = importHeaderDao.store(importHeader);
        importHeaderId = importHeader.getImportheaderid();
    }

    @Test(priority = 2)
    public void testRemoveImportAssessment()
    {
        importHeaderDao.realDelete(importHeaderId);
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }
}
