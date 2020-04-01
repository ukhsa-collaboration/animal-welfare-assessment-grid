package uk.gov.phe.erdst.sc.awag.service.factory.factor;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportFactor;

@Stateless
public class ImportFactorFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_FACTOR_NAME = 1;
    private static int CSV_COLUMN_FACTOR_DESCRIPTION = 2;

    public ImportFactor create(String[] importFactorCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importFactorCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String factorName = String.valueOf(importFactorCSVLineData[CSV_COLUMN_FACTOR_NAME]);
        final String factorDescription = String.valueOf(importFactorCSVLineData[CSV_COLUMN_FACTOR_DESCRIPTION]);

        ImportFactor importFactor = new ImportFactor();
        importFactor.setLineNumber(lineNumber);
        importFactor.setFactorName(factorName);
        importFactor.setFactorDescription(factorDescription);
        return importFactor;
    }

}
