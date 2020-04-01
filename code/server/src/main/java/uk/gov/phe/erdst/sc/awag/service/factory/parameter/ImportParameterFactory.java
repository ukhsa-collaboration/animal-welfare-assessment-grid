package uk.gov.phe.erdst.sc.awag.service.factory.parameter;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportParameter;

@Stateless
public class ImportParameterFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_PARAMETER_NAME = 1;

    public ImportParameter create(String[] importParameterCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importParameterCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String parameterName = String.valueOf(importParameterCSVLineData[CSV_COLUMN_PARAMETER_NAME]);

        ImportParameter importParameter = new ImportParameter();
        importParameter.setLineNumber(lineNumber);
        importParameter.setParameterName(parameterName);
        return importParameter;
    }

}
