package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessment;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentParameterFactorScore;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.AssessmentScoreUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.CwasCalculator;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor.DatesBorderValues;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.UniqueEntitySelectCollectionDto;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.ImportAssessmentFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.service.factory.template.ParametersOrderingFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SavedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SubmittedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreValidator;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentCreateRequest;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentScoreComparisonRequest;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentUpdateRequest;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCountResponse;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentMinimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentScoreComparisonResultDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSearchResultWrapperDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSimpleWrapperDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentsDynamicSearchDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.PreviousAssessmentDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.PreviousAssessmentScoreComparisonDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;

@Stateless
public class AssessmentController
{
    @Inject
    private AssessmentPartsFactory mAssessmentPartsFactory;

    @Inject
    private AssessmentFactory mAssessmentFactory;

    @Inject
    private AssessmentDtoFactory mAssessmentDtoFactory;

    @Inject
    private AssessmentDao mAssessmentDao;

    @EJB
    private AssessmentTemplateController mAssessmentTemplateController;

    @Inject
    private Validator mValidator;

    @Inject
    private AssessmentUniqueValuesExtractor mAssessmentUniqueValuesExtractor;

    @Inject
    private ParametersOrderingFactory mParametersOrderingFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private AssessmentTemplateDao mAssessmentTemplateDao;

    @Inject
    private ImportHeaderDao mImportHeaderDao;

    @Inject
    private ImportAssessmentFactory mImportAssessmentFactory;

