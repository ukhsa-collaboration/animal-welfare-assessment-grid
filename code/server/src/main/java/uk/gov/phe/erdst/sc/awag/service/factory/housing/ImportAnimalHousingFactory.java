package uk.gov.phe.erdst.sc.awag.service.factory.housing;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimalHousing;

@Stateless
public class ImportAnimalHousingFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_ANIMAL_HOUSING_NAME = 1;

    public ImportAnimalHousing create(String[] importAnimalHousingCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importAnimalHousingCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String animalHousingName = String.valueOf(importAnimalHousingCSVLineData[CSV_COLUMN_ANIMAL_HOUSING_NAME]);

        ImportAnimalHousing importAnimalHousing = new ImportAnimalHousing();
        importAnimalHousing.setLineNumber(lineNumber);
        importAnimalHousing.setAnimalHousingName(animalHousingName);
        return importAnimalHousing;
    }
}
