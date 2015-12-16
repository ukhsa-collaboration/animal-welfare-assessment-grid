package uk.gov.phe.erdst.sc.awag.service.factory.factor;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.dto.FactorDto;

@Stateless
public class FactorDtoFactory
{
    public FactorDto create(Factor factor)
    {
        FactorDto factorDto = new FactorDto();
        factorDto.factorId = factor.getId();
        factorDto.factorName = factor.getName();
        return factorDto;
    }
}