    @LoggedActivity(actionName = LoggedActions.CREATE_ASSESSMENT)
    public EntityCreateResponseDto createAssessment(AssessmentCreateRequest data, LoggedUser newApiLoggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWSeriousException
    {
        AssessmentScoreValidator.validateIsScoresVerifiedFlag(data.isScoresVerified);

        Class<?> validationGroup = (data.isSubmit ? SubmittedAssessment.class : SavedAssessment.class);

        AssessmentClientData clientData = data.assessment;

        Set<ConstraintViolation<AssessmentClientData>> assessmentConstraintViolations = mValidator.validate(clientData,
            validationGroup);

        if (assessmentConstraintViolations.isEmpty())
        {
            AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);

            AssessmentTemplate template = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(clientData.animalId);

            final boolean isComplete = data.isSubmit;
            Assessment assessment = mAssessmentFactory.create(clientData, template, assessmentParts, isComplete);
            mAssessmentDao.store(assessment);

            EntityCreateResponseDto response = new EntityCreateResponseDto();
            response.id = assessment.getId();
            response.value = assessment.getEntitySelectName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(assessmentConstraintViolations));
            return null;
        }
    }

    public AssessmentFullDto getAssessmentById(Long assessmentId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(assessmentId);
        return handleGetFullAssessmentDto(assessmentId);
    }

    public EntityCountResponse getAssessmentsCountByAnimalId(Long animalId) throws AWInputValidationException
    {
        ValidatorUtils.validateEntityId(animalId);
        return new EntityCountResponse(mAssessmentDao.getCountAnimalAssessments(animalId));
    }

    public EntityCountResponse getAssessmentsCountByTemplateId(Long templateId) throws AWInputValidationException
    {
        ValidatorUtils.validateEntityId(templateId);
        return new EntityCountResponse(mAssessmentDao.getCountAssessmentsByTemplateId(templateId));
    }

    public Long getAssessmentsCountByTemplateIdNonApiMethod(Long templateId)
    {
        return mAssessmentDao.getCountAssessmentsByTemplateId(templateId);
    }

    public EntityCountResponse getAssessmentsCountByCompleteness(boolean isComplete)
    {
        return new EntityCountResponse(mAssessmentDao.getAssessmentsByCompleteness(isComplete));
    }

    public EntityCountResponse getAssessmentsCount()
    {
        return new EntityCountResponse(mAssessmentDao.getCountAssessments());
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_ASSESSMENT)
    public void updateAssessment(Long assessmentId, AssessmentUpdateRequest data, LoggedUser newApiLoggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWSeriousException
    {
        ValidatorUtils.validateEntityId(assessmentId);

        Class<?> validationGroup = data.isSubmit ? SubmittedAssessment.class : SavedAssessment.class;

        AssessmentClientData clientData = data.assessment;

        Set<ConstraintViolation<AssessmentClientData>> assessmentConstraintViolations = mValidator.validate(clientData,
            validationGroup);

        if (assessmentConstraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.id, assessmentId, ValidationConstants.UPDATE_ID_MISMATCH);

            Assessment assessment = mAssessmentDao.getAssessment(assessmentId);

            AssessmentScoreValidator.validateIsNotCompleteOnUpdate(assessment);

            AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);
            final boolean isComplete = data.isSubmit;
            final AssessmentScore oldScore = assessment.getScore();
            AssessmentTemplate template = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(clientData.animalId);

            mAssessmentFactory.update(assessment, template, clientData, assessmentParts, isComplete);

            mAssessmentDao.deleteAssessmentScore(oldScore);
            mAssessmentDao.update(assessment);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(assessmentConstraintViolations));
        }
    }

    @LoggedActivity(actionName = LoggedActions.DELETE_ASSESSMENT)
    public void deleteAssessment(Long assessmentId, LoggedUser newApiLoggedUser)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(assessmentId);
        Assessment assessment = mAssessmentDao.getAssessment(assessmentId);
        mAssessmentDao.deleteAssessment(assessment);
    }

    public AssessmentSearchResultWrapperDto searchAssessments(Long animalId, String dateFrom, String dateTo,
        Long userId, Long reasonId, Long studyId, Boolean isComplete, PagingQueryParams pagingParams)
        throws AWInputValidationException
    {
        ValidatorUtils.validateOptionalEntityId(studyId);
        ValidatorUtils.validateOptionalEntityId(animalId);
        ValidatorUtils.validateOptionalEntityId(userId);
        ValidatorUtils.validateOptionalEntityId(reasonId);
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);

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

            Collection<Assessment> assessments = mAssessmentDao.getAssessments(animalId, dateFrom, dateTo, userId,
                reasonId, studyId, isComplete, offset, limit);

            AssessmentSearchResultWrapperDto response = mAssessmentDtoFactory
                .createAssessmentSearchResultWrapperDto(assessments);

            if (pagingParams.isParamsSet())
            {
                Long count = mAssessmentDao.getAssessmentsCount(animalId, dateFrom, dateTo, userId, reasonId, studyId,
                    isComplete);

                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, count);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public AssessmentsDynamicSearchDto searchAssessmentsAndGetUniqueSearchFilters(Long studyId, Long studyGroupId,
        Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId) throws AWInputValidationException
    {
        ValidatorUtils.validateOptionalEntityId(studyId);
        ValidatorUtils.validateOptionalEntityId(studyGroupId);
        ValidatorUtils.validateOptionalEntityId(animalId);
        ValidatorUtils.validateOptionalEntityId(userId);
        ValidatorUtils.validateOptionalEntityId(reasonId);
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);

        Collection<Assessment> assessments = Collections.emptyList();

        final boolean isFiltersSet = checkIsFiltersSet(studyId, studyGroupId, animalId, dateFrom, dateTo, userId,
            reasonId);

        assessments = mAssessmentDao.getAssessments(studyId, studyGroupId, animalId, dateFrom, dateTo, userId,
            reasonId);

        UniqueEntitySelectCollectionDto values;
        DatesBorderValues dateValues;

        final boolean isStudySet = studyId != null;

        if (isFiltersSet)
        {
            values = mAssessmentUniqueValuesExtractor.extract(assessments, isStudySet);
            dateValues = mAssessmentUniqueValuesExtractor.extractDatesBorderValues(assessments);
        }
        else
        {
            Collection<Assessment> empty = Collections.emptyList();
            values = mAssessmentUniqueValuesExtractor.extract(empty, isStudySet);
            dateValues = mAssessmentUniqueValuesExtractor.extractDatesBorderValues(empty);
        }

        Collection<AssessmentMinimalDto> assessmentDtos = mAssessmentDtoFactory
            .createMinimalAssessmentsDto(assessments);

        AssessmentsDynamicSearchDto dto = new AssessmentsDynamicSearchDto();
        dto.assessments = assessmentDtos;
        dto.filterValues = values;
        dto.dateOfFirstAssessment = dateValues.dateOfFirstAssessment;
        dto.dateOfLastAssessment = dateValues.dateOfLastAssessment;

        return dto;
    }

    public AssessmentSimpleWrapperDto searchCompleteAssessmentsForAnimalBetweenDates(String dateFrom, String dateTo,
        Long animalId, PagingQueryParams pagingParams) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(animalId);
        ValidatorUtils.validateDateParameter(dateFrom);
        ValidatorUtils.validateDateParameter(dateTo);

        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            final boolean isCompleteAssessments = true;

            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            List<Assessment> assessments = mAssessmentDao.getAnimalAssessmentsBetween(dateFrom, dateTo, animalId, true,
                offset, limit);

            AssessmentTemplate template = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(animalId);
            ParametersOrdering parametersOrdering = mParametersOrderingFactory.getParameterOrdering(template);

            AssessmentSimpleWrapperDto response = mAssessmentDtoFactory.createSimpleAssessmentsDto(assessments,
                parametersOrdering);

            if (pagingParams.isParamsSet())
            {
                Long assessmentsCount = mAssessmentDao.getCountAnimalAssessmentsBetween(dateFrom, dateTo, animalId,
                    isCompleteAssessments);

                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, assessmentsCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public AssessmentFullDto getPreviousAssessmentForAnimal(Long animalId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(animalId);

        Assessment prevAssessment = mAssessmentDao.getPreviousAssessment(animalId);
        if (prevAssessment != null)
        {
            return handleGetFullAssessmentDto(prevAssessment.getId());
        }

        return new AssessmentFullDto();
    }

    public PreviousAssessmentDto getPreviousAssessmentPreviewByDate(Long animalId, String assessmentDate,
        Long currentAssessmentId) throws AWInputValidationException, AWNoSuchEntityException
    {
        ValidatorUtils.validateEntityId(animalId);
        ValidatorUtils.validateEntityId(currentAssessmentId);
        ValidatorUtils.validateDateParameter(assessmentDate);

        Assessment prevAssessment = mAssessmentDao.getPreviousAssessmentByDate(animalId, assessmentDate,
            currentAssessmentId);

        if (prevAssessment != null)
        {
            AssessmentTemplate prevAssessmentTemplate = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(prevAssessment.getAnimal().getId());
            ParametersOrdering parametersOrdering = mParametersOrderingFactory
                .getParameterOrdering(prevAssessmentTemplate);

            return mAssessmentDtoFactory.createPreviousAssessmentDto(prevAssessment, parametersOrdering);
        }

        return new PreviousAssessmentDto();
    }

    public Collection<Assessment> getAssessments(Long[] ids)
    {
        return mAssessmentDao.getAssessmentsByIds(ids);
    }

    public double getCwasForAssessment(AssessmentFullDto assessmentFullDto)
    {
        return CwasCalculator.calculateCwas(assessmentFullDto);
    }

    public PreviousAssessmentScoreComparisonDto compareAssessmentScores(AssessmentScoreComparisonRequest data)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        Long previousAssessmentId = data.previousAssessmentId;
        AssessmentClientData newAssessment = data.newAssessmentData;

        ValidatorUtils.validateEntityId(previousAssessmentId);
        Set<ConstraintViolation<AssessmentClientData>> assessmentConstraintViolations = mValidator
            .validate(newAssessment, SavedAssessment.class);

        if (assessmentConstraintViolations.isEmpty())
        {
            Assessment previousAssessment = mAssessmentDao.getAssessment(previousAssessmentId);

            AssessmentTemplate previousAssessmentTemplate = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(previousAssessment.getAnimal().getId());

            AssessmentTemplate newAssessmentTemplate = mAssessmentTemplateController
                .getAssessmentTemplateByAnimalIdNonApiMethod(newAssessment.animalId);

            ParametersOrdering parametersOrdering = mParametersOrderingFactory
                .getParameterOrdering(newAssessmentTemplate);

            if (!previousAssessmentTemplate.equals(newAssessmentTemplate))
            {
                ValidatorUtils
                    .throwInputValidationExceptionWith(ValidationConstants.ERR_TEMPLATES_DIFFERENT_ON_SCORES_COMPARE);
            }

            AssessmentScoreComparisonResultDto comparisonResult = AssessmentScoreUtils
                .isAssessmentScoreEqual(previousAssessment.getScore(), newAssessment, parametersOrdering);

            comparisonResult.convertResultToCollection();

            return new PreviousAssessmentScoreComparisonDto(comparisonResult);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(assessmentConstraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_ASSESSMENT)
    public ResponseDto uploadAssessment(Long assessmentTemplateId, InputStream uploadFile, LoggedUser loggedUser)
        throws AWInputValidationException
    {
        try
        {
            final String uploadAssessmentHeaderColumn[] = getAssessmentTemplateUploadParameters(assessmentTemplateId);
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                uploadAssessmentHeaderColumn);

            ImportHeader importHeader = storeImportAssessments(loggedUser, assessmentTemplateId, csvLinesData);
            importHeader = mImportHeaderDao.store(importHeader);

            uploadAssessmentsFromImport(importHeader, loggedUser);
        }
        catch (AWNonUniqueException | AWNoSuchEntityException | AWSeriousException ex) // TODO check this result
        {
            throw new AWInputValidationException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT);
        }

        return new UploadResponseDto();
    }

    private AssessmentFullDto handleGetFullAssessmentDto(Long assessmentId) throws AWNoSuchEntityException
    {
        Assessment assessment = mAssessmentDao.getAssessment(assessmentId);
        AssessmentTemplate template = mAssessmentTemplateController
            .getAssessmentTemplateByAnimalIdNonApiMethod(assessment.getAnimal().getId());

        ParametersOrdering parametersOrdering = mParametersOrderingFactory.getParameterOrdering(template);
        return mAssessmentDtoFactory.createAssessmentFullDto(assessment, parametersOrdering);
    }

    private boolean checkIsFiltersSet(Object... filters)
    {
        for (Object filter : filters)
        {
            if (filter != null)
            {
                return true;
            }
        }

        return false;
    }

    private ImportHeader storeImportAssessments(LoggedUser loggedUser, Long assessmentTemplateId,
        ArrayList<String[]> csvLinesData) throws AWNonUniqueException
    {
        // TODO verify all animals are the same assessment - to ensure consistency of the system

        ImportHeader importHeader = importHeaderFactory.createWithImportAssessment(loggedUser);
        for (String[] csvLine : csvLinesData)
        {
            ImportAssessment importAssessment = mImportAssessmentFactory.create(assessmentTemplateId, csvLine);
            importHeader.addImportAssessment(importAssessment);
        }

        return importHeader;
    }

    private void uploadAssessmentsFromImport(ImportHeader importHeader, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWSeriousException, AWNonUniqueException
    {
        // TODO all the same order
        Collection<Assessment> assessments = new ArrayList<>();

        HashMap<Long, String> parameterToIdMap = new LinkedHashMap<>();

        // TODO is there a quicker way? Need a template- copy object.. and inefficient
        // Receives in the correct order of display
        for (ImportAssessment importAssessment : importHeader.getImportAssessments())
        {
            AssessmentTemplate assessmentTemplate = mAssessmentTemplateDao
                .getReference(importAssessment.getImportassessmenttemplateid());

            AssessmentClientData clientData = createClientData(importAssessment, assessmentTemplate, parameterToIdMap);

            for (ImportAssessmentParameterFactorScore score : importAssessment
                .getImportAssessmentParameterFactorScores())
            {
                String parameterId = parameterToIdMap.get(score.getParameterNumber());
                updateClientFactorScore(parameterId, score, clientData);
                clientData.parameterComments.put(parameterId, score.getParameterComments());
            }

            AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData, loggedUser);

            Assessment assessment = mAssessmentFactory.create(clientData, assessmentTemplate, assessmentParts,
                importAssessment.getIscomplete());
            assessments.add(assessment);
        }

        mAssessmentDao.upload(assessments);

        mImportHeaderDao.realDelete(importHeader.getImportheaderid());
    }

    private String[] getAssessmentTemplateUploadParameters(Long assessmentTemplateId) throws AWNoSuchEntityException
    {
        List<String> headerColumn = new ArrayList<>(
            Arrays.asList(Constants.Upload.UPLOAD_HEADER_PREFIX_ASSESSMENT_COLUMNS));

        final String[] suffixUploadHeadeColumns = mAssessmentTemplateDao
            .getAssessmentTemplateSuffixUploadHeaders(assessmentTemplateId);
        headerColumn.addAll(Arrays.asList(suffixUploadHeadeColumns));
        String[] assessmentCSVColumnHeaders = headerColumn.toArray(new String[headerColumn.size()]);
        return assessmentCSVColumnHeaders;
    }

    private AssessmentClientData createClientData(ImportAssessment importAssessment,
        AssessmentTemplate assessmentTemplate, HashMap<Long, String> parameterToIdMap)
    {
        AssessmentClientData clientData = new AssessmentClientData();
        clientData.id = Constants.UNASSIGNED_ID;
        clientData.animalId = importAssessment.getAnimalnumberid();
        clientData.reason = importAssessment.getAssessmentreasonName();
        clientData.animalHousing = importAssessment.getAnimalHousingName();
        clientData.performedBy = importAssessment.getPerformedByUser();
        clientData.date = importAssessment.getDateAssessment();
        clientData.averageScores = new LinkedHashMap<>();
        clientData.parameterComments = new LinkedHashMap<>();
        clientData.score = new LinkedHashMap<>();

        Long parameterNumber = 1L;
        for (AssessmentTemplateParameter parameter : assessmentTemplate.getAssessmentTemplateParameters())
        {
            String parameterId = parameter.getId().getParameterId().toString();
            clientData.averageScores.put(parameterId, 0D);
            clientData.parameterComments.put(parameterId, Constants.EMPTY_STRING);
            HashMap<String, AssessmentClientFactor> mapClientFactors = new LinkedHashMap<>();
            clientData.score.put(parameterId, mapClientFactors);
            parameterToIdMap.put(parameterNumber, parameterId);
            parameterNumber++;
        }

        for (AssessmentTemplateParameterFactor factor : assessmentTemplate.getAssessmentTemplateParameterFactors())
        {
            String parameterId = factor.getId().getParameterId().toString();
            String factorId = factor.getId().getFactorId().toString();
            AssessmentClientFactor clientFactor = new AssessmentClientFactor();
            clientFactor.isIgnored = true;
            clientFactor.score = 0;
            clientData.score.get(parameterId).put(factorId, clientFactor);
        }

        return clientData;
    }

    private void updateClientFactorScore(String parameterId, ImportAssessmentParameterFactorScore score,
        AssessmentClientData clientData)
    {
        String[] scoreNumber = score.getFactorScores().split(",");

        int scoreIndex = 0;
        for (Map.Entry<String, AssessmentClientFactor> clientFactorEntry : clientData.score.get(parameterId).entrySet())
        {
            AssessmentClientFactor clientFactor = clientFactorEntry.getValue();

            try
            {
                String number = scoreNumber[scoreIndex];
                clientFactor.isIgnored = false;
                clientFactor.score = Integer.parseInt(number.trim());
                scoreIndex++;
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
                clientFactor.isIgnored = true;
            }
        }
    }
}
