package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterFactorDao;
import uk.gov.phe.erdst.sc.awag.dao.FactorDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterPK;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.template.ImportTemplateFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.FactorClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.ParameterClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCountResponse;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.template.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.template.AssessmentTemplatesDto;

@Stateless
public class AssessmentTemplateController
{
    @Inject
    private AssessmentTemplateDao mAssessmentTemplateDao;

    @EJB
    private AssessmentController mAssessmentController;

    @Inject
    private AssessmentTemplateParameterDao mAssessmentTemplateParameterDao;

    @Inject
    private AssessmentTemplateParameterFactorDao mAssessmentTemplateParameterFactorDao;

    @Inject
    private ParameterDao mParameterDao;

    @Inject
    private FactorDao mFactorDao;

    @Inject
    private ScaleDao mScaleDao;

    @Inject
    private AssessmentTemplateFactory mAssessmentTemplateFactory;

    @Inject
    private ParameterFactory mParameterFactory;

    @Inject
    private FactorFactory mFactorFactory;

    @Inject
    private AssessmentTemplateDtoFactory mAssessmentTemplateDtoFactory;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportTemplateFactory importTemplateFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @Inject
    private Validator mValidator;

    @LoggedActivity(actionName = LoggedActions.CREATE_ASSESSMENT_TEMPLATE)
    public AssessmentTemplateDto createAssessmentTemplate(AssessmentTemplateClientData clientData,
        LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        Set<ConstraintViolation<AssessmentTemplateClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            AssessmentTemplate assessmentTemplate = mAssessmentTemplateFactory.create(clientData);
            assessmentTemplate.setScale(mScaleDao.getEntityById(clientData.templateScale));
            storeAssessmentTemplateParameters(assessmentTemplate, clientData.templateParameters);
            assessmentTemplate = mAssessmentTemplateDao.store(assessmentTemplate);
            return mAssessmentTemplateDtoFactory.create(assessmentTemplate);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_ASSESSMENT_TEMPLATE)
    public AssessmentTemplateDto updateAssessmentTemplate(Long assessmentTemplateId,
        AssessmentTemplateClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWNoSuchEntityException, AWSeriousException
    {
        Set<ConstraintViolation<AssessmentTemplateClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.templateId, assessmentTemplateId,
                ValidationConstants.UPDATE_ID_MISMATCH);

            AssessmentTemplate assessmentTemplate = mAssessmentTemplateDao.getEntityById(assessmentTemplateId);
            mAssessmentTemplateFactory.update(assessmentTemplate, clientData);

            // check for assessments using this template
            long assessmentCount = mAssessmentController
                .getAssessmentsCountByTemplateIdNonApiMethod(assessmentTemplateId);

            if (assessmentCount == 0)
            {
                // update the template, scale and parameter factors
                Scale scale = mScaleDao.getEntityById(clientData.templateScale);
                assessmentTemplate.setScale(scale);
                storeAssessmentTemplateParameters(assessmentTemplate, clientData.templateParameters);
                AssessmentTemplate mergedTemplate = mAssessmentTemplateDao.update(assessmentTemplate);
                return mAssessmentTemplateDtoFactory.create(mergedTemplate);
            }
            else
            {
                Collection<String> errors = new ArrayList<>(2);

                // check for changes in scale
                if (hasChangesToScale(clientData, assessmentTemplate))
                {
                    errors.add(ValidationConstants.ERR_TEMPLATE_IN_USE_UPD_SCALE);
                }

                // check for changes in parameter factors.
                if (hasChangesToParameterFactors(clientData, assessmentTemplate))
                {
                    errors.add(ValidationConstants.ERR_TEMPLATE_IN_USE_UPD_PARAMS);
                }

                // if there are no changes it is safe to update the template with just the name
                if (errors.isEmpty())
                {
                    AssessmentTemplate templateWithNameChanged = mAssessmentTemplateDao.update(assessmentTemplate);
                    return mAssessmentTemplateDtoFactory.create(templateWithNameChanged);
                }

                ValidatorUtils.throwInputValidationExceptionWith(errors);
                return null;
            }
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.DELETE_ASSESSMENT_TEMPLATE_PARAMETER)
    public void deleteTemplateParameter(Long templateId, Long parameterId, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(templateId);
        ValidatorUtils.validateEntityId(parameterId);

        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(templateId);

        long assessmentCount = mAssessmentController.getAssessmentsCountByTemplateIdNonApiMethod(templateId);

        if (assessmentCount == 0)
        {
            for (AssessmentTemplateParameterFactor atpf : template.getAssessmentTemplateParameterFactors())
            {
                if (atpf.getParameter().getId().equals(parameterId))
                {
                    mAssessmentTemplateParameterFactorDao.removeEntity(atpf);
                }
            }

            // TODO integration test - refactor?
            Parameter parameter = mParameterDao.getReference(parameterId);
            AssessmentTemplateParameterPK templateParameterPK = mAssessmentTemplateFactory
                .getAssessmentTemplateParameterPK(template, parameter);
            AssessmentTemplateParameter assessmentTemplateParameter = mAssessmentTemplateParameterDao
                .getAssessmentTemplateParameterById(templateParameterPK);
            mAssessmentTemplateParameterDao.removeEntity(assessmentTemplateParameter);

        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(ValidationConstants.ERR_TEMPLATE_IN_USE_DEL);
        }

    }

