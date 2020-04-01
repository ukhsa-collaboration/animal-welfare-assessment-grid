package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.webapi.response.reason.AssessmentReasonDto;

@Stateless
public class AssessmentReasonDtoFactory
{
    public AssessmentReasonDto createAssessmentReasonDto(AssessmentReason reason)
    {
        return new AssessmentReasonDto(reason.getId(), reason.getName());
    }

    public Collection<AssessmentReasonDto> createAssessmentReasonDtos(Collection<AssessmentReason> reasons)
    {
        Collection<AssessmentReasonDto> dto = new ArrayList<>(reasons.size());

        for (AssessmentReason reason : reasons)
        {
            dto.add(createAssessmentReasonDto(reason));
        }

        return dto;
    }
}
