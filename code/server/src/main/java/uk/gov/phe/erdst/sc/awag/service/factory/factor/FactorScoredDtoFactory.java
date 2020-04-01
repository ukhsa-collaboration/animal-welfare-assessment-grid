package uk.gov.phe.erdst.sc.awag.service.factory.factor;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorScoredDto;

@Stateless
public class FactorScoredDtoFactory
{

    public FactorScoredDto createFactorDto(FactorScored factorScored)
    {
        Factor factor = factorScored.getScoringFactor();

        if (factor == null)
        {
            return new FactorScoredDto();
        }

        Long factorId = factor.getId();
        String factorName = factor.getName();
        Long factorScore = Long.valueOf(factorScored.getScore());
        boolean isIgnored = factorScored.isIgnored();

        return new FactorScoredDto(factorId, factorName, factorScore, isIgnored);
    }
}
