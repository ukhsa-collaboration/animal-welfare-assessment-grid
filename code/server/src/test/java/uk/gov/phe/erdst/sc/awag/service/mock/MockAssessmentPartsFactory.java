package uk.gov.phe.erdst.sc.awag.service.mock;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Mock;

@Mock
public class MockAssessmentPartsFactory implements AssessmentPartsFactory
{

    @Override
    public AssessmentParts create(AssessmentClientData clientData)
    {
        Animal animal = AnimalTestUtils.createAnimal(clientData.animalId);

        AssessmentReason assessmentReason = new AssessmentReason();
        assessmentReason.setName(clientData.reason);

        User user = new User();
        user.setName(clientData.performedBy);

        AnimalHousing animalHousing = new AnimalHousing();
        animalHousing.setName(clientData.animalHousing);

        Study study = new Study();
        study.setStudyNumber(TestConstants.TEST_STUDY_NAME);

        AssessmentParts parts = new AssessmentParts(animal, study, assessmentReason, user, animalHousing);

        return parts;
    }

}
