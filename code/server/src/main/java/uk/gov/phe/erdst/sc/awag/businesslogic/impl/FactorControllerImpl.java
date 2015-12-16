package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.FactorController;
import uk.gov.phe.erdst.sc.awag.dao.FactorDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.client.FactorClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorFactory;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class FactorControllerImpl implements FactorController
{
    @Inject
    private FactorDao mFactorDao;

    @Inject
    private Validator mFactorValidator;

    @Inject
    private FactorFactory mFactorFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public Collection<Factor> getFactors(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Factor> scoringFactors = mFactorDao.getEntities(offset, limit);

        if (includeMetadata)
        {
            Long factorCount = mFactorDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, factorCount, responsePayload);
        }

        return scoringFactors;
    }

    @Override
    public Factor getFactor(Long factorId) throws AWNoSuchEntityException
    {
        return mFactorDao.getEntityById(factorId);
    }

    @Override
    public List<EntitySelectDto> getFactorsLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Factor> scoringFactors = mFactorDao.getEntitiesLike(like, offset, limit);

        if (includeMetadata)
        {
            Long factorCount = mFactorDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, factorCount, responsePayload);
        }

        return mEntitySelectDtoFactory.createEntitySelectDtos(scoringFactors);
    }

    @Override
    public void updateFactor(Long factorId, FactorClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mFactorValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            try
            {
                Factor factor = mFactorDao.getEntityById(clientData.factorId);
                mFactorFactory.update(factor, clientData);
                mFactorDao.update(factor);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(constraintViolations);
        }

    }

    @Override
    public void storeFactor(FactorClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mFactorValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            Factor factor = mFactorFactory.create(clientData);
            try
            {
                mFactorDao.store(factor);
                clientData.factorId = factor.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(constraintViolations);
        }
    }
}
