package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentReason;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentReasonClientData;

@Stateless
public class AssessmentReasonFactory
{
    public AssessmentReason create(AssessmentReasonClientData clientData)
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setName(clientData.reasonName);
        return reason;
    }

    public AssessmentReason create(ImportAssessmentReason importReason)
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setName(importReason.getAssessmentReasonName());
        return reason;
    }

    public void update(AssessmentReason reason, AssessmentReasonClientData clientData)
    {
        reason.setId(clientData.reasonId);
        reason.setName(clientData.reasonName);
    }
}
