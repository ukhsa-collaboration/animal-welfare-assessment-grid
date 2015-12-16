package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;

@Stateless
public class AssessmentFactory
{
    @Inject
    private AssessmentScoreFactory mAssessmentScoreFactory;

    public Assessment create(AssessmentClientData clientData, AssessmentTemplate template,
        AssessmentParts assessmentParts, boolean isComplete)
    {
        Assessment assessment = new Assessment();
        assessment.setIsComplete(isComplete);
        setRawDataBasedNonIdProperties(assessment, template, clientData);
        setPartsBasedProperties(assessment, assessmentParts);
        return assessment;
    }

    private void setRawDataBasedNonIdProperties(Assessment assessment, AssessmentTemplate template,
        AssessmentClientData clientData)
    {
        assessment.setDate(clientData.date);
        AssessmentScore score = mAssessmentScoreFactory.create(template, clientData);
        assessment.setScore(score);
    }

    private void setPartsBasedProperties(Assessment assessment, AssessmentParts assessmentParts)
    {
        assessment.setAnimal(assessmentParts.mAnimal);
        assessment.setStudy(assessmentParts.mStudy);
        assessment.setAnimalHousing(assessmentParts.mAnimalHousing);
        assessment.setReason(assessmentParts.mAssessmentReason);
        assessment.setPerformedBy(assessmentParts.mUser);
    }

    public void update(Assessment assessmentToUpdate, AssessmentTemplate template, AssessmentClientData clientData,
        AssessmentParts assessmentParts, boolean isComplete)
    {
        assessmentToUpdate.setIsComplete(isComplete);
        setRawDataBasedNonIdProperties(assessmentToUpdate, template, clientData);
        setPartsBasedProperties(assessmentToUpdate, assessmentParts);
    }
}
