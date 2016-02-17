package uk.gov.phe.erdst.sc.awag.servlets.assessment;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.ws.rs.HttpMethod;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentsGetRequestParams;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "assessment", urlPatterns = {"/assessment/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class AssessmentServlet extends HttpServlet
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private AssessmentController mAssessmentController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private Validator mRequestValidator;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String assessment = request.getParameter("assessment");
        boolean isSubmit = Boolean.valueOf(request.getParameter(ServletConstants.REQ_PARAM_ASSESS_IS_SUBMIT));
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter.convert(assessment,
            AssessmentClientData.class);

        if (!ValidatorUtils.isResourceValid(clientData.id, HttpMethod.POST))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        ResponsePayload responsePayload = new ResponsePayload();

        mAssessmentController.store(clientData, isSubmit, responsePayload, ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.hasErrors())
        {
            ServletUtils.setResponseClientError(response);
        }
        else
        {
            ServletUtils.setResponseOk(response);
        }

        String responseJson = mResponseFormatter.toJson(responsePayload);
        ServletUtils.setResponseBody(response, responseJson);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletUtils.setJsonContentType(response);
        ResponsePayload responsePayload = new ResponsePayload();

        try
        {
            String callback = ServletUtils.getCallbackParameter(request);
            String action;
            try
            {
                action = ServletUtils.getSelectActionParameter(request);
                executeAction(action, request, responsePayload);
            }
            catch (AWInvalidParameterException | AWInvalidResourceIdException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
            finally
            {
                ServletUtils.printResponse(response, callback, mResponseFormatter.toJson(responsePayload));
            }
        }
        catch (AWInvalidParameterException ex)
        {
            responsePayload.addError(ex.getMessage());
            ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String requestJson = ServletUtils.getRequestBody(request);
        boolean isSubmit = Boolean.valueOf(request.getParameter(ServletConstants.REQ_PARAM_ASSESS_IS_SUBMIT));
        AssessmentClientData clientData = (AssessmentClientData) mRequestConverter.convert(requestJson,
            AssessmentClientData.class);

        ResponsePayload responsePayload = new ResponsePayload();
        Long assessmentId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(assessmentId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mAssessmentController.update(assessmentId, clientData, isSubmit, responsePayload,
            ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.hasErrors())
        {
            ServletUtils.setResponseClientError(response);
        }
        else
        {
            ServletUtils.setResponseOk(response);
        }

        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long assessmentId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(assessmentId, HttpMethod.DELETE))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        try
        {
            mAssessmentController.delete(assessmentId, ServletSecurityUtils.getLoggedUser(request));
        }
        catch (AWNoSuchEntityException ex)
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        ServletUtils.setResponseDeleteOk(response);
    }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException, AWInvalidResourceIdException
    {
        Object payload = ServletUtils.getNoResponse();
        boolean includeMetadata = ServletUtils.getIncludeMetadataParameter(request);
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        switch (action)
        {
            case ServletConstants.REQ_PARAM_ASSESS_BETWEEN:
                String dateFrom = ServletUtils.getDateFromParameter(request);
                String dateTo = ServletUtils.getDateToParameter(request);
                Long animalId = ServletUtils.getParseResourceId(ServletUtils.getAnimalIdParameter(request));

                validateGetAssessmentsBetweenRequest(dateFrom, dateTo, animalId, responsePayload);
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessmentController.getAnimalAssessmentsBetween(dateFrom, dateTo, animalId, offset,
                        limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_ASSESS_SEARCH:
                AssessmentSearchRequestParams params = extractAssessmentSearchParams(request);
                ValidatorUtils.validateRequest(params, responsePayload, mRequestValidator);
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessmentController.getAssessmentsPreviewDtos(params.animalId, params.dateFrom,
                        params.dateTo, params.userId, params.reasonId, params.studyId, params.isComplete, offset,
                        limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_ALL:
                // FIXME: the returned assessments are the business logic type, not DTO.
                // payload = mAssessmentController.getAssessments(offset, limit, responsePayload);
                throw new UnsupportedOperationException("Implement with DTOs");
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                payload = mAssessmentController.getAssessmentFullDto(
                    ServletUtils.getParseResourceId(ServletUtils.getSelectEntityIdParameter(request)), responsePayload);
                break;
            case ServletConstants.REQ_PARAM_COUNT:
                payload = mAssessmentController.getAssessmentsCount();
                break;
            case ServletConstants.REQ_PARAM_COUNT_BY_ANIMAL_ID:
                payload = mAssessmentController.getAssessmentsCountByAnimalId(ServletUtils
                    .getParseResourceId(ServletUtils.getAnimalIdParameter(request)));
                break;
            case ServletConstants.REQ_PARAM_COUNT_BY_TEMPLATE_ID:
                payload = mAssessmentController.getAssessmentsCountByTemplateId(ServletUtils
                    .getParseResourceId(ServletUtils.getTemplateIdParameter(request)));
                break;
            default:
                payload = ServletUtils.getNoResponse();
                break;
        }

        responsePayload.setData(payload);
    }

    private AssessmentSearchRequestParams extractAssessmentSearchParams(HttpServletRequest request)
        throws AWInvalidParameterException, AWInvalidResourceIdException
    {
        String dateFrom = ServletUtils.getDateFromParameter(request);
        String dateTo = ServletUtils.getDateToParameter(request);

        Long animalId = ServletUtils.getIdParameterFromString(ServletUtils.getParameter(request,
            ServletConstants.REQ_PARAM_ANIMAL_ID));

        Long reasonId = ServletUtils.getIdParameterFromString(ServletUtils.getParameter(request,
            ServletConstants.REQ_PARAM_REASON_ID));

        Long userId = ServletUtils.getIdParameterFromString(ServletUtils.getParameter(request,
            ServletConstants.REQ_PARAM_USER_ID));

        Long studyId = ServletUtils.getIdParameterFromString(ServletUtils.getParameter(request,
            ServletConstants.REQ_PARAM_STUDY_ID));

        Boolean isComplete = ServletUtils.getBooleanParameterFromString(ServletUtils.getParameter(request,
            ServletConstants.REQ_PARAM_ASSESS_IS_COMPLETE));

        AssessmentSearchRequestParams params = new AssessmentSearchRequestParams();
        params.animalId = animalId;
        params.reasonId = reasonId;
        params.userId = userId;
        params.studyId = studyId;
        params.dateFrom = dateFrom;
        params.dateTo = dateTo;
        params.isComplete = isComplete;

        return params;
    }

    private void validateGetAssessmentsBetweenRequest(String dateFrom, String dateTo, Long animalId,
        ResponsePayload responsePayload)
    {
        AssessmentsGetRequestParams assessmentsGetRequestParams = new AssessmentsGetRequestParams();
        assessmentsGetRequestParams.animalId = animalId;
        assessmentsGetRequestParams.dateFrom = dateFrom;
        assessmentsGetRequestParams.dateTo = dateTo;

        ValidatorUtils.validateRequest(assessmentsGetRequestParams, responsePayload, mRequestValidator);
    }
}
