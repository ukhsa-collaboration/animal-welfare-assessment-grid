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
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportScale;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ImportScaleFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.ScaleClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.scale.ScaleDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.scale.ScalesDto;

@Stateless
public class ScaleController
{
    @Inject
    private ScaleDao scaleDao;

    @Inject
    private ScaleFactory scaleFactory;

    @Inject
    private ScaleDtoFactory mScaleDtoFactory;

    @Inject
    private Validator mValidator;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportScaleFactory importScaleFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_SCALE)
    public EntityCreateResponseDto createScale(ScaleClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Scale scale = scaleFactory.create(clientData);
            scale = scaleDao.store(scale);
            response.id = scale.getId();
            response.value = scale.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_SCALE)
    public ScaleDto updateScale(Long scaleId, ScaleClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<ScaleClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.scaleId, scaleId, ValidationConstants.UPDATE_ID_MISMATCH);

            Scale scale = scaleDao.getEntityById(clientData.scaleId);
            scaleFactory.update(scale, clientData);
            scale = scaleDao.update(scale);
            return mScaleDtoFactory.create(scale);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_SCALE)
    public ResponseDto uploadScale(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_SCALE_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportScales(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportScale importScale = importScaleFactory.create(uploadCSVLineData);
                importHeader.addImportScale(importScale);
            }

            importHeader = importHeaderDao.store(importHeader);

            uploadScalesFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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

    public ScalesDto getAllScales(PagingQueryParams pagingParams) throws AWInputValidationException
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

            ScalesDto response = new ScalesDto();

            Collection<Scale> scales = scaleDao.getEntities(offset, limit);
            response.scales = mScaleDtoFactory.createScaleDtos(scales);

            if (pagingParams.isParamsSet())
            {
                Long scaleCount = scaleDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, scaleCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }

    }

    public ScalesDto getScalesLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            ScalesDto response = new ScalesDto();

            List<Scale> scales = scaleDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            response.scales = mScaleDtoFactory.createScaleDtos(scales);

            if (pagingParams.isParamsSet())
            {
                Long scalesCount = scaleDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, scalesCount);
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

    public ScaleDto getScaleById(Long scaleId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(scaleId);
        return mScaleDtoFactory.create(scaleDao.getEntityById(scaleId));
    }

    // TODO convert for REST API
    private void uploadScalesFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Scale> sources = new ArrayList<>();
        for (ImportScale importScale : importHeader.getImportScales())
        {
            Scale source = scaleFactory.create(importScale);
            sources.add(source);
        }

        scaleDao.upload(sources);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}
