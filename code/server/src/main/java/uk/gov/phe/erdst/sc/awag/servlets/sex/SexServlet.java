package uk.gov.phe.erdst.sc.awag.servlets.sex;

import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.phe.erdst.sc.awag.businesslogic.SexController;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet({"/sex"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class SexServlet extends HttpServlet
{
    @Inject
    SexController mSexController;
    @Inject
    ResponseFormatter mResponseFormatter;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String callback;
        ServletUtils.setJsonContentType(response);
        ResponsePayload responsePayload = new ResponsePayload();
        try
        {
            callback = ServletUtils.getCallbackParameter(request);
            Collection<Sex> sexes = mSexController.getSexes();
            responsePayload.setData(sexes);
            ServletUtils.printResponse(response, callback, mResponseFormatter.toJson(responsePayload));
        }
        catch (AWInvalidParameterException ex)
        {
            responsePayload.addError(ex.getMessage());

            ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
        }
    }
}
