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

import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.ImportAnimalHousingFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.housing.AnimalHousingDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.housing.AnimalHousingsDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;

@Stateless
public class AnimalHousingController
{
    @Inject
    private AnimalHousingFactory mAnimalHousingFactory;

    @Inject
    private AnimalHousingDao mAnimalHousingDao;

    @Inject
    private Validator mValidator;

    @Inject
    private AnimalHousingDtoFactory mAnimalHousingDtoFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportAnimalHousingFactory mImportAnimalHousingFactory;

    @Inject
    private ImportHeaderDao mImportHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_HOUSING)
    public EntityCreateResponseDto createAnimalHousing(AnimalHousingClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            AnimalHousing animalHousing = mAnimalHousingFactory.create(clientData);
            animalHousing = mAnimalHousingDao.store(animalHousing);
            response.id = animalHousing.getId();
            response.value = animalHousing.getName();

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public AnimalHousingsDto getAllAnimalHousings(PagingQueryParams pagingParams) throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            AnimalHousingsDto response = new AnimalHousingsDto();

            Collection<AnimalHousing> housings = mAnimalHousingDao.getEntities(offset, limit);

            response.housings = mAnimalHousingDtoFactory.createAnimalHousingDtos(housings);

            if (pagingParams.isParamsSet())
            {
                Long housingCount = mAnimalHousingDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, housingCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_HOUSING)
    public AnimalHousingDto updateAnimalHousing(Long animalHousingId, AnimalHousingClientData clientData,
        LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<AnimalHousingClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.housingId, animalHousingId,
                ValidationConstants.UPDATE_ID_MISMATCH);

            AnimalHousing animalHousing = mAnimalHousingDao.getEntityById(animalHousingId);
            mAnimalHousingFactory.update(animalHousing, clientData);
            animalHousing = mAnimalHousingDao.update(animalHousing);
            return mAnimalHousingDtoFactory.createAnimalHousingDto(animalHousing);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public AnimalHousingsDto getHousingLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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
            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            AnimalHousingsDto response = new AnimalHousingsDto();

            List<AnimalHousing> housings = mAnimalHousingDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            response.housings = mAnimalHousingDtoFactory.createAnimalHousingDtos(housings);

            if (pagingParams.isParamsSet())
            {
                Long housingsCount = mAnimalHousingDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, housingsCount);
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

    public AnimalHousingDto getAnimalHousing(Long housingId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(housingId);
        AnimalHousing housing = mAnimalHousingDao.getEntityById(housingId);
        return mAnimalHousingDtoFactory.createAnimalHousingDto(housing);
    }

    public AnimalHousing getAnimalHousingNonApiMethod(String name) throws AWNoSuchEntityException
    {
        return mAnimalHousingDao.getEntityByNameField(name);
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_HOUSING)
    public ResponseDto uploadAnimalHousing(InputStream uploadFile, LoggedUser loggedUser)
        throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_ASSESSMENT_ANIMAL_HOUSING_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportAnimalHousing(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportAnimalHousing importAnimalHousing = mImportAnimalHousingFactory.create(uploadCSVLineData);
                importHeader.addImportAnimalHousing(importAnimalHousing);
            }

            importHeader = mImportHeaderDao.store(importHeader);

            uploadAnimalHousingsFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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

    // TODO convert for REST API
    private void uploadAnimalHousingsFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<AnimalHousing> animalHousings = new ArrayList<>();
        for (ImportAnimalHousing importAnimalHousing : importHeader.getImportAnimalHousings())
        {
            AnimalHousing animalHousing = mAnimalHousingFactory.create(importAnimalHousing);
            animalHousings.add(animalHousing);
        }

        mAnimalHousingDao.upload(animalHousings);

        mImportHeaderDao.realDelete(importHeader.getImportheaderid());
    }

    public AnimalHousing createAnimalHousingNonApi(AnimalHousingClientData clientData, LoggedUser loggedUser)
        throws AWNonUniqueException
    {
        // TODO unit test
        AnimalHousing animalHousing = mAnimalHousingFactory.create(clientData);
        animalHousing = mAnimalHousingDao.store(animalHousing);
        return animalHousing;
    }

}
