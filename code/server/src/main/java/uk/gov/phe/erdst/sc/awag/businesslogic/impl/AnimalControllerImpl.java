package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class AnimalControllerImpl implements AnimalController
{
    @Inject
    private AnimalDao mAnimalDao;

    @Inject
    private AssessmentTemplateController mAssessmentTemplateController;

    @Inject
    private AnimalDtoFactory mAnimalDtoFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private Validator mAnimalValidator;

    @Inject
    private AnimalFactory mAnimalFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public Collection<AnimalDto> getNonDeletedAnimals(Integer offset, Integer limit, ResponsePayload responsePayload,
        boolean includeMetadata)
    {
        Collection<Animal> animals = mAnimalDao.getNonDeletedAnimals(offset, limit);

        if (includeMetadata)
        {
            Long animalsCount = mAnimalDao.getCountNonDeletedAnimals();
            mResponsePager.setPagingTotalsMetadata(offset, limit, animalsCount, responsePayload);
        }

        return mAnimalDtoFactory.createAnimalDtos(animals);
    }

    @Override
    public AssessmentTemplateDto getAnimalAssessmentTemplateDto(Long animalId) throws AWNoSuchEntityException
    {
        return mAssessmentTemplateController.getAssessmentTemplateDtoByAnimalId(animalId);
    }

    @Override
    public Animal getAnimal(Long animalId) throws AWNoSuchEntityException
    {
        return mAnimalDao.getAnimal(animalId);
    }

    @Override
    public AnimalDto getAnimalDto(Long animalId) throws AWNoSuchEntityException
    {
        Animal animal = mAnimalDao.getAnimal(animalId);
        return mAnimalDtoFactory.createAnimalDto(animal);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_ANIMAL)
    public void storeAnimal(AnimalClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AnimalClientData>> animalConstraintViolations = mAnimalValidator.validate(clientData);

        if (animalConstraintViolations.isEmpty())
        {
            try
            {
                Animal animal = mAnimalFactory.create(clientData);
                Animal newAnimal = mAnimalDao.store(animal);
                clientData.id = newAnimal.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(animalConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_ANIMAL)
    public void updateAnimal(Long animalId, AnimalClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<AnimalClientData>> animalConstraintViolations = mAnimalValidator.validate(clientData);

        if (animalConstraintViolations.isEmpty())
        {
            try
            {
                Animal animal = mAnimalDao.getAnimal(clientData.id);
                mAnimalFactory.update(animal, clientData);
                mAnimalDao.store(animal);
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(animalConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.DELETE_ANIMAL)
    public void deleteAnimal(Long animalId, LoggedUser loggedUser) throws AWNoSuchEntityException
    {
        mAnimalDao.updateIsDeleted(animalId, true);
    }

    @Override
    public List<EntitySelectDto> getNonDeletedAnimalsLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Animal> animals = mAnimalDao.getNonDeletedAnimalsLike(like, offset, limit);

        if (includeMetadata)
        {
            Long animalsCount = mAnimalDao.getCountNonDeletedAnimalsLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, animalsCount, responsePayload);
        }

        return mEntitySelectDtoFactory.createEntitySelectDtos(animals);
    }

    @Override
    public AnimalDto getNonDeletedAnimalDtoById(Long animalId)
    {
        Animal animal = mAnimalDao.getNonDeletedAnimalById(animalId);
        if (animal == null)
        {
            // TODO: handle no animal found with id throw up and return 404
        }

        return mAnimalDtoFactory.createAnimalDto(animal);
    }

    @Override
    public List<EntitySelectDto> getNonDeletedAnimalsLikeSexDtos(String like, String sex, Integer offset,
        Integer limit, ResponsePayload responsePayload, boolean includeMetadata)
    {
        String sexToQuery;
        List<Animal> animals;

        if ("f".equals(sex))
        {
            sexToQuery = Sex.FEMALE;
            animals = mAnimalDao.getNonDeletedAnimalsLikeSex(like, Sex.FEMALE, offset, limit);
        }
        else
        {
            sexToQuery = Sex.MALE;
            animals = mAnimalDao.getNonDeletedAnimalsLikeSex(like, Sex.MALE, offset, limit);
        }

        if (includeMetadata)
        {
            Long animalsCount = mAnimalDao.getCountNonDeleteAnimalsLikeSex(like, sexToQuery);
            mResponsePager.setPagingTotalsMetadata(offset, limit, animalsCount, responsePayload);
        }

        return mEntitySelectDtoFactory.createEntitySelectDtos(animals);
    }

    @Override
    public List<AnimalDto> getNonDeletedAnimalLikeFullDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<Animal> animals = mAnimalDao.getNonDeletedAnimalsLike(like, offset, limit);

        if (includeMetadata)
        {
            Long animalsTotal = mAnimalDao.getCountNonDeletedAnimalsLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, animalsTotal, responsePayload);
        }

        return mAnimalDtoFactory.createAnimalDtos(animals);
    }
}
