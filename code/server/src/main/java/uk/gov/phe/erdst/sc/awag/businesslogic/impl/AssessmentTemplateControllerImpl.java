package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterFactorDao;
import uk.gov.phe.erdst.sc.awag.dao.FactorDao;
import uk.gov.phe.erdst.sc.awag.dao.ParameterDao;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.FactorClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ParameterClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentTemplateDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWTemplateInUseException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateFactory;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Stateless
public class AssessmentTemplateControllerImpl implements AssessmentTemplateController
{
    @Inject
    private AssessmentTemplateDao mAssessmentTemplateDao;

    @EJB
    private AssessmentController mAssessmentController;

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
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Inject
    private Validator mAssessmentTemplateValidator;

    @Override
    public Collection<AssessmentTemplateDto> getAssessmentTemplatesDtos(Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        Collection<AssessmentTemplate> assessmentTemplates = mAssessmentTemplateDao.getEntities(offset, limit);

        if (includeMetadata)
        {
            Long assessmentTemplatesCount = mAssessmentTemplateDao.getEntityCount();
            mResponsePager.setPagingTotalsMetadata(offset, limit, assessmentTemplatesCount, responsePayload);
        }

        return mAssessmentTemplateDtoFactory.create(assessmentTemplates);
    }

    @Override
    public AssessmentTemplate getAssessmentTemplateByAnimalId(Long animalId) throws AWNoSuchEntityException
    {
        return mAssessmentTemplateDao.getAssessmentTemplateByAnimalId(animalId);
    }

    @Override
    public AssessmentTemplateDto getAssessmentTemplateDtoByAnimalId(Long animalId) throws AWNoSuchEntityException
    {
        AssessmentTemplate template = getAssessmentTemplateByAnimalId(animalId);
        return mAssessmentTemplateDtoFactory.create(template);
    }

    @Override
    public List<EntitySelectDto> getAssessmentTemplatesLikeDtos(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<AssessmentTemplate> assessmentTemplates = mAssessmentTemplateDao.getEntitiesLike(like, offset, limit);

        if (includeMetadata)
        {
            Long assessmentTemplatesCount = mAssessmentTemplateDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, assessmentTemplatesCount, responsePayload);
        }

        return mEntitySelectDtoFactory.createEntitySelectDtos(assessmentTemplates);
    }

    @Override
    public AssessmentTemplateDto getAssessmentTemplateDtoById(Long templateId) throws AWNoSuchEntityException
    {
        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(templateId);
        return mAssessmentTemplateDtoFactory.create(template);
    }

    @Override
    public void storeAssessmentTemplate(AssessmentTemplateClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<AssessmentTemplateClientData>> violations = mAssessmentTemplateValidator
            .validate(clientData);

        if (violations.isEmpty())
        {
            AssessmentTemplate assessmentTemplate = mAssessmentTemplateFactory.create(clientData);

            try
            {
                assessmentTemplate.setScale(mScaleDao.getEntityById(clientData.templateScale));
                storeAssessmentTemplateParameters(assessmentTemplate, clientData.templateParameters);
                mAssessmentTemplateDao.store(assessmentTemplate);
                AssessmentTemplateDto assessmentTemplateDto = mAssessmentTemplateDtoFactory.create(assessmentTemplate);
                responsePayload.setData(assessmentTemplateDto);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(violations);
        }
    }

    @Override
    public void updateAssessmentTemplate(Long assessmentTemplateId, AssessmentTemplateClientData clientData,
        ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<AssessmentTemplateClientData>> violations = mAssessmentTemplateValidator
            .validate(clientData);

        if (violations.isEmpty())
        {
            try
            {
                AssessmentTemplate assessmentTemplate = mAssessmentTemplateDao.getEntityById(assessmentTemplateId);
                mAssessmentTemplateFactory.update(assessmentTemplate, clientData);

                // check for assessments using this template
                long assessmentCount = mAssessmentController.getAssessmentsCountByTemplateId(assessmentTemplateId);
                if (assessmentCount == 0)
                {
                    // update the template, scale and parameter factors
                    Scale scale = mScaleDao.getEntityById(clientData.templateScale);
                    assessmentTemplate.setScale(scale);
                    storeAssessmentTemplateParameters(assessmentTemplate, clientData.templateParameters);
                    AssessmentTemplate mergedTemplate = mAssessmentTemplateDao.update(assessmentTemplate);
                    AssessmentTemplateDto assessmentTemplateDto = mAssessmentTemplateDtoFactory.create(mergedTemplate);
                    responsePayload.setData(assessmentTemplateDto);
                }
                else
                {
                    // check for changes in scale
                    if (hasChangesToScale(clientData, assessmentTemplate))
                    {
                        responsePayload.addError(ValidationConstants.ERR_TEMPLATE_IN_USE_UPD_SCALE);
                    }

                    // check for changes in parameter factors.
                    if (hasChangesToParameterFactors(clientData, assessmentTemplate))
                    {
                        responsePayload.addError(ValidationConstants.ERR_TEMPLATE_IN_USE_UPD_PARAMS);
                    }

                    // if there are no changes it is safe to update the template with just the name
                    if (!responsePayload.hasErrors())
                    {
                        AssessmentTemplate templateWithNameChanged = mAssessmentTemplateDao.update(assessmentTemplate);
                        AssessmentTemplateDto assessmentTemplateDto = mAssessmentTemplateDtoFactory
                            .create(templateWithNameChanged);
                        responsePayload.setData(assessmentTemplateDto);
                    }
                }
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {

                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(violations);
        }
    }

    private boolean hasChangesToParameterFactors(AssessmentTemplateClientData clientData,
        AssessmentTemplate existingTemplate)
    {
        ArrayList<AssessmentTemplateParameterFactor> templateParameterFactors = new ArrayList<AssessmentTemplateParameterFactor>();
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

        return ((templateParameterFactors.size() != existingTemplate.getAssessmentTemplateParameterFactors().size()) || !existingTemplate
            .getAssessmentTemplateParameterFactors().containsAll(templateParameterFactors));
    }

    private boolean hasChangesToScale(AssessmentTemplateClientData clientData, AssessmentTemplate existingTemplate)
    {
        return (!existingTemplate.getScale().getId().equals(clientData.templateScale));
    }

    private void storeAssessmentTemplateParameters(AssessmentTemplate assessmentTemplate,
        Set<ParameterClientData> templateParameters) throws AWNonUniqueException
    {
        // build template parameter factor
        for (ParameterClientData parameterClientData : templateParameters)
        {
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

    @Override
    public void deleteTemplateParameter(Long templateId, Long parameterId) throws AWNoSuchEntityException,
        AWTemplateInUseException
    {
        AssessmentTemplate template = mAssessmentTemplateDao.getEntityById(templateId);

        long assessmentCount = mAssessmentController.getAssessmentsCountByTemplateId(templateId);

        if (assessmentCount == 0)
        {
            for (AssessmentTemplateParameterFactor atpf : template.getAssessmentTemplateParameterFactors())
            {
                if (atpf.getParameter().getId().equals(parameterId))
                {
                    mAssessmentTemplateParameterFactorDao.removeEntity(atpf);
                }
            }
        }
        else
        {
            throw new AWTemplateInUseException(ValidationConstants.ERR_TEMPLATE_IN_USE_DEL);
        }

    }
}
