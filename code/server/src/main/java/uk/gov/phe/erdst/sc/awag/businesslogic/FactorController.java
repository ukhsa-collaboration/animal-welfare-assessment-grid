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

import uk.gov.phe.erdst.sc.awag.dao.FactorDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.ImportFactorFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.FactorClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorsDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;

@Stateless
public class FactorController
{
    @Inject
    private FactorDao mFactorDao;

    @Inject
    private Validator mValidator;

    @Inject
    private FactorFactory mFactorFactory;

    @Inject
    private FactorDtoFactory mFactorDtoFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportFactorFactory importFactorFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_FACTOR)
    public EntityCreateResponseDto createFactor(FactorClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Factor factor = mFactorFactory.create(clientData);
            factor = mFactorDao.store(factor);
            response.id = factor.getId();
            response.value = factor.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public FactorsDto getAllFactors(PagingQueryParams pagingParams) throws AWInputValidationException
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

            FactorsDto response = new FactorsDto();

            Collection<Factor> scoringFactors = mFactorDao.getEntities(offset, limit);
            response.factors = mFactorDtoFactory.createFactorDtos(scoringFactors);

            if (pagingParams.isParamsSet())
            {
                Long factorCount = mFactorDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, factorCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public FactorDto getFactorById(Long factorId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(factorId);
        Factor factor = mFactorDao.getEntityById(factorId);
        FactorDto dto = mFactorDtoFactory.create(factor);
        return dto;
    }

    public FactorsDto getFactorsLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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
            FactorsDto response = new FactorsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            List<Factor> scoringFactors = mFactorDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            response.factors = mFactorDtoFactory.createFactorDtos(scoringFactors);

            if (pagingParams.isParamsSet())
            {
                Long factorCount = mFactorDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, factorCount);
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

    @LoggedActivity(actionName = LoggedActions.UPDATE_FACTOR)
    public FactorDto updateFactor(Long factorId, FactorClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<FactorClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.factorId, factorId, ValidationConstants.UPDATE_ID_MISMATCH);

            Factor factor = mFactorDao.getEntityById(clientData.factorId);
            mFactorFactory.update(factor, clientData);
            factor = mFactorDao.update(factor);
            return mFactorDtoFactory.create(factor);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_FACTOR)
    public ResponseDto uploadFactor(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_FACTOR_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportFactor(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportFactor importFactor = importFactorFactory.create(uploadCSVLineData);
                importHeader.addImportFactor(importFactor);
            }

            importHeader = importHeaderDao.store(importHeader);

            uploadFactorsFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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
    // TODO this code to be moved in the client (test for now)
    private void uploadFactorsFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Factor> Factors = new ArrayList<>();
        for (ImportFactor importFactor : importHeader.getImportFactors())
        {
            Factor Factor = mFactorFactory.create(importFactor);
            Factors.add(Factor);
        }

        mFactorDao.upload(Factors);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}