    public AssessmentTemplateDto getAssessmentTemplateById(Long templateId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(templateId);
        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(templateId);
        return mAssessmentTemplateDtoFactory.create(template);
    }

    public AssessmentTemplatesDto getAllAssessmentTemplates(PagingQueryParams pagingParams)
        throws AWInputValidationException
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

            Collection<AssessmentTemplate> assessmentTemplates = mAssessmentTemplateDao.getEntities(offset, limit);
            AssessmentTemplatesDto response = new AssessmentTemplatesDto();
            response.templates = mAssessmentTemplateDtoFactory.create(assessmentTemplates);

            if (pagingParams.isParamsSet())
            {
                Long assessmentTemplatesCount = mAssessmentTemplateDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, assessmentTemplatesCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public AssessmentTemplatesDto getAssessmentTemplatesLike(LikeFilterParam likeFilterParam,
        PagingQueryParams pagingParams) throws AWInputValidationException
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

            List<AssessmentTemplate> assessmentTemplates = mAssessmentTemplateDao.getEntitiesLike(likeFilterParam.value,
                offset, limit);

            AssessmentTemplatesDto response = new AssessmentTemplatesDto();
            response.templates = mAssessmentTemplateDtoFactory.create(assessmentTemplates);

            if (pagingParams.isParamsSet())
            {
                Long assessmentTemplatesCount = mAssessmentTemplateDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, assessmentTemplatesCount);
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

    @LoggedActivity(actionName = LoggedActions.UPLOAD_ASSESSMENT_TEMPLATE)
    public ResponseDto uploadAssessmentTemplate(InputStream uploadFile, LoggedUser loggedUser)
        throws AWInputValidationException
    {
        try
        {
            final String[] uploadHeaderParameterColumns = getTemplateUploadParameters();

            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                uploadHeaderParameterColumns);

            ImportHeader importHeader = importHeaderFactory.createWithImportTemplate(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                // TODO validate the import - through the database?

                ImportTemplate importAssessmentTemplate = importTemplateFactory.create(uploadCSVLineData);
                importHeader.addImportTemplate(importAssessmentTemplate);
            }

            importHeader = importHeaderDao.store(importHeader);

            uploadAssessmentTemplateFromImport(importHeader);

        }
        catch (AWNonUniqueException ex)
        {
            // TODO needed?
            throw new AWInputValidationException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT);
        }

