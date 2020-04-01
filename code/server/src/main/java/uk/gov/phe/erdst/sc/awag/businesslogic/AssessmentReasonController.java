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

import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentReasonDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentReasonFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.ImportAssessmentReasonFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.reason.AssessmentReasonDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.reason.AssessmentReasonsDto;

@Stateless
public class AssessmentReasonController
{
    @Inject
    private AssessmentReasonFactory mAssessmentReasonFactory;

    @Inject
    private AssessmentReasonDtoFactory mAssessmentReasonDtoFactory;

    @Inject
    private AssessmentReasonDao mAssessmentReasonDao;

    @Inject
    private Validator mValidator;

    @Inject
    private ImportHeaderDao mImportHeaderDao;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportAssessmentReasonFactory mImportAssessmentReasonFactory;

    @LoggedActivity(actionName = LoggedActions.CREATE_REASON)
    public EntityCreateResponseDto createReason(AssessmentReasonClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            AssessmentReason reason = mAssessmentReasonFactory.create(clientData);
            reason = mAssessmentReasonDao.store(reason);
            response.id = reason.getId();
            response.value = reason.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public AssessmentReasonsDto getAllReasons(PagingQueryParams pagingParams) throws AWInputValidationException
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

            AssessmentReasonsDto response = new AssessmentReasonsDto();

            Collection<AssessmentReason> assessmentReasons = mAssessmentReasonDao.getEntities(offset, limit);
            response.reasons = mAssessmentReasonDtoFactory.createAssessmentReasonDtos(assessmentReasons);

            if (pagingParams.isParamsSet())
            {
                Long assessmentReasonsCount = mAssessmentReasonDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, assessmentReasonsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public AssessmentReasonsDto getReasonsLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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
            AssessmentReasonsDto response = new AssessmentReasonsDto();

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            List<AssessmentReason> reasons = mAssessmentReasonDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            response.reasons = mAssessmentReasonDtoFactory.createAssessmentReasonDtos(reasons);

            if (pagingParams.isParamsSet())
            {
                Long reasonsCount = mAssessmentReasonDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, reasonsCount);
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

    @LoggedActivity(actionName = LoggedActions.UPDATE_REASON)
    public AssessmentReasonDto updateReason(Long reasonId, AssessmentReasonClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<AssessmentReasonClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.reasonId, reasonId, ValidationConstants.UPDATE_ID_MISMATCH);

            AssessmentReason reason = mAssessmentReasonDao.getEntityById(reasonId);
            mAssessmentReasonFactory.update(reason, clientData);
            reason = mAssessmentReasonDao.update(reason);
            return mAssessmentReasonDtoFactory.createAssessmentReasonDto(reason);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public AssessmentReasonDto getReasonById(Long reasonId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(reasonId);
        AssessmentReason reason = mAssessmentReasonDao.getEntityById(reasonId);
        return mAssessmentReasonDtoFactory.createAssessmentReasonDto(reason);
    }

    public AssessmentReason getReasonNonApiMethod(String reasonName) throws AWNoSuchEntityException
    {
        return mAssessmentReasonDao.getEntityByNameField(reasonName);
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_REASON)
    public ResponseDto uploadReason(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_ASSESSMENT_REASON_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportAssessmentReason(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportAssessmentReason importAssessmentReason = mImportAssessmentReasonFactory
                    .create(uploadCSVLineData);
                importHeader.addImportAssessmentReason(importAssessmentReason);
            }

            importHeader = mImportHeaderDao.store(importHeader);

            uploadAssessmentReasonFromImport(importHeader); // TODO this code to be moved in the client (test for now)

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
    private void uploadAssessmentReasonFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<AssessmentReason> reasons = new ArrayList<>();
        for (ImportAssessmentReason importAssessmentReason : importHeader.getImportAssessmentReasons())
        {
            AssessmentReason reason = mAssessmentReasonFactory.create(importAssessmentReason);
            reasons.add(reason);
        }

        mAssessmentReasonDao.upload(reasons);

        mImportHeaderDao.realDelete(importHeader.getImportheaderid());
    }

    public AssessmentReason createAssessmentReasonNonApi(AssessmentReasonClientData clientData, LoggedUser loggedUser)
        throws AWNonUniqueException
    {
        // TODO unit test
        AssessmentReason reason = mAssessmentReasonFactory.create(clientData);
        reason = mAssessmentReasonDao.store(reason);
        return reason;
    }

}
