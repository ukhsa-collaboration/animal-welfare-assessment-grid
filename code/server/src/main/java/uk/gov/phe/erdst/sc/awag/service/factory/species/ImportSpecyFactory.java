package uk.gov.phe.erdst.sc.awag.service.factory.species;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportSpecy;

@Stateless
public class ImportSpecyFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_SPECIES_NAME = 1;

    public ImportSpecy create(String[] importSpeciesCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importSpeciesCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String specyName = String.valueOf(importSpeciesCSVLineData[CSV_COLUMN_SPECIES_NAME]);

        ImportSpecy importSpecy = new ImportSpecy();
        importSpecy.setLineNumber(lineNumber);
        importSpecy.setSpeciesName(specyName);
        return importSpecy;
    }

}