        return new UploadResponseDto();
    }

    public AssessmentTemplateDto getAssessmentTemplateByAnimalId(Long animalId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(animalId);
        AssessmentTemplate template = getAssessmentTemplateByAnimalIdNonApiMethod(animalId);
        return mAssessmentTemplateDtoFactory.create(template);
    }

    public AssessmentTemplate getAssessmentTemplateByAnimalIdNonApiMethod(Long animalId) throws AWNoSuchEntityException
    {
        // TODO
        return mAssessmentTemplateDao.getAssessmentTemplateByAnimalId(animalId);
    }

    public AssessmentTemplate getAssessmentTemplateByIdNonApiMethod(Long assessmentTemplateId)
        throws AWNoSuchEntityException
    {
        // TODO
        return mAssessmentTemplateDao.getEntityById(assessmentTemplateId);
    }

    public AssessmentTemplate getAssessmentTemplateByNameNonApiMethod(String assessmentTemplateName)
        throws AWNoSuchEntityException
    {
        // TODO
        return mAssessmentTemplateDao.getEntityByNameField(assessmentTemplateName);
    }

    public EntityCountResponse getAssessmentTemplatesCount()
    {
        return new EntityCountResponse(mAssessmentTemplateDao.getCountAssessmentTemplates());
    }

    // TODO convert for REST API
    private void uploadAssessmentTemplateFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        return; // TODO

        // Collection<Import>

        /*
        Collection<Source> sources = new ArrayList<>();
        for (ImportSource importSource : importHeader.getImportSources())
        {
            Source source = mAssessmentTemplateFactory.create(importAssessmentTemplate);
            sources.add(source);
        }

        mSourceDao.upload(sources);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
        */
    }

    private String[] getTemplateUploadParameters()
    {
        List<String> headerColumn = new ArrayList<>(
            Arrays.asList(Constants.Upload.UPLOAD_HEADER_PREFIX_TEMPLATE_PARAMETER_COLUMNS));

        final String[] suffixUploadHeadeColumns = UploadUtils
            .retrieveTemplateUploadSuffixHeader(Constants.Upload.MIN_TEMPLATE_PARAMETER_FACTOR_COLUMNS);
        headerColumn.addAll(Arrays.asList(suffixUploadHeadeColumns));
        String[] templateCSVColumnHeaders = headerColumn.toArray(new String[headerColumn.size()]);
        return templateCSVColumnHeaders;

    }

    private boolean hasChangesToParameterFactors(AssessmentTemplateClientData clientData,
        AssessmentTemplate existingTemplate)
    {
        ArrayList<AssessmentTemplateParameterFactor> templateParameterFactors = new ArrayList<>();
        for (ParameterClientData parameterClientData : clientData.templateParameters)
        {
            Parameter parameter = mParameterFactory.create(parameterClientData);
            for (FactorClientData factorClientData : parameterClientData.parameterFactors)
            {
                Factor factor = mFactorFactory.create(factorClientData);

                AssessmentTemplateParameterFactorPK id = mAssessmentTemplateFactory
                    .getAssessmentTemplateParameterFactorPK(existingTemplate, parameterClientData, factorClientData);

                AssessmentTemplateParameterFactor assessmentTemplateParameterFactor = mAssessmentTemplateFactory
                    .getAssessmentTemplateParameterFactor(existingTemplate, parameter, factor, id);

                templateParameterFactors.add(assessmentTemplateParameterFactor);
            }
        }

        return ((templateParameterFactors.size() != existingTemplate.getAssessmentTemplateParameterFactors().size())
            || !existingTemplate.getAssessmentTemplateParameterFactors().containsAll(templateParameterFactors));
    }

    private boolean hasChangesToScale(AssessmentTemplateClientData clientData, AssessmentTemplate existingTemplate)
    {
        return (!existingTemplate.getScale().getId().equals(clientData.templateScale));
    }

    // TODO refactor
    private void storeAssessmentTemplateParameter(AssessmentTemplate assessmentTemplate,
        ParameterClientData parameterClientData) throws AWNonUniqueException
    {
        AssessmentTemplateParameterPK templateParameterPK = mAssessmentTemplateFactory
            .getAssessmentTemplateParameterPK(assessmentTemplate, parameterClientData);
        AssessmentTemplateParameter assessmentTemplateParameter = mAssessmentTemplateParameterDao
            .getAssessmentTemplateParameterById(templateParameterPK);

        if (assessmentTemplateParameter == null)
        {
            Parameter parameter = mParameterDao.getReference(parameterClientData.parameterId);
            AssessmentTemplateParameter assessmentTemplateParameterNew = mAssessmentTemplateFactory
                .getAssessmentTemplateParameter(assessmentTemplate, parameter, templateParameterPK);
            mAssessmentTemplateParameterDao.store(assessmentTemplateParameterNew);
        }
    }

    private void storeAssessmentTemplateParameters(AssessmentTemplate assessmentTemplate,
        Set<ParameterClientData> templateParameters) throws AWNonUniqueException
    {
        // build template parameter factor
        for (ParameterClientData parameterClientData : templateParameters)
        {
            storeAssessmentTemplateParameter(assessmentTemplate, parameterClientData);

            for (FactorClientData factorClientData : parameterClientData.parameterFactors)
            {
                AssessmentTemplateParameterFactorPK id = mAssessmentTemplateFactory
                    .getAssessmentTemplateParameterFactorPK(assessmentTemplate, parameterClientData, factorClientData);
                AssessmentTemplateParameterFactor templateParameterFactor = mAssessmentTemplateParameterFactorDao
                    .getAssessmentTemplateParameterFactorById(id);

                // if it isn't already in the database add it
                if (templateParameterFactor == null)
                {
                    AssessmentTemplateParameterFactor assessmentTemplateParameterFactor = mAssessmentTemplateFactory
                        .getAssessmentTemplateParameterFactor(assessmentTemplate,
                            mParameterDao.getReference(parameterClientData.parameterId),
                            mFactorDao.getReference(factorClientData.factorId), id);

                    // persist the assessment parameter factor
                    mAssessmentTemplateParameterFactorDao.store(assessmentTemplateParameterFactor);

                    // add back to the template once persisted.
                    assessmentTemplate.addAssessmentTemplateParameterFactor(assessmentTemplateParameterFactor);
                }
            }

        }
    }

}
