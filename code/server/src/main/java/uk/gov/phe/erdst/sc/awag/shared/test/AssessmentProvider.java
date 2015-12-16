package uk.gov.phe.erdst.sc.awag.shared.test;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;

@Stateless
public class AssessmentProvider
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private AssessmentPartsFactory mAssessmentPartsFactory;

    @Inject
    private AssessmentFactory mAssessmentFactory;

    public Assessment createAssessment(AssessmentTemplate template) throws AWAssessmentCreationException
    {
        boolean isComplete = true;
        return createAssessment(template, isComplete);
    }

    public Assessment createAssessment(AssessmentTemplate template, boolean isComplete)
        throws AWAssessmentCreationException
    {
        AssessmentClientData clientData = createClientData();
        AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);

        return mAssessmentFactory.create(clientData, template, assessmentParts, isComplete);
    }

    public AssessmentClientData createClientData()
    {
        return (AssessmentClientData) mRequestConverter.convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA,
            AssessmentClientData.class);
    }
}
