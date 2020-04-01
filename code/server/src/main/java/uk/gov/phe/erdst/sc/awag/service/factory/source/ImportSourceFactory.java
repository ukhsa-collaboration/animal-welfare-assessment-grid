package uk.gov.phe.erdst.sc.awag.service.factory.source;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportSource;

@Stateless
public class ImportSourceFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_SOURCE_NAME = 1;

    public ImportSource create(String[] importSourceCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importSourceCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String sourceName = importSourceCSVLineData[CSV_COLUMN_SOURCE_NAME];

        ImportSource importSource = new ImportSource();
        importSource.setLineNumber(lineNumber);
        importSource.setSourceName(sourceName);
        return importSource;
    }

}
