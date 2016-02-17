package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.NotImplementedException;

import uk.gov.phe.erdst.sc.awag.businesslogic.SourceController;
import uk.gov.phe.erdst.sc.awag.dao.SourceDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SourceClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.source.SourceFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class SourceControllerImpl implements SourceController
{
    @Inject
    private Validator mSourceValidator;

    @Inject
    private SourceFactory mSourceFactory;

    @Inject
    private SourceDao mSourceDao;

    @Inject
    private ResponsePager mResponsePager;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_SOURCE)
    public void storeSource(SourceClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<SourceClientData>> sourceConstraintViolations = mSourceValidator.validate(clientData);

        if (sourceConstraintViolations.isEmpty())
        {
            Source source = mSourceFactory.create(clientData);
            try
            {
                Source newSource = mSourceDao.store(source);
                clientData.sourceId = newSource.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(sourceConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_SOURCE)
    public void updateSource(Long sourceId, SourceClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<SourceClientData>> sourceConstraintViolations = mSourceValidator.validate(clientData);

        if (sourceConstraintViolations.isEmpty())
        {
            try
            {
                Source source = mSourceDao.getSource(clientData.sourceId);
                mSourceFactory.update(source, clientData);
                mSourceDao.store(source);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(sourceConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.DELETE_SOURCE)
    public void deleteSource(Long sourceId, LoggedUser loggedUser)
    {
        throw new NotImplementedException("Not implemented yet.");
    }

    @Override
    public Collection<Source> getSources(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Source> sources = mSourceDao.getSources(offset, limit);
        if (includeMetadata)
        {
            Long sourcesCount = mSourceDao.getCountSources();
            mResponsePager.setPagingTotalsMetadata(offset, limit, sourcesCount, responsePayload);
        }
        return sources;
    }

    @Override
    public List<EntitySelectDto> getSourcesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Source> sources = mSourceDao.getSourcesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long sourcesCount = mSourceDao.getCountSourcesLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, sourcesCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(sources);
    }
}
