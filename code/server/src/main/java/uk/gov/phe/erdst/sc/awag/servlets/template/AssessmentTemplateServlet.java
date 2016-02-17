package uk.gov.phe.erdst.sc.awag.servlets.template;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentTemplateClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWTemplateInUseException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "template", urlPatterns = {"/template/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class AssessmentTemplateServlet extends HttpServlet
{
    @Inject
    private AssessmentTemplateController mAssessmentTemplateController;

    @Inject
    private AnimalController mAnimalController;

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private Validator mRequestValidator;

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        /**
         * parse url like expecting either:
         * http://localhost/animal-welfare-system-client/server/template/{templateId}
         * or
         * http://localhost/animal-welfare-system-client/server/template/{templateId}/parameter/{
         * parameterId}
         */
        String[] parts = request.getPathInfo().substring(1).split("/");

        ResponsePayload responsePayload = new ResponsePayload();

        if (ServletUtils.isDeleteTemplate(parts))
        {
            throw new UnsupportedOperationException("Deleting of templates is not implemented at present.");
        }
        else if (ServletUtils.isDeleteTemplateParameter(parts))
        {
            try
            {
                Long templateId = Long.parseLong(parts[0]);
                Long parameterId = Long.parseLong(parts[2]);

                mAssessmentTemplateController.deleteTemplateParameter(templateId, parameterId,
                    ServletSecurityUtils.getLoggedUser(request));
            }
            catch (NumberFormatException ex)
            {
                ServletUtils.setResponseClientError(response);
            }
            catch (AWNoSuchEntityException | AWTemplateInUseException ex)
            {
                ServletUtils.setResponseClientError(response);
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            ServletUtils.setResponseClientError(response);
        }

        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String requestTemplateJson = request.getParameter("template");
        AssessmentTemplateClientData clientData = (AssessmentTemplateClientData) mRequestConverter
            .convert(requestTemplateJson, AssessmentTemplateClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mAssessmentTemplateController.storeAssessmentTemplate(clientData, responsePayload,
            ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.getErrors().size() > 0)
        {
            ServletUtils.setResponseClientError(response);
        }
        else
        {
            ServletUtils.setResponseCreated(response);
        }
        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String assessmentTemplateJson = ServletUtils.getRequestBody(request);
        AssessmentTemplateClientData clientData = (AssessmentTemplateClientData) mRequestConverter
            .convert(assessmentTemplateJson, AssessmentTemplateClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long assessmentTemplateId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(assessmentTemplateId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mAssessmentTemplateController.updateAssessmentTemplate(assessmentTemplateId, clientData, responsePayload,
            ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.getErrors().size() > 0)
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String callback;
        ResponsePayload responsePayload = new ResponsePayload();
        ServletUtils.setJsonContentType(response);
        try
        {
            callback = ServletUtils.getCallbackParameter(request);
            try
            {
                String action = ServletUtils.getSelectActionParameter(request);
                executeAction(action, request, responsePayload);
            }
            catch (AWInvalidParameterException | AWInvalidResourceIdException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
                ServletUtils.setResponseClientError(response);
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

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException, AWInvalidResourceIdException, AWNoSuchEntityException
    {
        Object payload = ServletUtils.getNoResponsePayload();

        boolean includeMetadata = ServletUtils.getIncludeMetadataParameter(request);
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        switch (action)
        {
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                Long templateId = ServletUtils.getParseResourceId(ServletUtils.getSelectEntityIdParameter(request));
                payload = mAssessmentTemplateController.getAssessmentTemplateDtoById(templateId);
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_ALL:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessmentTemplateController.getAssessmentTemplatesDtos(offset, limit, responsePayload,
                        includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessmentTemplateController.getAssessmentTemplatesLikeDtos(
                        ServletUtils.getSelectLikeParameter(request), offset, limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_ANIMAL_TEMPLATE:
                String sAnimalId = ServletUtils.getAnimalIdParameter(request);
                Long lAnimalId = ServletUtils.getParseResourceId(sAnimalId);
                payload = mAnimalController.getAnimalAssessmentTemplateDto(lAnimalId);
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }

        responsePayload.setData(payload);
    }
}
