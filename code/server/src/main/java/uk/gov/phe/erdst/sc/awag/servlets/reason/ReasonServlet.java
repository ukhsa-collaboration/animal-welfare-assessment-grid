package uk.gov.phe.erdst.sc.awag.servlets.reason;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentReasonController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;
@SuppressWarnings("serial")
@WebServlet(name = "reason", urlPatterns = {"/reason/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class ReasonServlet extends HttpServlet
{
    @Inject
    private AssessmentReasonController mAssessReasonController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private Validator mRequestValidator;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException
    {
        String reasonJson = request.getParameter("reason");
        AssessmentReasonClientData clientData = (AssessmentReasonClientData) mRequestConverter.convert(reasonJson,
            AssessmentReasonClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mAssessReasonController.storeReason(clientData, responsePayload);

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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String reasonJson = ServletUtils.getRequestBody(request);
        AssessmentReasonClientData clientData = (AssessmentReasonClientData) mRequestConverter.convert(reasonJson,
            AssessmentReasonClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long reasonId = ServletUtils.getResourceId(request);

        if (!ValidatorUtils.isResourceValid(reasonId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mAssessReasonController.updateReason(reasonId, clientData, responsePayload);

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
        ServletUtils.setJsonContentType(response);
        ResponsePayload responsePayload = new ResponsePayload();
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
            case ServletConstants.REQ_PARAM_SELECT_ACTION_ALL:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessReasonController.getReasons(offset, limit, responsePayload, includeMetadata);
                }

                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                Long reasonId = ServletUtils.getParseResourceId(ServletUtils.getSelectEntityIdParameter(request));
                payload = mAssessReasonController.getReason(reasonId);
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                String like = ServletUtils.getSelectLikeParameter(request);
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mAssessReasonController.getReasonsLike(like, offset, limit, responsePayload,
                        includeMetadata);
                }
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }

        responsePayload.setData(payload);
    }
}
