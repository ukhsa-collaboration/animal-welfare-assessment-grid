package uk.gov.phe.erdst.sc.awag.servlets;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.export.ActivityLogExporter;
import uk.gov.phe.erdst.sc.awag.service.export.AssessmentExporter;
import uk.gov.phe.erdst.sc.awag.service.export.Exporter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "exportdata", urlPatterns = {"/exportdata/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class ExportDataServlet extends HttpServlet
{
    private static final String DOWNLOAD_STATUS_COOKIE_PATH = "/";
    private static final Logger LOGGER = LogManager.getLogger(ExportDataServlet.class.getName());

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String exportType = request.getParameter(ServletConstants.REQ_PARAM_EXPORT_TYPE);

        ResponsePayload responsePayload = new ResponsePayload();

        Exporter exporter = null;

        try
        {
            exporter = produceExporter(exportType);
            exporter.processParameters(request, responsePayload);

            if (!responsePayload.hasErrors())
            {
                setDownloadStatusCookie(response, exporter);
                exporter.export(response, responsePayload, ServletSecurityUtils.getLoggedUser(request));
            }
            else
            {
                handleError(response, responsePayload, ServletUtils.getNoResponse());
            }

        }
        catch (AWInvalidParameterException | IllegalArgumentException | IOException e)
        {
            LOGGER.error(e);
            responsePayload.addError(e.getMessage());
            String errorData = "There was an error when exporting assessments."
                + " Please send the AWAG software development team the error message.";
            handleError(response, responsePayload, errorData);
        }
        finally
        {
            if (exporter != null)
            {
                CDI.current().destroy(exporter);
            }
        }
    }

    private Exporter produceExporter(String exportType)
    {
        switch (exportType)
        {
            case ServletConstants.EXPORT_TYPE_ACTIVITY_LOGS:
                return CDI.current().select(ActivityLogExporter.class).get();
            case ServletConstants.EXPORT_TYPE_ASSESSMENTS:
                return CDI.current().select(AssessmentExporter.class).get();
            default:
                throw new IllegalArgumentException("Unknown export type:" + exportType);
        }
    }

    private void handleError(HttpServletResponse response, ResponsePayload responsePayload, String errorData)
        throws IOException
    {
        responsePayload.setData(errorData);
        ServletUtils.setResponseClientError(response);
        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    private void setDownloadStatusCookie(HttpServletResponse response, Exporter exporter)
    {
        Cookie cookie = new Cookie(exporter.getDownloadStatusCookieName(), exporter.getDownloadStatusCookieValue());
        cookie.setMaxAge(exporter.getDownloadStatusCookieExpirationTimeSeconds());
        cookie.setPath(DOWNLOAD_STATUS_COOKIE_PATH);
        cookie.setHttpOnly(false);

        response.addCookie(cookie);
    }
}
