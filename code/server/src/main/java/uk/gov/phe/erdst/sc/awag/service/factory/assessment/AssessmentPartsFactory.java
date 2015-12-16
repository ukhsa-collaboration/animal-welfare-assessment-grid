package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;

public interface AssessmentPartsFactory
{

    AssessmentParts create(AssessmentClientData clientData) throws AWAssessmentCreationException;

}
