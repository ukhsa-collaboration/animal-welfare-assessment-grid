package uk.gov.phe.erdst.sc.awag.webapi.response.template;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class AssessmentTemplatesDto implements ResponseDto
{
    public Collection<AssessmentTemplateDto> templates;
    public PagingInfo pagingInfo;
}
