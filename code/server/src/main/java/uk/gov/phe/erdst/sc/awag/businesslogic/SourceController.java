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
import uk.gov.phe.erdst.sc.awag.dao.SourceDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportSource;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.source.ImportSourceFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.source.SourceDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.source.SourceFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.SourceClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.source.SourceDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.source.SourcesDto;

@Stateless
public class SourceController
{
    @Inject
    private Validator mValidator;

    @Inject
    private SourceFactory mSourceFactory;

    @Inject
    private SourceDtoFactory mSourceDtoFactory;

    @Inject
    private SourceDao mSourceDao;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportSourceFactory importSourceFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_SOURCE)
    public EntityCreateResponseDto createSource(SourceClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Source source = mSourceFactory.create(clientData);
            source = mSourceDao.store(source);
            response.id = source.getId();
            response.value = source.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_SOURCE)
    public SourceDto updateSource(Long sourceId, SourceClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<SourceClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.sourceId, sourceId, ValidationConstants.UPDATE_ID_MISMATCH);

            Source source = mSourceDao.getSource(clientData.sourceId);
            mSourceFactory.update(source, clientData);
            source = mSourceDao.store(source);
            return mSourceDtoFactory.create(source);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public SourcesDto getAllSources(PagingQueryParams pagingParams) throws AWInputValidationException
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

            Collection<Source> sources = mSourceDao.getSources(offset, limit);
            SourcesDto response = new SourcesDto();
            response.sources = mSourceDtoFactory.createSourceDtos(sources);

            if (pagingParams.isParamsSet())
            {
                Long sourcesCount = mSourceDao.getCountSources();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, sourcesCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public SourcesDto getSourcesLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            List<Source> sources = mSourceDao.getSourcesLike(likeFilterParam.value, offset, limit);
            SourcesDto response = new SourcesDto();
            response.sources = mSourceDtoFactory.createSourceDtos(sources);

            if (pagingParams.isParamsSet())
            {
                Long sourcesCount = mSourceDao.getCountSourcesLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, sourcesCount);
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

    public SourceDto getSourceById(Long sourceId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(sourceId);
        Source source = mSourceDao.getSource(sourceId);
        return mSourceDtoFactory.create(source);
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_SOURCE)
    public ResponseDto uploadSource(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_SOURCE_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportSources(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportSource importSource = importSourceFactory.create(uploadCSVLineData);
                importHeader.addImportSource(importSource);
            }

            importHeader = importHeaderDao.store(importHeader);

            uploadSourcesFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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

    public Source getSourceByNameNonApiMethod(String sourceName) throws AWNoSuchEntityException
    {
        // TODO
        return mSourceDao.getSource(sourceName);
    }

    // TODO convert for REST API
    private void uploadSourcesFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Source> sources = new ArrayList<>();
        for (ImportSource importSource : importHeader.getImportSources())
        {
            Source source = mSourceFactory.create(importSource);
            sources.add(source);
        }

        mSourceDao.upload(sources);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
    }

}
