package uk.gov.phe.erdst.sc.awag.dto;

import java.util.ArrayList;
import java.util.List;

public class AssessmentTemplateDto
{
    public Long templateId;
    public String templateName;
    public ScaleDto templateScale;
    public List<ParameterDto> templateParameters = new ArrayList<ParameterDto>();
}
