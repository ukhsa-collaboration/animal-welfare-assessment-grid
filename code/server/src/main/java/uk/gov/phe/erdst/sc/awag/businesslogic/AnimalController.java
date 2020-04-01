package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.ImportAnimalFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalsDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;

@Stateless
public class AnimalController
{

    @Inject
    private AnimalDao mAnimalDao;

    @Inject
    private AnimalDtoFactory mAnimalDtoFactory;

    @Inject
    private Validator mValidator;

    @Inject
    private AnimalFactory mAnimalFactory;

    @Inject
    private ImportAnimalFactory mImportAnimalFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportHeaderDao mImportHeaderDao;

    private List<ImportAnimal> mImportAnimalsWithNoParent;

    private List<ImportAnimal> mImportAnimalsWithParent;

    @LoggedActivity(actionName = LoggedActions.CREATE_ANIMAL)
    public EntityCreateResponseDto newApiCreateAnimal(AnimalClientData clientData, LoggedUser loggedUser)
        throws AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        Set<ConstraintViolation<AnimalClientData>> animalConstraintViolations = mValidator.validate(clientData);

        if (animalConstraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Animal animal = mAnimalFactory.create(clientData);
            animal = mAnimalDao.store(animal);
            response.id = animal.getId();
            response.value = animal.getAnimalNumber();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(animalConstraintViolations));
            return null;
        }
    }

    public AnimalDto newApiGetNonDeletedAnimalById(Long id) throws AWInputValidationException, AWNoSuchEntityException
    {
        ValidatorUtils.validateEntityId(id);
        Animal animal = mAnimalDao.getNonDeletedAnimalById(id);
        AnimalDto dto = mAnimalDtoFactory.createAnimalDto(animal);
        return dto;
    }

    public AnimalsDto newApiGetNonDeletedAnimals(PagingQueryParams pagingParams) throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            AnimalsDto response = new AnimalsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            response.animals = mAnimalDtoFactory.createAnimalDtos(mAnimalDao.getNonDeletedAnimals(offset, limit));

            if (pagingParams.isParamsSet())
            {
                Long animalsCount = mAnimalDao.getCountNonDeletedAnimals();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, animalsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public AnimalsDto newApiGetNonDeletedAnimalsLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
        throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);

        Set<ConstraintViolation<LikeFilterParam>> likeParamViolations = mValidator.validate(likeFilterParam);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty() && likeParamViolations.isEmpty())
        {
            AnimalsDto response = new AnimalsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            response.animals = mAnimalDtoFactory
                .createAnimalDtos(mAnimalDao.getNonDeletedAnimalsLike(likeFilterParam.value, offset, limit));

            if (pagingParams.isParamsSet())
            {
                Long animalsCount = mAnimalDao.getCountNonDeletedAnimalsLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, animalsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils
                .throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations, likeParamViolations));
            return null;
        }
    }

    public AnimalsDto newApiGetNonDeletedFemaleAnimalsLike(LikeFilterParam likeFilterParam,
        PagingQueryParams pagingParams) throws AWInputValidationException
    {
        return newApiGetNonDeletedAnimalsLikeBySex(likeFilterParam, pagingParams, Sex.FEMALE);
    }

    public AnimalsDto newApiGetNonDeletedMaleAnimalsLike(LikeFilterParam likeFilterParam,
        PagingQueryParams pagingParams) throws AWInputValidationException
    {
        return newApiGetNonDeletedAnimalsLikeBySex(likeFilterParam, pagingParams, Sex.MALE);
    }

    private AnimalsDto newApiGetNonDeletedAnimalsLikeBySex(LikeFilterParam likeFilterParam,
        PagingQueryParams pagingParams, String sexToQuery) throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);

        Set<ConstraintViolation<LikeFilterParam>> likeParamViolations = mValidator.validate(likeFilterParam);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty() && likeParamViolations.isEmpty())
        {
            AnimalsDto response = new AnimalsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            response.animals = mAnimalDtoFactory.createAnimalDtos(
                mAnimalDao.getNonDeletedAnimalsLikeSex(likeFilterParam.value, sexToQuery, offset, limit));

            if (pagingParams.isParamsSet())
            {
                Long animalsCount = mAnimalDao.getCountNonDeleteAnimalsLikeSex(likeFilterParam.value, sexToQuery);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, animalsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils
                .throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations, likeParamViolations));
            return null;
        }
    }

    public AnimalsDto newApiGetDeletedAnimals(PagingQueryParams pagingParams) throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);

        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            AnimalsDto response = new AnimalsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            response.animals = mAnimalDtoFactory.createAnimalDtos(mAnimalDao.getDeletedAnimals(offset, limit));

            if (pagingParams.isParamsSet())
            {
                Long animalsCount = mAnimalDao.getCountDeletedAnimals();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, animalsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_ANIMAL)
    public AnimalDto newApiUpdateAnimal(Long id, AnimalClientData clientData, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        Set<ConstraintViolation<AnimalClientData>> animalConstraintViolations = mValidator.validate(clientData);

        if (animalConstraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.id, id, ValidationConstants.UPDATE_ID_MISMATCH);

            Animal animal = mAnimalDao.getAnimal(id);
            mAnimalFactory.update(animal, clientData);
            Animal updatedAnimal = mAnimalDao.store(animal);
            AnimalDto response = mAnimalDtoFactory.createAnimalDto(updatedAnimal);
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(animalConstraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.DELETE_ANIMAL)
    public AnimalDto newApiDeleteAnimal(Long id, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(id);
        AnimalDto response = mAnimalDtoFactory.createAnimalDto(mAnimalDao.updateIsDeleted(id, true));
        return response;
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_ANIMAL)
    public ResponseDto uploadAnimal(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_ANIMAL_COLUMNS);

            sortImportAnimals(csvLinesData);
            ImportHeader importHeader = storeImportAnimals(loggedUser);

            uploadAnimalFromImport(importHeader);
        }
        catch (AWNonUniqueException ex)
        {
            throw new AWInputValidationException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT);
        }

        return new UploadResponseDto();
    }

    public Animal getAnimalNonApiMethod(Long animalId) throws AWNoSuchEntityException
    {
        return mAnimalDao.getAnimal(animalId);
    }

    public Animal getAnimalNonApiMethod(String animalNumber) throws AWNoSuchEntityException
    {
        return mAnimalDao.getAnimal(animalNumber);
    }

    private void sortImportAnimals(final ArrayList<String[]> csvLinesData)
    {
        mImportAnimalsWithNoParent = new ArrayList<>();
        mImportAnimalsWithParent = new ArrayList<>();

        for (String[] uploadCSVLineData : csvLinesData)
        {
            ImportAnimal importAnimal = mImportAnimalFactory.create(uploadCSVLineData);
            if (importAnimal.getDamAnimalName() == null && importAnimal.getFatherAnimalName() == null)
            {
                mImportAnimalsWithNoParent.add(importAnimal);
            }
            else
            {
                mImportAnimalsWithParent.add(importAnimal);
            }
        }
    }

    private ImportHeader storeImportWithNoParent(ImportHeader importHeader) throws AWNonUniqueException
    {
        for (ImportAnimal importAnimal : mImportAnimalsWithNoParent)
        {
            importHeader.addImportAnimal(importAnimal);
        }

        ImportHeader newImportHeader = mImportHeaderDao.store(importHeader);

        return newImportHeader;
    }

    private ImportHeader storeImportWithParent(ImportHeader importHeader) throws AWNonUniqueException
    {
        for (ImportAnimal importAnimal : mImportAnimalsWithParent)
        {
            mImportAnimalFactory.update(importAnimal, importHeader);
        }

        for (ImportAnimal importAnimal : mImportAnimalsWithParent)
        {
            importHeader.addImportAnimal(importAnimal);
        }

        ImportHeader newImportHeader = mImportHeaderDao.store(importHeader);

        return newImportHeader;
    }

    private ImportHeader storeImportAnimals(LoggedUser loggedUser) throws AWNonUniqueException
    {
        ImportHeader importHeader = importHeaderFactory.createWithImportAnimal(loggedUser);
        importHeader = storeImportWithNoParent(importHeader);
        importHeader = storeImportWithParent(importHeader);
        return importHeader;
    }

    private void uploadAnimalFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        // TODO don't like this - should be handled as a Query to filter information - needs tests!
        // Inefficient
        Collection<Animal> animalsNoParent = new ArrayList<>();
        for (ImportAnimal importAnimal : importHeader.getImportAnimals())
        {
            if (importAnimal.getDamImportanimalid() == null && importAnimal.getFatherImportanimalid() == null)
            {
                Animal animal = mAnimalFactory.create(importAnimal);
                animalsNoParent.add(animal);
            }
        }
        mAnimalDao.upload(animalsNoParent);

        Collection<Animal> animalsWithParent = new ArrayList<>();
        for (ImportAnimal importAnimal : importHeader.getImportAnimals())
        {
            if (importAnimal.getDamImportanimalid() != null || importAnimal.getFatherImportanimalid() != null)
            {
                Animal animal = mAnimalFactory.create(importAnimal);
                animalsWithParent.add(animal);
            }
        }
        mAnimalDao.upload(animalsWithParent);

        mImportHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}