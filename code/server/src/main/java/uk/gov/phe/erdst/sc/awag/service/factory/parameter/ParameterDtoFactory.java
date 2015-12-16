package uk.gov.phe.erdst.sc.awag.service.factory.parameter;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.dto.ParameterDto;

@Stateless
public class ParameterDtoFactory
{
    public ParameterDto create(Parameter parameter)
    {
        ParameterDto parameterDto = new ParameterDto();
        parameterDto.parameterId = parameter.getId();
        parameterDto.parameterName = parameter.getName();
        return parameterDto;
    }
}
