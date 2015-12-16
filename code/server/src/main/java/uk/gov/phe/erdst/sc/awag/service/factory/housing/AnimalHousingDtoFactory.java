package uk.gov.phe.erdst.sc.awag.service.factory.housing;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.dto.AnimalHousingDto;

@Stateless
public class AnimalHousingDtoFactory
{
    public AnimalHousingDto createAnimalHousingDto(AnimalHousing housing)
    {
        return new AnimalHousingDto(housing.getId(), housing.getName());
    }
}
