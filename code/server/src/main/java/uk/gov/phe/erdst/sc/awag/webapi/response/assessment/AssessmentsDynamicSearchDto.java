package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.UniqueEntitySelectCollectionDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class AssessmentsDynamicSearchDto implements ResponseDto
{
    public Collection<AssessmentMinimalDto> assessments;
    public UniqueEntitySelectCollectionDto filterValues;
    public String dateOfFirstAssessment;
    public String dateOfLastAssessment;
}
