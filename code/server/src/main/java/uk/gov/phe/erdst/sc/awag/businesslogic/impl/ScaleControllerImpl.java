package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.ScaleController;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ScaleClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.ScaleDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class ScaleControllerImpl implements ScaleController
{
    @Inject
    private ScaleDao mScaleDao;

    @Inject
    private ScaleFactory mScaleFactory;

    @Inject
    private ScaleDtoFactory mScaleDtoFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private Validator mScaleValidator;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_SCALE)
    public void storeScale(ScaleClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<ScaleClientData>> scaleConstraintViolations = mScaleValidator.validate(clientData);

        if (scaleConstraintViolations.isEmpty())
        {
            Scale scale = mScaleFactory.create(clientData);
            try
            {
                Scale newScale = mScaleDao.store(scale);
                clientData.scaleId = newScale.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(scaleConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_SCALE)
    public void updateScale(Long scaleId, ScaleClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<ScaleClientData>> scaleConstraintViolations = mScaleValidator.validate(clientData);

        if (scaleConstraintViolations.isEmpty())
        {
            try
            {
                Scale scale = mScaleDao.getEntityById(clientData.scaleId);
                mScaleFactory.update(scale, clientData);
                mScaleDao.store(scale);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(scaleConstraintViolations);
        }
    }

    @Override
    public Collection<Scale> getScales(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Scale> scales = mScaleDao.getEntities(offset, limit);
        if (includeMetadata)
        {
            Long scaleCount = mScaleDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, scaleCount, responsePayload);
        }
        return scales;
    }

    @Override
    public List<EntitySelectDto> getScalesLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Scale> scales = mScaleDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long scalesCount = mScaleDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, scalesCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(scales);
    }

    @Override
    public ScaleDto getScale(Long scaleId) throws AWNoSuchEntityException
    {
        return mScaleDtoFactory.create(mScaleDao.getEntityById(scaleId));
    }
}
