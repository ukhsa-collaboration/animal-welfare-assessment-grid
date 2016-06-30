package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.UniqueEntitySelectCollectionDto;

// CS:OFF: VisibilityModifier
public class AssessmentsDynamicSearchDto
{
    public Collection<EntitySelectDto> assessments;
    public UniqueEntitySelectCollectionDto filterValues;
    public String dateOfFirstAssessment;
    public String dateOfLastAssessment;
}
// CS:ON
