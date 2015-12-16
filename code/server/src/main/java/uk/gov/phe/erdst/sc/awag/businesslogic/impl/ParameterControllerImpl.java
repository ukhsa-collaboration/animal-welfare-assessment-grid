package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.ParameterController;
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ParameterClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterFactory;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class ParameterControllerImpl implements ParameterController
{
    @Inject
    private ParameterDao mParameterDao;

    @Inject
    private Validator mParameterValidator;

    @Inject
    private ParameterFactory mParameterFactory;

    @Inject
    private ParameterDtoFactory mParameterDtoFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public ParameterDto getParameterDto(Long parameterId) throws AWNoSuchEntityException
    {
        Parameter parameter = mParameterDao.getEntityById(parameterId);
        return mParameterDtoFactory.create(parameter);
    }

    @Override
    public Collection<Parameter> getParameters(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Parameter> parameters = mParameterDao.getEntities(offset, limit);
        if (includeMetadata)
        {
            Long parameterCount = mParameterDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, parameterCount, responsePayload);
        }
        return parameters;
    }

    @Override
    public List<EntitySelectDto> getParametersLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Parameter> parameters = mParameterDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long parameterCount = mParameterDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, parameterCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(parameters);
    }

    @Override
    public void updateParameter(Long parameterId, ParameterClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<ParameterClientData>> parameterConstraintViolations = mParameterValidator
            .validate(clientData);

        if (parameterConstraintViolations.isEmpty())
        {
            try
            {
                Parameter parameter = mParameterDao.getEntityById(clientData.parameterId);
                mParameterFactory.update(parameter, clientData);
                mParameterDao.store(parameter);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(parameterConstraintViolations);
        }
    }

    @Override
    public void storeParameter(ParameterClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<ParameterClientData>> parameterConstraintViolations = mParameterValidator
            .validate(clientData);

        if (parameterConstraintViolations.isEmpty())
        {
            Parameter parameter = mParameterFactory.create(clientData);
            try
            {
                mParameterDao.store(parameter);
                clientData.parameterId = parameter.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(parameterConstraintViolations);
        }
    }

    @Override
    public Collection<Parameter> getParameters(Long[] parametersIds)
    {
        return mParameterDao.getEntitiesByIds(parametersIds);
    }
}
