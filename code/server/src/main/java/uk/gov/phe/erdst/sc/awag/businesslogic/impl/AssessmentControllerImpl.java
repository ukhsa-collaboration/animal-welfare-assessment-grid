package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentSearchPreviewDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentsDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentsDynamicSearchDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.dto.assessment.PreviousAssessmentDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWAssessmentCreationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.CwasCalculator;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor.DatesBorderValues;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.UniqueEntitySelectCollectionDto;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentPartsFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.impl.AssessmentPartsFactoryImpl.AssessmentParts;
import uk.gov.phe.erdst.sc.awag.service.factory.template.ParametersOrderingFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SavedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SubmittedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Stateless
public class AssessmentControllerImpl implements AssessmentController
{
    @Inject
    private AssessmentPartsFactory mAssessmentPartsFactory;

    @Inject
    private AssessmentFactory mAssessmentFactory;

    @Inject
    private AssessmentDtoFactory mAssessmentDtoFactory;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private AssessmentDao mAssessmentDao;

    @EJB
    private AssessmentTemplateController mAssessmentTemplateController;

    @Inject
    private Validator mAssessmentValidator;

    @Inject
    private ResponsePager mResponsePager;

    @Inject
    private AssessmentUniqueValuesExtractor mAssessmentUniqueValuesExtractor;

    @Inject
    private CwasCalculator mCwasCalculator;

    @Inject
    private ParametersOrderingFactory mParametersOrderingFactory;

    @Override
    public Long getAssessmentsCount()
    {
        return mAssessmentDao.getCountAssessments();
    }

    @Override
    public Long getAssessmentsCountByCompleteness(boolean isComplete)
    {
        return mAssessmentDao.getAssessmentsByCompleteness(isComplete);
    }

    @Override
    public Collection<Assessment> getAssessments(Integer offset, Integer limit)
    {
        return mAssessmentDao.getAssessments(offset, limit);
    }

