package uk.gov.phe.erdst.sc.awag.service.factory.housing;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimalHousing;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalHousingClientData;

@Stateless
public class AnimalHousingFactory
{
    public AnimalHousing create(AnimalHousingClientData clientData)
    {
        AnimalHousing animalHousing = new AnimalHousing();
        animalHousing.setName(clientData.housingName);
        return animalHousing;
    }

    public AnimalHousing create(ImportAnimalHousing importAnimalHousing)
    {
        AnimalHousing animalHousing = new AnimalHousing();
        animalHousing.setName(importAnimalHousing.getAnimalHousingName());
        return animalHousing;
    }

    public void update(AnimalHousing animalHousing, AnimalHousingClientData clientData)
    {
        if (animalHousing != null)
        {
            animalHousing.setId(clientData.housingId);
            animalHousing.setName(clientData.housingName);
        }
    }
}
