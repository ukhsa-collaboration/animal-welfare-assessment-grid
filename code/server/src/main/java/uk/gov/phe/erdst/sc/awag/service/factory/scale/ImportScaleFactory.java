package uk.gov.phe.erdst.sc.awag.service.factory.scale;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportScale;

@Stateless
public class ImportScaleFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_SCALE_NAME = 1;
    private static int CSV_COLUMN_SCALE_MINIMUM = 2;
    private static int CSV_COLUMN_SCALE_MAXIMUM = 3;

    public ImportScale create(String[] importScaleCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importScaleCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String scaleName = importScaleCSVLineData[CSV_COLUMN_SCALE_NAME];
        final int scaleMinimum = Integer.parseInt(importScaleCSVLineData[CSV_COLUMN_SCALE_MINIMUM].trim());
        final int scaleMaximum = Integer.parseInt(importScaleCSVLineData[CSV_COLUMN_SCALE_MAXIMUM].trim());

        ImportScale importScale = new ImportScale();
        importScale.setLineNumber(lineNumber);
        importScale.setScaleName(scaleName);
        importScale.setMin(scaleMinimum);
        importScale.setMax(scaleMaximum);

        return importScale;
    }

}
