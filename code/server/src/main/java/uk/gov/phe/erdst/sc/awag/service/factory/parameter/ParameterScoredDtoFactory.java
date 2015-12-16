package uk.gov.phe.erdst.sc.awag.service.factory.parameter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.dto.FactorScoredDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorScoredDtoFactory;

@Stateless
public class ParameterScoredDtoFactory
{
    @Inject
    private FactorScoredDtoFactory mFactorScoredDtoFactory;

    public ParameterScoredDto createParameterDto(ParameterScore parameterScore)
    {
        if (parameterScore.getParameterScored() != null)
        {
            Long parameterId = parameterScore.getParameterScored().getId();
            String parameterName = parameterScore.getParameterScored().getName();
            String parameterComment = parameterScore.getComment();
            Double parameterAverage = parameterScore.getAverageScore();

            Collection<FactorScored> scoringFactorsScored = parameterScore.getScoringFactorsScored();

            Set<FactorScoredDto> factorDtos = new LinkedHashSet<FactorScoredDto>();

            if (scoringFactorsScored != null)
            {
                for (FactorScored scoringFactorScored : scoringFactorsScored)
                {
                    FactorScoredDto factorDto = mFactorScoredDtoFactory.createFactorDto(scoringFactorScored);
                    factorDtos.add(factorDto);
                }
            }

            ParameterScoredDto parameterDto = new ParameterScoredDto(parameterId, parameterName, parameterAverage,
                parameterComment, factorDtos);
            return parameterDto;
        }
        else
        {
            return new ParameterScoredDto();
        }

    }
}
