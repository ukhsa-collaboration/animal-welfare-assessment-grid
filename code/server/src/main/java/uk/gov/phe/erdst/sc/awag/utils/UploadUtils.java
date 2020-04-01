package uk.gov.phe.erdst.sc.awag.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.opencsv.CSVReader;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;

public final class UploadUtils
{

    final static String BOOLEAN_TRUE = "TRUE";

    private UploadUtils()
    {

    }

    public static boolean convertToBoolean(final String booleanString) // TODO
    {

        final String upperBooleanString = booleanString.toUpperCase();
        if (BOOLEAN_TRUE.equals(upperBooleanString))
        {
            return true;
        }

        return false;

    }

    // TODO ideally visitor pattern here?? Still relevant? Close the stream?
    public static ArrayList<String[]> retrieveCSVLines(InputStream uploadFile, final String[] expectedHeaderColumns)
        throws AWInputValidationException, IOException
    {
        ArrayList<String[]> reviewCSVLine = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(uploadFile)))
        {
            String[] uploadColumn = null;
            while ((uploadColumn = csvReader.readNext()) != null)
            {
                addUploadCSVLineToList(expectedHeaderColumns, uploadColumn, csvReader, reviewCSVLine);
            }

            if (reviewCSVLine.isEmpty())
            {
                throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_EMPTY_DATA_FILE);
            }
        }

        return reviewCSVLine;
    }

    // TODO needs code test, and naming is less than acceptable
    public static String[] retrieveAssessmentUploadSuffixHeader(final int columnCount)
    {
        ArrayList<String> headerColumn = new ArrayList<>();
        for (int index = 0; index < columnCount; index++)
        {
            final int columnNumber = (index + 1);
            headerColumn.add(String.format("parameter-%d-factor-scored", columnNumber));
            headerColumn.add(String.format("parameter-%d-comment", columnNumber));
        }

        final String[] headerColumns = headerColumn.toArray(new String[headerColumn.size()]);
        return headerColumns;
    }

    // TODO needs code test, and naming is less than acceptable
    public static String[] retrieveTemplateUploadSuffixHeader(final int columnCount)
    {
        ArrayList<String> headerColumn = new ArrayList<>();
        for (int index = 0; index < columnCount; index++)
        {
            final int columnNumber = (index + 1);
            headerColumn.add(String.format("parameter-%d-name", columnNumber));
            headerColumn.add(String.format("parameter-%d-factors", columnNumber));
        }

        final String[] headerColumns = headerColumn.toArray(new String[headerColumn.size()]);
        return headerColumns;
    }

    private static void addUploadCSVLineToList(final String[] expectedHeaderColumns, String[] uploadColumn,
        CSVReader csvReader, ArrayList<String[]> reviewCSVLine) throws AWInputValidationException
    {
        final long expectedColumnLength = expectedHeaderColumns.length;
        long currentLineNumber = csvReader.getLinesRead();

        if (csvReader.getLinesRead() == Constants.Upload.UPLOAD_HEADER_COLUMN_LINE_NUMBER)
        {
            if (!Arrays.equals(expectedHeaderColumns, uploadColumn))
            {
                throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT);
            }
        }

        if (uploadColumn.length > 0 && uploadColumn.length != expectedColumnLength)
        {
            String errMsg = String.format(Constants.Upload.ERR_IMPORT_INVALID_NUMBER_OF_COLUMNS, currentLineNumber,
                expectedColumnLength);
            throw new AWInputValidationException(errMsg);
        }

        if (uploadColumn.length == expectedColumnLength
            && csvReader.getLinesRead() > Constants.Upload.UPLOAD_HEADER_COLUMN_LINE_NUMBER)
        {
            final String[] uploadColumnWithLineNumber = ArrayUtils
                .addAll(new String[] {Long.toString(currentLineNumber)}, uploadColumn);
            reviewCSVLine.add(uploadColumnWithLineNumber);
        }
    }

    private String[] santiseCSVData(String[] csvLineData) // TODO process
    {
        return null;
    }

}
