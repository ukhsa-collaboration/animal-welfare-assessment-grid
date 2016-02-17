package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.NotImplementedException;

import uk.gov.phe.erdst.sc.awag.businesslogic.SpeciesController;
import uk.gov.phe.erdst.sc.awag.dao.SpeciesDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SpeciesClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.species.SpeciesFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class SpeciesControllerImpl implements SpeciesController
{
    @Inject
    private Validator mSpeciesValidator;

    @Inject
    private SpeciesFactory mSpeciesFactory;

    @Inject
    private SpeciesDao mSpeciesDao;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_SPECIES)
    public void storeSpecies(SpeciesClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<SpeciesClientData>> speciesConstraintViolations = mSpeciesValidator
            .validate(clientData);

        if (speciesConstraintViolations.isEmpty())
        {
            Species species = mSpeciesFactory.create(clientData);
            try
            {
                Species newSpecies = mSpeciesDao.store(species);
                clientData.speciesId = newSpecies.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(speciesConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_SPECIES)
    public void updateSpecies(Long speciesId, SpeciesClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<SpeciesClientData>> speciesConstraintViolations = mSpeciesValidator
            .validate(clientData);

        if (speciesConstraintViolations.isEmpty())
        {
            try
            {
                Species species = mSpeciesDao.getSpecies(clientData.speciesId);
                mSpeciesFactory.update(species, clientData);
                mSpeciesDao.store(species);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(speciesConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.DELETE_SPECIES)
    public void deleteSpecies(Long speciesId, LoggedUser loggedUser)
    {
        // mSpeciesDao.updateIsDeleted(speciesId, true);
        throw new NotImplementedException("Not implemented yet.");
    }

    @Override
    public Collection<Species> getSpecies(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Species> species = mSpeciesDao.getSpecies(offset, limit);
        if (includeMetadata)
        {
            Long speciesCount = mSpeciesDao.getCountSpecies();
            mResponsePager.setPagingTotalsMetadata(offset, limit, speciesCount, responsePayload);
        }
        return species;
    }

    @Override
    public List<EntitySelectDto> getSpeciesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Species> species = mSpeciesDao.getSpeciesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long speciesCount = mSpeciesDao.getCountSpeciesLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, speciesCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(species);
    }
}
