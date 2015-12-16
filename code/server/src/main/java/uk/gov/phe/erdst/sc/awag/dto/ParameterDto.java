package uk.gov.phe.erdst.sc.awag.dto;

import java.util.ArrayList;
import java.util.List;

public class ParameterDto
{
    public Long parameterId;
    public String parameterName;
    public List<FactorDto> parameterFactors = new ArrayList<FactorDto>();

    public ParameterDto()
    {
    }

}
