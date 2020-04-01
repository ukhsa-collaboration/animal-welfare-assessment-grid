package uk.gov.phe.erdst.sc.awag.webapi.response.template;

import java.util.ArrayList;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.scale.ScaleDto;

public class AssessmentTemplateDto implements ResponseDto
{
    public Long templateId;
    public String templateName;
    public ScaleDto templateScale;
    public List<ParameterDto> templateParameters = new ArrayList<ParameterDto>();
    public boolean isAllowZeroScores;
}
