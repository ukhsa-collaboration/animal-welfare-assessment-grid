package uk.gov.phe.erdst.sc.awag.upload.assessment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP,
        TestConstants.TESTNG_CONTAINER_ASSESSMENT_IMPORT_TESTS_GROUP},
    priority = 1)
public class AssessmentUploadTest
{
    private static final long UPLOAD_ASSESSMENT_FILE_EXPECTED_VALID_LINE_COUNT = 2L;
    private static final String EXPECTED_EXCEPTION_INCORRECT_COLUMNS_REGEX_MSG = "The file format is invalid, expects 3 column[(]s[)] at line 1, import aborted[.]";

    @Test(priority = 1, expectedExceptions = {AWInputValidationException.class},
        expectedExceptionsMessageRegExp = Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT)
    public void testUploadWithInvalidPartialUploadFormat() throws IOException, AWInputValidationException
    {
        try (FileInputStream fileInputStream = getTestDataFile("UploadAssessment.csv"))
        {
            final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
                Constants.Upload.UPLOAD_HEADER_PREFIX_ASSESSMENT_COLUMNS);
            Assert.assertEquals(UPLOAD_ASSESSMENT_FILE_EXPECTED_VALID_LINE_COUNT, csvLines.size());
        }
    }

    @Test(priority = 2)
    public void testUploadWithSuccessfulData() throws IOException, AWInputValidationException
    {
        try (FileInputStream fileInputStream = getTestDataFile("UploadAssessment.csv"))
        {
            final String assessmentCSVHeaderColumn[] = getAssessmentCSVColumnHeaders(4);

            final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
                assessmentCSVHeaderColumn);
            Assert.assertEquals(UPLOAD_ASSESSMENT_FILE_EXPECTED_VALID_LINE_COUNT, csvLines.size());
        }
    }

    @Test(priority = 2)
    public void testUploadWithAssessmentsBasedOnTestBackupData() throws IOException, AWInputValidationException
    {
        // TODO is this named correctly? is the data in the public release?
        try (FileInputStream fileInputStream = getTestDataFile("UploadAssessment-test-backup-data.csv"))
        {
            final String assessmentCSVHeaderColumn[] = getAssessmentCSVColumnHeaders(2);

            final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(fileInputStream,
                assessmentCSVHeaderColumn);
            Assert.assertEquals(UPLOAD_ASSESSMENT_FILE_EXPECTED_VALID_LINE_COUNT, csvLines.size());
        }
    }

    private final String[] getAssessmentCSVColumnHeaders(int columnCount)
    {
        List<String> headerColumn = new ArrayList<>(
            Arrays.asList(Constants.Upload.UPLOAD_HEADER_PREFIX_ASSESSMENT_COLUMNS));
        String suffixHeaderColumns[] = UploadUtils.retrieveAssessmentUploadSuffixHeader(columnCount);
        headerColumn.addAll(Arrays.asList(suffixHeaderColumns));

        String[] assessmentCSVColumnHeaders = headerColumn.toArray(new String[headerColumn.size()]);
        return assessmentCSVColumnHeaders;

    }

    private FileInputStream getTestDataFile(final String testCSVDataFile) throws IOException
    {
        String testUploadAnimalDataFile = TestConstants.UPLOAD_TEST_DATA_FOLDER + File.separator + "assessment"
            + File.separator + testCSVDataFile;
        return new FileInputStream(testUploadAnimalDataFile);
    }

}
