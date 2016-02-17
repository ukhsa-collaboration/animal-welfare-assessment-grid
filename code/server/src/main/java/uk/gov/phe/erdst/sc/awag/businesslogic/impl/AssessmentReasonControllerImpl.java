package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentReasonController;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentReasonFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class AssessmentReasonControllerImpl implements AssessmentReasonController
{
    @Inject
    private AssessmentReasonFactory mAssessmentReasonFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private AssessmentReasonDao mAssessmentReasonDao;

    @Inject
    private Validator mReasonValidator;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public Collection<AssessmentReason> getReasons(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<AssessmentReason> assessmentReasons = mAssessmentReasonDao.getEntities(offset, limit);
        if (includeMetadata)
        {
            Long assessmentReasonsCount = mAssessmentReasonDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, assessmentReasonsCount, responsePayload);
        }
        return assessmentReasons;
    }

    @Override
    public AssessmentReason getReason(String reasonName) throws AWNoSuchEntityException
    {
        return mAssessmentReasonDao.getEntityByNameField(reasonName);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_REASON)
    public void storeReason(AssessmentReasonClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AssessmentReasonClientData>> reasonConstraintViolations = mReasonValidator
            .validate(clientData);

        if (reasonConstraintViolations.isEmpty())
        {
            AssessmentReason reason = mAssessmentReasonFactory.create(clientData);
            try
            {
                mAssessmentReasonDao.store(reason);
                clientData.reasonId = reason.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(reasonConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_REASON)
    public void updateReason(Long reasonId, AssessmentReasonClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AssessmentReasonClientData>> reasonConstraintViolations = mReasonValidator
            .validate(clientData);

        if (reasonConstraintViolations.isEmpty())
        {
            try
            {
                AssessmentReason reason = mAssessmentReasonDao.getEntityById(reasonId);
                mAssessmentReasonFactory.update(reason, clientData);
                mAssessmentReasonDao.update(reason);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(reasonConstraintViolations);
        }
    }

    @Override
    public Collection<EntitySelectDto> getReasonsLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<AssessmentReason> reasons = mAssessmentReasonDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long reasonsCount = mAssessmentReasonDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, reasonsCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(reasons);
    }

    @Override
    public AssessmentReason getReason(Long reasonId) throws AWNoSuchEntityException
    {
        return mAssessmentReasonDao.getEntityById(reasonId);
    }
}
