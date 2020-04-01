package uk.gov.phe.erdst.sc.awag.service.factory.housing;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.webapi.response.housing.AnimalHousingDto;

@Stateless
public class AnimalHousingDtoFactory
{
    public AnimalHousingDto createAnimalHousingDto(AnimalHousing housing)
    {
        return new AnimalHousingDto(housing.getId(), housing.getName());
    }

    public Collection<AnimalHousingDto> createAnimalHousingDtos(Collection<AnimalHousing> housings)
    {
        Collection<AnimalHousingDto> dto = new ArrayList<>(housings.size());

        for (AnimalHousing housing : housings)
        {
            dto.add(createAnimalHousingDto(housing));
        }

        return dto;
    }
}
