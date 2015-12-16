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

import uk.gov.phe.erdst.sc.awag.datamodel.client.LogonDetails;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet({"/logondetails"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class LogonDetailsServlet extends HttpServlet
{
    @Inject
    ResponseFormatter mResponseFormatter;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String username = ServletSecurityUtils.getLoggedInUserName(request);

        LogonDetails logonDetails = new LogonDetails();
        logonDetails.username = username;
        // logonDetails.username = "No Security";

        String callback;
        ServletUtils.setJsonContentType(response);
        ResponsePayload responsePayload = new ResponsePayload();
        try
        {
            callback = ServletUtils.getCallbackParameter(request);
            responsePayload.setData(logonDetails);
            ServletUtils.printResponse(response, callback, mResponseFormatter.toJson(responsePayload));
        }
        catch (AWInvalidParameterException ex)
        {
            responsePayload.addError(ex.getMessage());
            ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
        }
    }
}
