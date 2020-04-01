package uk.gov.phe.erdst.sc.awag.service.factory.parameter;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterDto;

@Stateless
public class ParameterDtoFactory
{
    public ParameterDto create(Parameter parameter)
    {
        ParameterDto parameterDto = new ParameterDto();
        parameterDto.parameterId = parameter.getId();
        parameterDto.parameterName = parameter.getName();
        parameterDto.parameterDisplayOrderNumber = parameter.getId();
        return parameterDto;
    }

    // TODO this should be being used in AssessmentTemplateDtoFactory and appears to cause an error
    public void update(ParameterDto parameterDto, AssessmentTemplateParameter assessmentTemplateParameter)
    {
        parameterDto.parameterDisplayOrderNumber = assessmentTemplateParameter.getClockwiseDisplayOrderNumber();
    }

    public Collection<ParameterDto> createParameterDtos(Collection<Parameter> parameters)
    {
        Collection<ParameterDto> dto = new ArrayList<>(parameters.size());

        for (Parameter parameter : parameters)
        {
            dto.add(create(parameter));
        }

        return dto;
    }
}
