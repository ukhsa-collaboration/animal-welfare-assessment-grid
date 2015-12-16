package uk.gov.phe.erdst.sc.awag.service.factory.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;

@Stateless
public class AssessmentPartsFactoryImpl implements AssessmentPartsFactory
{
    private static final Logger LOGGER = LogManager.getLogger(AssessmentPartsFactoryImpl.class);

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
    public AssessmentParts create(AssessmentClientData clientData) throws AWAssessmentCreationException
    {
        Animal animal = null;
        Study study = null;
        User user = null;
        AssessmentReason reason = null;
        AnimalHousing housing = null;

        try
        {
            animal = mAnimalController.getAnimal(clientData.animalId);
            study = mStudyController.getStudyWithAnimal(animal.getId());
        }
        catch (AWNoSuchEntityException | AWMultipleResultException e)
        {
            LOGGER.error(e);
            throw new AWAssessmentCreationException(e.getMessage());
        }

        try
        {
            user = mUserController.getUser(clientData.performedBy);
        }
        catch (AWNoSuchEntityException e)
        {
            user = null;
        }

        try
        {
            reason = mAssessmentReasonController.getReason(clientData.reason);
        }
        catch (AWNoSuchEntityException e)
        {
            reason = null;
        }

        try
        {
            housing = mAnimalHousingController.getAnimalHousing(clientData.animalHousing);
        }
        catch (AWNoSuchEntityException e)
        {
            housing = null;
        }

        return new AssessmentParts(animal, study, reason, user, housing);
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