    @Override
    public Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId,
        Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit)
    {
        return mAssessmentDao.getAssessments(animalId, dateFrom, dateTo, userId, reasonId, studyId, isComplete, offset,
            limit);
    }

    @Override
    public Collection<AssessmentSearchPreviewDto> getAssessmentsPreviewDtos(Long animalId, String dateFrom,
        String dateTo, Long userId, Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        Collection<Assessment> assessments = getAssessments(animalId, dateFrom, dateTo, userId, reasonId, studyId,
            isComplete, offset, limit);

        if (includeMetadata)
        {
            Long count = mAssessmentDao.getAssessmentsCount(animalId, dateFrom, dateTo, userId, reasonId, studyId,
                isComplete);

            mResponsePager.setPagingTotalsMetadata(offset, limit, count, responsePayload);
        }

        Collection<AssessmentSearchPreviewDto> dtos = new ArrayList<>(assessments.size());
        for (Assessment assessment : assessments)
        {
            dtos.add(mAssessmentDtoFactory.createAssessmentSearchPreviewDto(assessment));
        }

        return dtos;
    }

    @Override
    public AssessmentsDynamicSearchDto getAssessmentsDynamicSearchDto(Long studyId, Long studyGroupId, Long animalId,
        String dateFrom, String dateTo, Long userId, Long reasonId, ResponsePayload responsePayload)
    {
        Collection<Assessment> assessments = Collections.emptyList();

        final boolean isFiltersSet = checkIsFiltersSet(studyId, studyGroupId, animalId, dateFrom, dateTo, userId,
            reasonId);

        assessments = mAssessmentDao
            .getAssessments(studyId, studyGroupId, animalId, dateFrom, dateTo, userId, reasonId);

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

        Collection<EntitySelectDto> assessmentDtos = mEntitySelectDtoFactory.createEntitySelectDtos(assessments);

        AssessmentsDynamicSearchDto dto = new AssessmentsDynamicSearchDto();
        dto.assessments = assessmentDtos;
        dto.filterValues = values;
        dto.dateOfFirstAssessment = dateValues.dateOfFirstAssessment;
        dto.dateOfLastAssessment = dateValues.dateOfLastAssessment;

        return dto;
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

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_ASSESSMENT)
    public void store(AssessmentClientData clientData, boolean isSubmit, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Class<?> validationGroup = isSubmit ? SubmittedAssessment.class : SavedAssessment.class;

        Set<ConstraintViolation<AssessmentClientData>> violations = mAssessmentValidator.validate(clientData,
            validationGroup);

        if (violations.isEmpty())
        {
            try
            {
                storeAssessment(clientData, isSubmit);
            }
            catch (AWAssessmentCreationException | AWNoSuchEntityException e)
            {
                responsePayload.addError(e.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(violations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_ASSESSMENT)
    public void update(Long assessmentId, AssessmentClientData clientData, boolean isSubmit,
        ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Class<?> validationGroup = isSubmit ? SubmittedAssessment.class : SavedAssessment.class;
        Set<ConstraintViolation<AssessmentClientData>> constraintViolations = mAssessmentValidator.validate(clientData,
            validationGroup);

        if (constraintViolations.isEmpty())
        {
            try
            {
                Assessment assessment = mAssessmentDao.getAssessment(assessmentId);

                if (assessment.isComplete())
                {
                    responsePayload.addError(ValidationConstants.ERR_ASSESSMENT_UPDATE_COMPLETED_ATTEMPT);
                    return;
                }

                AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);
                final boolean isComplete = isSubmit;
                final AssessmentScore oldScore = assessment.getScore();
                AssessmentTemplate template = mAssessmentTemplateController
                    .getAssessmentTemplateByAnimalId(clientData.animalId);

                mAssessmentFactory.update(assessment, template, clientData, assessmentParts, isComplete);

                mAssessmentDao.deleteAssessmentScore(oldScore);
                mAssessmentDao.update(assessment);
            }
            catch (AWNoSuchEntityException | AWAssessmentCreationException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(constraintViolations);
        }
    }

    @Override
    public PreviousAssessmentDto getPreviousAssessment(Long animalId)
    {
        return handleGetPreviousAssessment(animalId, null, null);
    }

    @Override
    public PreviousAssessmentDto getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId)
    {
        return handleGetPreviousAssessment(animalId, date, currentAssessmentId);
    }

    private PreviousAssessmentDto handleGetPreviousAssessment(Long animalId, String date, Long currentAssessmentId)
    {
        Assessment prevAssessment = null;

        if (date == null)
        {
            prevAssessment = mAssessmentDao.getPreviousAssessment(animalId);
        }
        else
        {
            prevAssessment = mAssessmentDao.getPreviousAssessmentByDate(animalId, date, currentAssessmentId);
        }

        if (prevAssessment != null)
        {
            return mAssessmentDtoFactory.createPreviousAssessmentDto(prevAssessment);
        }

        return new PreviousAssessmentDto();
    }

    private void storeAssessment(AssessmentClientData clientData, boolean isSubmit)
        throws AWAssessmentCreationException, AWNoSuchEntityException
    {
        AssessmentParts assessmentParts = mAssessmentPartsFactory.create(clientData);

        AssessmentTemplate template = mAssessmentTemplateController
            .getAssessmentTemplateByAnimalId(clientData.animalId);

        final boolean isComplete = isSubmit;
        Assessment assessment = mAssessmentFactory.create(clientData, template, assessmentParts, isComplete);
        mAssessmentDao.store(assessment);
    }

    @Override
    public AssessmentsDto getAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId, Integer offset,
        Integer limit, ResponsePayload responsePayload, boolean includeMetadata) throws AWNoSuchEntityException
    {
        List<Assessment> assessments = mAssessmentDao.getAnimalAssessmentsBetween(dateFrom, dateTo, animalId, true,
            offset, limit);

        if (includeMetadata)
        {
            Long assessmentsCount = mAssessmentDao.getCountAnimalAssessmentsBetween(dateFrom, dateTo, animalId);

            mResponsePager.setPagingTotalsMetadata(offset, limit, assessmentsCount, responsePayload);
        }

        AssessmentTemplate template = mAssessmentTemplateController.getAssessmentTemplateByAnimalId(animalId);
        ParametersOrdering parametersOrdering = mParametersOrderingFactory.getParameterOrdering(template);

        AssessmentsDto assessmentsDto = mAssessmentDtoFactory.createAssessmentsDto(assessments, parametersOrdering);

        return assessmentsDto;
    }

    @Override
    public AssessmentFullDto getAssessmentFullDto(Long assessmentId, ResponsePayload responsePayload)
    {
        try
        {
            Assessment assessment = mAssessmentDao.getAssessment(assessmentId);
            AssessmentTemplate template = mAssessmentTemplateController.getAssessmentTemplateByAnimalId(assessment
                .getAnimal().getId());
            ParametersOrdering parametersOrdering = mParametersOrderingFactory.getParameterOrdering(template);
            return mAssessmentDtoFactory.createAssessmentFullDto(assessment, parametersOrdering);
        }
        catch (AWNoSuchEntityException e)
        {
            responsePayload.addError(e.getMessage());
        }

        return new AssessmentFullDto();
    }

    @Override
    public Long getAssessmentsCountByAnimalId(Long animalId)
    {
        return mAssessmentDao.getCountAnimalAssessments(animalId);
    }

    @Override
    public Long getAssessmentsCountByTemplateId(Long templateId)
    {
        return mAssessmentDao.getCountAssessmentsByTemplateId(templateId);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.DELETE_ASSESSMENT)
    public void delete(Long assessmentId, LoggedUser loggedUser) throws AWNoSuchEntityException
    {
        Assessment assessment = mAssessmentDao.getAssessment(assessmentId);
        mAssessmentDao.deleteAssessment(assessment);
    }

    @Override
    public Collection<Assessment> getAssessments(Long[] ids)
    {
        return mAssessmentDao.getAssessmentsByIds(ids);
    }

    @Override
    public double getCwasForAssessment(AssessmentFullDto assessmentFullDto)
    {
        return mCwasCalculator.calculateCwas(assessmentFullDto);
    }
}
