package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentsExportClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;

@RequestScoped
public class AssessmentExporter implements Exporter
{
    private static final String DOWNLOAD_STATUS_COOKIE_NAME = "assessmentsExportDownloadStatus";

    @Inject
    private RequestConverter mRequestConverter;

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

    private AssessmentsExportClientData mClientData;

    @Override
    public void processParameters(HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException
    {
        String exportData = request.getParameter(ServletConstants.REQ_PARAM_EXPORT_DATA);
        mClientData = (AssessmentsExportClientData) mRequestConverter.convert(exportData,
            AssessmentsExportClientData.class);

        ValidatorUtils.validateRequest(mClientData, responsePayload, mRequestValidator);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.EXPORT_ASSESSMENTS)
    public void export(HttpServletResponse response, ResponsePayload responsePayload, LoggedUser loggedUser)
        throws IOException
    {
        Collection<Assessment> assessments = mAssessmentController.getAssessments(mClientData.ids);

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
                        + " Such exports are currently not supported.";
                    responsePayload.addError(errorMsg
                        + " Please choose a dataset that is based on a single assessment template and try again.");

                    throw new IllegalArgumentException(errorMsg);
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

            AssessmentFullDto dto = mAssessmentDtoFactory.createAssessmentFullDto(assessment, parametersOrdering);

            StudyGroup group = null;
            Study study = assessment.getStudy();

            if (study != null)
            {
                group = mStudyGroupController.getStudyGroup(animal, study);
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

    @Override
    public String getDownloadStatusCookieName()
    {
        return DOWNLOAD_STATUS_COOKIE_NAME;
    }

    @Override
    public String getDownloadStatusCookieValue()
    {
        return ServletConstants.DEFAULT_DOWNLOAD_STATUS_COOKIE_VALUE;
    }

    @Override
    public int getDownloadStatusCookieExpirationTimeSeconds()
    {
        return ServletConstants.DEFAULT_DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC;
    }
}
