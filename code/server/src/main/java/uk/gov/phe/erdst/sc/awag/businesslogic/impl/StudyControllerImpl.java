package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyGroupClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.StudySimpleDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class StudyControllerImpl implements StudyController
{
    private static final Logger LOGGER = LogManager.getLogger(StudyControllerImpl.class.getName());

    @Inject
    private AnimalController mAnimalController;

    @Inject
    private StudyDao mStudyDao;

    @Inject
    private Validator mStudyValidator;

    @Inject
    private StudyFactory mStudyFactory;

    @Inject
    private StudyGroupFactory mStudyGroupFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Inject
    private StudyDtoFactory mStudyDtoFactory;

    @Override
    public Collection<Study> getStudies(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Study> studies = mStudyDao.getEntities(offset, limit);
        if (includeMetadata)
        {
            Long studiesCount = mStudyDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, studiesCount, responsePayload);
        }
        return studies;
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_STUDY)
    public void updateStudy(Long studyId, StudyClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<StudyClientData>> studyConstraintViolations = mStudyValidator.validate(clientData);
        if (studyConstraintViolations.isEmpty())
        {
            try
            {
                Study study = mStudyDao.getEntityById(studyId);
                mStudyFactory.update(study, clientData);
                mStudyDao.store(study);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(studyConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_STUDY)
    public void storeStudy(StudyClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<StudyClientData>> studyConstraintViolations = mStudyValidator.validate(clientData);
        if (studyConstraintViolations.isEmpty())
        {
            try
            {
                Study study = mStudyFactory.create(clientData);
                Study newStudy = mStudyDao.store(study);
                clientData.studyId = newStudy.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(studyConstraintViolations);
        }
    }

    @Override
    public List<EntitySelectDto> getStudyLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Study> studies = mStudyDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long studiesCount = mStudyDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, studiesCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(studies);
    }

    @Override
    public StudyClientData getStudy(Long studyId) throws AWNoSuchEntityException
    {
        Study study = mStudyDao.getEntityById(studyId);
        Set<StudyGroupClientData> studyGroupClientData = mStudyGroupFactory.create(study.getStudyGroups());
        return mStudyFactory.create(study, studyGroupClientData);
    }

    @Override
    public Study getStudyWithAnimal(Long animalId) throws AWNoSuchEntityException, AWMultipleResultException
    {
        Animal animal = mAnimalController.getAnimal(animalId);
        return mStudyDao.getStudyWithAnimal(animal);
    }

    @Override
    public StudySimpleDto getStudyWithAnimalDto(Long animalId) throws AWNoSuchEntityException
    {
        Study study = null;

        try
        {
            study = getStudyWithAnimal(animalId);
            return mStudyDtoFactory.createStudySimpleDto(study);
        }
        catch (AWMultipleResultException e)
        {
            LOGGER.error(e);
            // FIXME: this should be returned as an error in response payload
            // studyNumber = "Error: more than one study for animal with id: " + animalId;
        }

        return mStudyDtoFactory.createStudySimpleDto(study);
    }
}
