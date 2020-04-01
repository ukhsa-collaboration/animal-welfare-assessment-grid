package uk.gov.phe.erdst.sc.awag.upload.species;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP},
    priority = 1)
public class SpeciesUploadTest
{
    private static final long UPLOAD_SPECIES_FILE_EXPECTED_VALID_LINE_COUNT = 3L;
    private static final String EXPECTED_EXCEPTION_INCORRECT_COLUMNS_REGEX_MSG = "The file format is invalid, expects 3 column[(]s[)] at line 1, import aborted[.]";

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    @Test
    public void testUploadWithSuccessfulData() throws IOException, AWInputValidationException
    {
        FileInputStream fileInputStream = getTestDataFile("UploadSpecies.csv");

        final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
            Constants.Upload.UPLOAD_HEADER_SPECIES_COLUMNS);
        Assert.assertEquals(UPLOAD_SPECIES_FILE_EXPECTED_VALID_LINE_COUNT, csvLines.size());
        fileInputStream.close();
    }

    @Test(expectedExceptions = {AWInputValidationException.class},
        expectedExceptionsMessageRegExp = Constants.Upload.ERR_IMPORT_EMPTY_DATA_FILE)
    public void testUploadEmptyFile() throws IOException, AWInputValidationException
    {
        FileInputStream fileInputStream = getTestDataFile("UploadSpecies_empty_file.csv");

        final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
            Constants.Upload.UPLOAD_HEADER_SPECIES_COLUMNS);
        Assert.assertEquals(true, csvLines.isEmpty());
        fileInputStream.close();
    }

    @Test(expectedExceptions = {AWInputValidationException.class},
        expectedExceptionsMessageRegExp = EXPECTED_EXCEPTION_INCORRECT_COLUMNS_REGEX_MSG)
    public void testUploadWithIncorrectColumnHeaders() throws IOException, AWInputValidationException
    {
        FileInputStream fileInputStream = getTestDataFile("UploadSpecies_failed_incorrect_columns.csv");

        final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
            Constants.Upload.UPLOAD_HEADER_SPECIES_COLUMNS);
        Assert.assertEquals(UPLOAD_SPECIES_FILE_EXPECTED_VALID_LINE_COUNT, csvLines.size());

        fileInputStream.close();
    }

    private FileInputStream getTestDataFile(final String testCSVDataFile) throws IOException
    {
        String testUploadSpeciesDataFile = TestConstants.UPLOAD_TEST_DATA_FOLDER + File.separator + "species"
            + File.separator + testCSVDataFile;
        return new FileInputStream(testUploadSpeciesDataFile);
    }

}
