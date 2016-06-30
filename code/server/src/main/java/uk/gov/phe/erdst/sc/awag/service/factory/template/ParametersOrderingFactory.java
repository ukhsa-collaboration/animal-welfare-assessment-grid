package uk.gov.phe.erdst.sc.awag.service.factory.template;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.dto.assessment.ParametersOrdering;

@Stateless
public class ParametersOrderingFactory
{
    public ParametersOrdering getParameterOrdering(AssessmentTemplate template)
    {
        ParametersOrdering ordering = new ParametersOrdering();

        Set<Long> orderedIds = new LinkedHashSet<>();
        List<AssessmentTemplateParameterFactor> parameterFactors = template.getAssessmentTemplateParameterFactors();

        for (AssessmentTemplateParameterFactor pf : parameterFactors)
        {
            orderedIds.add(pf.getParameter().getId());
        }

        int i = 0;
        for (Long id : orderedIds)
        {
            ordering.add(id, i);
            i++;
        }

        return ordering;
    }
}
