package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalHousingController;
import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class AnimalHousingControllerImpl implements AnimalHousingController
{
    @Inject
    private AnimalHousingFactory mAnimalHousingFactory;

    @Inject
    private AnimalHousingDao mAnimalHousingDao;

    @Inject
    private Validator mAnimalHousingValidator;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public Collection<AnimalHousing> getAnimalHousings(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<AnimalHousing> houses = mAnimalHousingDao.getEntities(offset, limit);
        if (includeMetadata)
        {
            Long housingCount = mAnimalHousingDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, housingCount, responsePayload);
        }
        return houses;
    }

    @Override
    public AnimalHousing getAnimalHousing(String name) throws AWNoSuchEntityException
    {
        return mAnimalHousingDao.getEntityByNameField(name);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_HOUSING)
    public void storeAnimalHousing(AnimalHousingClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AnimalHousingClientData>> animalHousingConstraintViolations = mAnimalHousingValidator
            .validate(clientData);

        if (animalHousingConstraintViolations.isEmpty())
        {
            AnimalHousing animalHousing = mAnimalHousingFactory.create(clientData);
            try
            {
                mAnimalHousingDao.store(animalHousing);
                clientData.housingId = animalHousing.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(animalHousingConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_HOUSING)
    public void updateAnimalHousing(Long animalHousingId, AnimalHousingClientData clientData,
        ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AnimalHousingClientData>> animalHousingConstraintViolations = mAnimalHousingValidator
            .validate(clientData);

        if (animalHousingConstraintViolations.isEmpty())
        {
            try
            {
                AnimalHousing animalHousing = mAnimalHousingDao.getEntityById(animalHousingId);
                mAnimalHousingFactory.update(animalHousing, clientData);
                mAnimalHousingDao.update(animalHousing);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(animalHousingConstraintViolations);
        }
    }

    @Override
    public List<EntitySelectDto> getHousingLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<AnimalHousing> housings = mAnimalHousingDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long housingsCount = mAnimalHousingDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, housingsCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(housings);
    }

    @Override
    public AnimalHousing getAnimalHousing(Long housingId) throws AWNoSuchEntityException
    {
        return mAnimalHousingDao.getEntityById(housingId);
    }
}
