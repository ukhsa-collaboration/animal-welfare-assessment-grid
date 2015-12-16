package uk.gov.phe.erdst.sc.awag.servlets.source;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.SourceController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SourceClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "source", urlPatterns = {"/source/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class SourceServlet extends HttpServlet
{
    @Inject
    private SourceController mSourceController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private Validator mRequestValidator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        ResponsePayload responsePayload = new ResponsePayload();
        ServletUtils.setJsonContentType(response);
        try
        {
            String callback = ServletUtils.getCallbackParameter(request);
            try
            {
                String action = ServletUtils.getSelectActionParameter(request);
                executeAction(action, request, responsePayload);
            }
            catch (AWInvalidParameterException ex)
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String sourceJson = ServletUtils.getRequestBody(request);
        SourceClientData clientData = (SourceClientData) mRequestConverter.convert(sourceJson, SourceClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long sourceId = ServletUtils.getResourceId(request);

        if (!ValidatorUtils.isResourceValid(sourceId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mSourceController.updateSource(sourceId, clientData, responsePayload);

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException
    {
        String requestSourceJson = request.getParameter("source");
        SourceClientData clientData = (SourceClientData) mRequestConverter.convert(requestSourceJson,
            SourceClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mSourceController.storeSource(clientData, responsePayload);

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

    // XXX: Delete functionality is not to be enabled for now.
    // @Override
    // protected void doDelete(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException
    // {
    // Long sourceId;
    // try
    // {
    // sourceId = Long.parseLong(ServletUtils.getResourceId(request));
    //
    // }
    // catch (NumberFormatException ex)
    // {
    // ServletUtils.setResponseClientError(response);
    // return;
    // }
    //
    // mSourceController.deleteSource(sourceId);
    //
    // ServletUtils.setResponseDeleteOk(response);
    // }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException
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
                    payload = mSourceController.getSources(offset, limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mSourceController.getSourcesLikeDtos(ServletUtils.getSelectLikeParameter(request),
                        offset, limit, responsePayload, includeMetadata);
                }
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }

        responsePayload.setData(payload);
    }
}
