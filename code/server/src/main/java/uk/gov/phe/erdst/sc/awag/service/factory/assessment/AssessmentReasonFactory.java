package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;

@Stateless
public class AssessmentReasonFactory
{
    public AssessmentReason create(AssessmentReasonClientData clientData)
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setName(clientData.reasonName);
        return reason;
    }

    public void update(AssessmentReason reason, AssessmentReasonClientData clientData)
    {
        reason.setId(clientData.reasonId);
        reason.setName(clientData.reasonName);
    }
}
