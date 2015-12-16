package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentReasonDto;

@Stateless
public class AssessmentReasonDtoFactory
{
    public AssessmentReasonDto createAssessmentReasonDto(AssessmentReason reason)
    {
        return new AssessmentReasonDto(reason.getId(), reason.getName());
    }
}
