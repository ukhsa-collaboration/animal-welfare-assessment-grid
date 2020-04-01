package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gson.Gson;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentsExportClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentFullDto;

@RequestScoped
public class AssessmentExporter
{
    private static final String DOWNLOAD_STATUS_COOKIE_NAME = "assessmentsExportDownloadStatus";

    @Inject
    private AssessmentController mAssessmentController;

    @Inject
    private StudyGroupController mStudyGroupController;

    @Inject
    private AssessmentDtoFactory mAssessmentDtoFactory;

    @Inject
    private Validator mRequestValidator;

    @Inject
    private AssessmentExportOutputter mAssessmentExportOutputter;

    @LoggedActivity(actionName = LoggedActions.EXPORT_ASSESSMENTS)
    public void exportData(String rawAssessmentIds, HttpServletResponse response, LoggedUser newApiLoggedUser)
        throws AWInputValidationException, IOException
    {
        Gson gson = new Gson();
        Long[] idsParsed = gson.fromJson(rawAssessmentIds, Long[].class);

        AssessmentsExportClientData exportRequest = new AssessmentsExportClientData();
        exportRequest.ids = idsParsed;

        Set<ConstraintViolation<AssessmentsExportClientData>> validationViolations = mRequestValidator
            .validate(exportRequest);

        if (validationViolations.isEmpty())
        {
            Collection<Assessment> assessments = mAssessmentController.getAssessments(exportRequest.ids);

            AssessmentsExport export = new AssessmentsExport(assessments.size());

            Long prevTemplateId = null;
            ParametersOrdering parametersOrdering = null;

            for (Assessment assessment : assessments)
            {
                Animal animal = assessment.getAnimal();
                AssessmentTemplate template = animal.getAssessmentTemplate();
                Long templateId = template.getId();

                if (prevTemplateId != null)
                {
                    if (!prevTemplateId.equals(templateId))
                    {
                        String errorMsg = "Encountered different assessment templates during export."
                            + " Such exports are currently not supported. Please choose a dataset that is based on"
                            + " a single assessment template and try again.";

                        ValidatorUtils.throwInputValidationExceptionWith(errorMsg);
                    }
                }
                else
                {
                    prevTemplateId = templateId;
                }

                if (parametersOrdering == null)
                {
                    parametersOrdering = getParameterOrdering(template);
                    export.parametersOrdering = parametersOrdering;
                }

                AssessmentFullDto dto = mAssessmentDtoFactory.createAssessmentFullDto(assessment,
                    parametersOrdering);

                StudyGroup group = null;
                Study study = assessment.getStudy();

                if (study != null)
                {
                    group = mStudyGroupController.getStudyGroupWithAnimalNonApiMethod(animal, study);
                }

                double cwas = mAssessmentController.getCwasForAssessment(dto);

                AssessmentsExportEntry entry = new AssessmentsExportEntry();
                entry.dto = dto;
                entry.studyGroup = group;
                entry.cwas = cwas;

                export.add(entry);
            }

            mAssessmentExportOutputter.outputExport(response, export);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(validationViolations));
            return;
        }
    }

    private ParametersOrdering getParameterOrdering(AssessmentTemplate template)
    {
        ParametersOrdering ordering = new ParametersOrdering();

        Set<Long> orderedIds = new LinkedHashSet<>();
        List<AssessmentTemplateParameterFactor> parameterFactors = template.getAssessmentTemplateParameterFactors();

        for (AssessmentTemplateParameterFactor pf : parameterFactors)
        {
            orderedIds.add(pf.getParameter().getId());
        }

        int i = 0;
        for (Long id : orderedIds)
        {
            ordering.add(id, i);
            i++;
        }

        return ordering;
    }

    public String getDownloadStatusCookieName()
    {
        return DOWNLOAD_STATUS_COOKIE_NAME;
    }

    public String getDownloadStatusCookieValue()
    {
        return Constants.WebApi.DEFAULT_DOWNLOAD_STATUS_COOKIE_VALUE;
    }

    public int getDownloadStatusCookieExpirationTimeSeconds()
    {
        return Constants.WebApi.DEFAULT_DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC;
    }
}
