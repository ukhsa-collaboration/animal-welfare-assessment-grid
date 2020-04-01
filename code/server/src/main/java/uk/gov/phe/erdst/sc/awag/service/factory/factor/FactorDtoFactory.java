package uk.gov.phe.erdst.sc.awag.service.factory.factor;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorDto;

@Stateless
public class FactorDtoFactory
{
    public FactorDto create(Factor factor)
    {
        FactorDto factorDto = new FactorDto();
        factorDto.factorId = factor.getId();
        factorDto.factorName = factor.getName();
        factorDto.factorDescription = factor.getFactorDescription();
        return factorDto;
    }

    public Collection<FactorDto> createFactorDtos(Collection<Factor> scoringFactors)
    {
        Collection<FactorDto> dto = new ArrayList<>(scoringFactors.size());

        for (Factor factor : scoringFactors)
        {
            dto.add(create(factor));
        }

        return dto;
    }
}
