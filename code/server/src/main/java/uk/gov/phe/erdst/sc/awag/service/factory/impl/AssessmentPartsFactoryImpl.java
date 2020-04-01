package uk.gov.phe.erdst.sc.awag.service.factory.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalHousingController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentReasonController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.businesslogic.UserController;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserClientData;

@Stateless
public class AssessmentPartsFactoryImpl implements AssessmentPartsFactory
{
    @Inject
    private AnimalController mAnimalController;

    @Inject
    private AnimalHousingController mAnimalHousingController;

    @Inject
    private AssessmentReasonController mAssessmentReasonController;

    @Inject
    private UserController mUserController;

    @Inject
    private StudyController mStudyController;

    @Override
    public AssessmentParts create(AssessmentClientData clientData) throws AWNoSuchEntityException, AWSeriousException
    {
        Animal animal = null;
        Study study = null;
        User user = null;
        AssessmentReason reason = null;
        AnimalHousing housing = null;

        try
        {
            animal = mAnimalController.getAnimalNonApiMethod(clientData.animalId);
            study = mStudyController.getStudyWithAnimalNonApiMethod(animal.getId());
        }
        catch (AWMultipleResultException e)
        {
            throw new AWSeriousException(e);
        }

        try
        {
            user = mUserController.getUserByNameNonApiMethod(clientData.performedBy);
        }
        catch (AWNoSuchEntityException e)
        {
            user = null;
        }

        try
        {
            reason = mAssessmentReasonController.getReasonNonApiMethod(clientData.reason);
        }
        catch (AWNoSuchEntityException e)
        {
            reason = null;
        }

        try
        {
            housing = mAnimalHousingController.getAnimalHousingNonApiMethod(clientData.animalHousing);
        }
        catch (AWNoSuchEntityException e)
        {
            housing = null;
        }

        return new AssessmentParts(animal, study, reason, user, housing);
    }

    @Override
    public AssessmentParts create(AssessmentClientData clientData, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWSeriousException, AWNonUniqueException
    {
        AssessmentParts assessmentParts = create(clientData);

        if (assessmentParts.mUser == null || assessmentParts.mAssessmentReason == null
            || assessmentParts.mAnimalHousing == null || assessmentParts.mStudy == null)
        {
            if (assessmentParts.mAssessmentReason == null)
            {
                AssessmentReasonClientData assessmentReasonClientData = new AssessmentReasonClientData(
                    Constants.UNASSIGNED_ID, clientData.reason);
                mAssessmentReasonController.createAssessmentReasonNonApi(assessmentReasonClientData, loggedUser);
            }

            if (assessmentParts.mAnimalHousing == null)
            {
                AnimalHousingClientData animalHousingClientData = new AnimalHousingClientData(Constants.UNASSIGNED_ID,
                    clientData.animalHousing);
                mAnimalHousingController.createAnimalHousingNonApi(animalHousingClientData, loggedUser);
            }

            if (assessmentParts.mUser == null)
            {
                UserClientData userClientData = new UserClientData(Constants.UNASSIGNED_ID, clientData.performedBy);
                mUserController.createUserNonApi(userClientData, loggedUser);
            }

            assessmentParts = create(clientData);
            return assessmentParts;
        }

        return assessmentParts;
    }

    public static class AssessmentParts
    {
        // CS:OFF: VisibilityModifier
        public final Animal mAnimal;
        public final Study mStudy;
        public final AssessmentReason mAssessmentReason;
        public final User mUser;
        public final AnimalHousing mAnimalHousing;
        // CS:ON

        public AssessmentParts(Animal animal, Study study, AssessmentReason assessmentReason, User user,
            AnimalHousing animalHousing)
        {
            mAnimal = animal;
            mStudy = study;
            mAssessmentReason = assessmentReason;
            mUser = user;
            mAnimalHousing = animalHousing;
        }
    }
}
