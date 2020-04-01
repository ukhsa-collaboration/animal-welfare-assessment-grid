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

import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.dao.SpeciesDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportSpecy;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.species.ImportSpecyFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.species.SpeciesDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.species.SpeciesFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.SpeciesClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.species.SpeciesDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.species.SpeciesManyDto;

@Stateless
public class SpeciesController
{
    @Inject
    private Validator mValidator;

    @Inject
    private SpeciesFactory mSpeciesFactory;

    @Inject
    private SpeciesDtoFactory mSpeciesDtoFactory;

    @Inject
    private SpeciesDao mSpeciesDao;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportSpecyFactory mImportSpecyFactory;

    @Inject
    private ImportHeaderDao mImportHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_SPECIES)
    public EntityCreateResponseDto createSpecies(SpeciesClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Species species = mSpeciesFactory.create(clientData);
            species = mSpeciesDao.store(species);
            response.id = species.getId();
            response.value = species.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_SPECIES)
    public SpeciesDto updateSpecies(Long speciesId, SpeciesClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<SpeciesClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.speciesId, speciesId, ValidationConstants.UPDATE_ID_MISMATCH);

            Species species = mSpeciesDao.getSpecies(clientData.speciesId);
            mSpeciesFactory.update(species, clientData);
            species = mSpeciesDao.store(species);

            return mSpeciesDtoFactory.create(species);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public SpeciesManyDto getAllSpecies(PagingQueryParams pagingParams) throws AWInputValidationException
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

            Collection<Species> species = mSpeciesDao.getSpecies(offset, limit);
            SpeciesManyDto response = new SpeciesManyDto();
            response.species = mSpeciesDtoFactory.createSpeciesDtos(species);

            if (pagingParams.isParamsSet())
            {
                Long speciesCount = mSpeciesDao.getCountSpecies();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, speciesCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public SpeciesManyDto getSpeciesLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            List<Species> species = mSpeciesDao.getSpeciesLike(likeFilterParam.value, offset, limit);
            SpeciesManyDto response = new SpeciesManyDto();
            response.species = mSpeciesDtoFactory.createSpeciesDtos(species);

            if (pagingParams.isParamsSet())
            {
                Long speciesCount = mSpeciesDao.getCountSpeciesLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, speciesCount);
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

    public SpeciesDto getSpeciesById(Long speciesId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(speciesId);
        Species species = mSpeciesDao.getSpecies(speciesId);
        return mSpeciesDtoFactory.create(species);
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_SPECIES)
    public ResponseDto uploadSpecies(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_SPECIES_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportSpecies(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportSpecy importSpecy = mImportSpecyFactory.create(uploadCSVLineData);
                importHeader.addImportSpecy(importSpecy);
            }

            importHeader = mImportHeaderDao.store(importHeader);

            uploadSpeciesFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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

    public Species getSpeciesByNameNonApiMethod(String speciesName) throws AWNoSuchEntityException
    {
        // TODO
        return this.mSpeciesDao.getSpecies(speciesName);
    }

    // TODO convert for REST API
    private void uploadSpeciesFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Species> species = new ArrayList<>();
        for (ImportSpecy importSpecies : importHeader.getImportSpecies())
        {
            Species specy = mSpeciesFactory.create(importSpecies);
            species.add(specy);
        }

        mSpeciesDao.upload(species);

        mImportHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}