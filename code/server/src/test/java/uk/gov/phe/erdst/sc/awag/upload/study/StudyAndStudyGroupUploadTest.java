package uk.gov.phe.erdst.sc.awag.upload.study;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

// @Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP, TestConstants.TESTNG_CONTAINER_UPLOAD_TESTS_GROUP},
// priority = 1) // TODO
public class StudyAndStudyGroupUploadTest
{

    // TODO could be property based for similar other tests

    @Test
    public void testUploadWithSuccessfulData() throws IOException
    {

        /*
        // TODO: implement factory conversion of CSV data to the Source Client data
        //
        // convert the CSV data to JSON, then let the SourceClientData take over
        //
        // TODO: Load the from the test-data files from a linked folder
        // TODO: validate the source file header against the meta data names?

        String tempFolder = "C:\\apps\\projects\\dev\\animal-welfare-system\\test-data\\source\\";

        FileInputStream fis = new FileInputStream(new File(tempFolder + "UploadSource.csv"));

        CSVReader reader = new CSVReader(new FileReader(tempFolder + "UploadSource.csv"));
        String[] nextLine;

        nextLine = reader.readNext();
        Assert.assertEquals(nextLine[0], "mname");

        while ((nextLine = reader.readNext()) != null)
        {
            System.out.println(nextLine[0]);

            SourceClientData clientData = new SourceClientData();
            clientData.sourceName = nextLine[0];
            // SourceClientData clientData = (SourceClientData) mRequestConverter
            // .convert(TestConstants.DUMMY_NEW_SOURCE_RAW_DATA, SourceClientData.class);

            Source source = mSourceFactory.create(clientData);
            assertNonIdProperties(source, clientData);

        }
        reader.close();
        */

        Assert.assertEquals(false, true);

    }

    @Test
    public void testUploadWithDuplicates() throws IOException
    {
        Assert.assertEquals(false, true);

    }

    @Test
    public void testUploadWithIncorrectColumnHeaders() throws IOException
    {
        Assert.assertEquals(false, true);

    }

    @Test
    public void testUploadEmptyFile() throws IOException
    {
        Assert.assertEquals(false, true);

    }

    @Test
    public void testUploadTooLongStrings() throws IOException
    {
        Assert.assertEquals(false, true);

    }

}
