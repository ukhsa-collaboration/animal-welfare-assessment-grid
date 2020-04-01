package uk.gov.phe.erdst.sc.awag.service.factory.template;

import java.util.List;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.ParametersOrdering;

@Stateless
public class ParametersOrderingFactory
{
    public ParametersOrdering getParameterOrdering(AssessmentTemplate template)
    {
        List<AssessmentTemplateParameter> parameters = template.getAssessmentTemplateParameters();

        ParametersOrdering ordering = new ParametersOrdering();

        int displayOrderIndex = 0; // TODO move into class ParametersOrdering
        for (AssessmentTemplateParameter parameter : parameters)
        {
            ordering.add(parameter.getId().getParameterId(), displayOrderIndex++);
        }

        return ordering;
    }
}
