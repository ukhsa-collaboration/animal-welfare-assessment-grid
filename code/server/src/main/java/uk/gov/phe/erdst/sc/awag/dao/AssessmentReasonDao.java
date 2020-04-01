package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface AssessmentReasonDao extends CommonDao<AssessmentReason>
{
    void upload(Collection<AssessmentReason> assessmentReasons) throws AWNonUniqueException;
}
