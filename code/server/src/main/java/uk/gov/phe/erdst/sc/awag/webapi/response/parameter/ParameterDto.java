package uk.gov.phe.erdst.sc.awag.webapi.response.parameter;

import java.util.ArrayList;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorDto;

public class ParameterDto implements ResponseDto
{
    public Long parameterId;
    public String parameterName;
    public Long parameterDisplayOrderNumber;
    public List<FactorDto> parameterFactors = new ArrayList<FactorDto>();

    public ParameterDto()
    {
    }

}
