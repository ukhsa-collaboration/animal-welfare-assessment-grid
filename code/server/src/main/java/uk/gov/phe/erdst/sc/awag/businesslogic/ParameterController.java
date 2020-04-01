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
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ImportParameterFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.ParameterClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParametersDto;

@Stateless
public class ParameterController
{
    @Inject
    private ParameterDao mParameterDao;

    @Inject
    private Validator mValidator;

    @Inject
    private ParameterFactory mParameterFactory;

    @Inject
    private ParameterDtoFactory mParameterDtoFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportParameterFactory importParameterFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_PARAMETER)
    public EntityCreateResponseDto createParameter(ParameterClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<ParameterClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();

            Parameter parameter = mParameterFactory.create(clientData);
            parameter = mParameterDao.store(parameter);
            response.id = parameter.getId();
            response.value = parameter.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public ParametersDto getAllParameters(PagingQueryParams pagingParams) throws AWInputValidationException
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

            ParametersDto response = new ParametersDto();

            Collection<Parameter> parameters = mParameterDao.getEntities(offset, limit);
            response.parameters = mParameterDtoFactory.createParameterDtos(parameters);

            if (pagingParams.isParamsSet())
            {
                Long parameterCount = mParameterDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, parameterCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public ParameterDto getParameterById(Long parameterId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(parameterId);
        Parameter parameter = mParameterDao.getEntityById(parameterId);
        return mParameterDtoFactory.create(parameter);
    }

    public ParametersDto getParametersLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            ParametersDto response = new ParametersDto();

            List<Parameter> parameters = mParameterDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            response.parameters = mParameterDtoFactory.createParameterDtos(parameters);

            if (pagingParams.isParamsSet())
            {
                Long parameterCount = mParameterDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, parameterCount);
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

    @LoggedActivity(actionName = LoggedActions.UPDATE_PARAMETER)
    public ParameterDto updateParameter(Long parameterId, ParameterClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<ParameterClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.parameterId, parameterId,
                ValidationConstants.UPDATE_ID_MISMATCH);

            Parameter parameter = mParameterDao.getEntityById(clientData.parameterId);
            mParameterFactory.update(parameter, clientData);
            parameter = mParameterDao.store(parameter);
            return mParameterDtoFactory.create(parameter);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_PARAMETER)
    public ResponseDto uploadParameter(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_PARAMETER_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportParameter(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportParameter importParameter = importParameterFactory.create(uploadCSVLineData);
                importHeader.addImportParameter(importParameter);
            }

            importHeader = importHeaderDao.store(importHeader);

            uploadParametersFromImport(importHeader);

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
    private void uploadParametersFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Parameter> Parameters = new ArrayList<>();
        for (ImportParameter importParameter : importHeader.getImportParameters())
        {
            Parameter Parameter = mParameterFactory.create(importParameter);
            Parameters.add(Parameter);
        }

        mParameterDao.upload(Parameters);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}
