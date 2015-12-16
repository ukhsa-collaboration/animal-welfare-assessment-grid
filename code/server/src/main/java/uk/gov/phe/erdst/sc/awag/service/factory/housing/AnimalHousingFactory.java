package uk.gov.phe.erdst.sc.awag.service.factory.housing;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalHousingClientData;

@Stateless
public class AnimalHousingFactory
{
    public AnimalHousing create(AnimalHousingClientData clientData)
    {
        AnimalHousing animalHousing = new AnimalHousing();
        animalHousing.setName(clientData.housingName);
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
