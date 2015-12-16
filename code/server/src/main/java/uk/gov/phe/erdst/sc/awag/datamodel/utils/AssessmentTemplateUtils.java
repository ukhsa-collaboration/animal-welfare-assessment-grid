package uk.gov.phe.erdst.sc.awag.datamodel.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;

public final class AssessmentTemplateUtils
{
    private AssessmentTemplateUtils()
    {
    }

    public static Map<Parameter, Collection<Factor>>
        getMappedParameterFactors(Collection<AssessmentTemplateParameterFactor> parameterFactors)
    {
        Map<Parameter, Collection<Factor>> parameterFactorMap = new HashMap<Parameter, Collection<Factor>>();
        if (parameterFactors != null)
        {
            for (AssessmentTemplateParameterFactor parameterFactor : parameterFactors)
            {
                Parameter parameter = parameterFactor.getParameter();
                Factor factor = parameterFactor.getFactor();
                Collection<Factor> factors = parameterFactorMap.get(parameter);

                if (factors == null)
                {
                    factors = new ArrayList<Factor>();
                    factors.add(factor);
                    parameterFactorMap.put(parameter, factors);
                }
                else
                {
                    factors.add(factor);
                }
            }
        }
        return parameterFactorMap;
    }
}
