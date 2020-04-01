package uk.gov.phe.erdst.sc.awag.service.factory.species;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.webapi.response.species.SpeciesDto;

@Stateless
public class SpeciesDtoFactory
{
    public SpeciesDto create(Species species)
    {
        SpeciesDto dto = new SpeciesDto();
        dto.speciesId = species.getId();
        dto.speciesName = species.getName();
        return dto;
    }

    public Collection<SpeciesDto> createSpeciesDtos(Collection<Species> speciesMany)
    {
        Collection<SpeciesDto> dto = new ArrayList<>(speciesMany.size());

        for (Species species : speciesMany)
        {
            dto.add(create(species));
        }

        return dto;
    }
}
