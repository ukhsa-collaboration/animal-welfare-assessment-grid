package uk.gov.phe.erdst.sc.awag.servlets.authentication;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.UserAuthController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWDeleteAdminUserException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "user-auth", urlPatterns = {"/user-auth/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class UserAuthServlet extends HttpServlet
{
    @Inject
    private RequestConverter mRequestConverter;
    @Inject
    private ResponseFormatter mResponseFormatter;
    @Inject
    private Validator mRequestValidator;
    @Inject
    private UserAuthController mUserAuthController;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
            catch (AWInvalidParameterException | AWNoSuchEntityException ex)
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
            ServletUtils.setResponseClientError(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException
    {
        String userAuthJson = request.getParameter("user-auth");
        UserAuthClientData clientData = (UserAuthClientData) mRequestConverter.convert(userAuthJson,
            UserAuthClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mUserAuthController.storeUser(clientData, responsePayload, ServletSecurityUtils.getLoggedUser(request));

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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userAuthJson = ServletUtils.getRequestBody(request);
        UserAuthClientData clientData = (UserAuthClientData) mRequestConverter.convert(userAuthJson,
            UserAuthClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        String username = ServletUtils.getStringResourceId(request);

        mUserAuthController.updateUser(username, clientData, responsePayload,
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException
    {
        ResponsePayload responsePayload = new ResponsePayload();
        String username = ServletUtils.getStringResourceId(request);

        try
        {
            mUserAuthController.deleteUser(username, ServletSecurityUtils.getLoggedUser(request));
        }
        catch (AWNoSuchEntityException | AWDeleteAdminUserException ex)
        {
            ServletUtils.setResponseClientError(response);
            responsePayload.addError(ex.getMessage());
        }

        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException, AWNoSuchEntityException
    {
        Object payload = ServletUtils.getNoResponsePayload();
        boolean includeMetadata = ServletUtils.getIncludeMetadataParameter(request);
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        switch (action)
        {
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mUserAuthController.getUserAuthDtosLike(ServletUtils.getSelectLikeParameter(request),
                        offset, limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                String username = ServletUtils.getSelectEntityIdParameter(request);
                payload = mUserAuthController.getUserAuthById(username);
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }
        responsePayload.setData(payload);
    }

}
