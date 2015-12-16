package uk.gov.phe.erdst.sc.awag.service.factory.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentTemplateUtils;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.dto.FactorDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterDto;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleDtoFactory;

@Stateless
public class AssessmentTemplateDtoFactory
{
    @Inject
    private ParameterDtoFactory mParameterDtoFactory;

    @Inject
    private FactorDtoFactory mFactorDtoFactory;

    @Inject
    private ScaleDtoFactory mScaleDtoFactory;

    public AssessmentTemplateDto create(AssessmentTemplate template)
    {
        AssessmentTemplateDto templateDto = new AssessmentTemplateDto();

        templateDto.templateId = template.getId();
        templateDto.templateName = template.getName();
        templateDto.templateScale = mScaleDtoFactory.create(template.getScale());

        List<AssessmentTemplateParameterFactor> assessmentTemplateParameterFactors = template
            .getAssessmentTemplateParameterFactors();

        Map<Parameter, Collection<Factor>> mappedParameterFactors = AssessmentTemplateUtils
            .getMappedParameterFactors(assessmentTemplateParameterFactors);

        for (Map.Entry<Parameter, Collection<Factor>> entry : mappedParameterFactors.entrySet())
        {
            ParameterDto parameterDto = mParameterDtoFactory.create(entry.getKey());
            for (Factor factor : entry.getValue())
            {
                FactorDto factorDto = mFactorDtoFactory.create(factor);
                parameterDto.parameterFactors.add(factorDto);
            }
            templateDto.templateParameters.add(parameterDto);
        }

        return templateDto;
    }

    public Collection<AssessmentTemplateDto> create(Collection<AssessmentTemplate> assessmentTemplates)
    {
        Collection<AssessmentTemplateDto> dtos = new ArrayList<AssessmentTemplateDto>(assessmentTemplates.size());
        for (AssessmentTemplate template : assessmentTemplates)
        {
            dtos.add(create(template));
        }

        return dtos;
    }
}
