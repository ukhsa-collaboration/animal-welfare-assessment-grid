package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyGroupClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class StudyGroupControllerImpl implements StudyGroupController
{
    @Inject
    private StudyGroupDao mStudyGroupDao;

    @Inject
    private Validator mStudyGroupValidator;

    @Inject
    private StudyGroupFactory mStudyGroupFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_STUDY_GROUP)
    public void storeGroup(StudyGroupClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<StudyGroupClientData>> studyGroupConstraintViolations = mStudyGroupValidator
            .validate(clientData);

        if (studyGroupConstraintViolations.isEmpty())
        {
            try
            {
                StudyGroup studyGroup = mStudyGroupFactory.create(clientData);
                StudyGroup newStudyGroup = mStudyGroupDao.store(studyGroup);
                clientData.studyGroupId = newStudyGroup.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(studyGroupConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_STUDY_GROUP)
    public void updateStudyGroup(Long studyGroupId, StudyGroupClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<StudyGroupClientData>> studyGroupConstraintViolations = mStudyGroupValidator
            .validate(clientData);

        if (studyGroupConstraintViolations.isEmpty())
        {
            try
            {
                StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(studyGroupId);
                mStudyGroupFactory.update(studyGroup, clientData);
                mStudyGroupDao.store(studyGroup);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(studyGroupConstraintViolations);
        }
    }

    @Override
    public List<EntitySelectDto> getStudyGroupsLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        Set<StudyGroup> studyGroups = mStudyGroupDao.getStudyGroupsLike(like, offset, limit);
        if (includeMetadata)
        {
            Long studyGroupsCount = mStudyGroupDao.getCountStudyGroupsLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, studyGroupsCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(studyGroups);
    }

    @Override
    public StudyGroupClientData getStudyGroup(Long id) throws AWNoSuchEntityException
    {
        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(id);
        return mStudyGroupFactory.create(studyGroup);
    }

    @Override
    public Set<StudyGroupClientData> getStudyGroupsLikeFull(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        Set<StudyGroup> studyGroups = mStudyGroupDao.getStudyGroupsLike(like, offset, limit);
        if (includeMetadata)
        {
            Long studyGroupsCount = mStudyGroupDao.getCountStudyGroupsLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, studyGroupsCount, responsePayload);
        }
        return mStudyGroupFactory.create(studyGroups);
    }

    @Override
    public Set<StudyGroupClientData> getStudyGroups(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Set<StudyGroup> studyGroups = mStudyGroupDao.getStudyGroups(offset, limit);
        if (includeMetadata)
        {
            Long studyGroupCount = mStudyGroupDao.getCountStudyGroups();
            mResponsePager.setPagingTotalsMetadata(offset, limit, studyGroupCount, responsePayload);
        }
        return mStudyGroupFactory.create(studyGroups);
    }

    @Override
    public StudyGroup getStudyGroup(Animal animal, Study study)
    {
        for (StudyGroup group : study.getStudyGroups())
        {
            if (group.getAnimals().contains(animal))
            {
                return group;
            }
        }

        return null;
    }
}
