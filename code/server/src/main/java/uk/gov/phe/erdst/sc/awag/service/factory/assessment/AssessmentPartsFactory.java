package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;

public interface AssessmentPartsFactory
{

    AssessmentParts create(AssessmentClientData clientData) throws AWNoSuchEntityException, AWSeriousException;

    AssessmentParts create(AssessmentClientData clientData, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWSeriousException, AWNonUniqueException;

}
