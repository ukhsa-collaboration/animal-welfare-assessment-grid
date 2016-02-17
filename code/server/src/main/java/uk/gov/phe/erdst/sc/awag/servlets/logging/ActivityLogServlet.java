package uk.gov.phe.erdst.sc.awag.servlets.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.businesslogic.ActivityLogController;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ActivityLogSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

import com.opencsv.CSVWriter;

@SuppressWarnings("serial")
@WebServlet(name = "activitylog", urlPatterns = {"/activitylog/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class ActivityLogServlet extends HttpServlet
{
    private static final int DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC = 10;
    private static final String DOWNLOAD_STATUS_COOKIE_VALUE = "finished";
    private static final String DOWNLOAD_STATUS_COOKIE_NAME = "activityLogDownloadStatus";
    private static final String DOWNLOAD_STATUS_COOKIE_PATH = "/";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT);
    private static final Logger LOGGER = LogManager.getLogger(ActivityLogServlet.class.getName());

    @Inject
    private ActivityLogController mActivityLogController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private Validator mRequestValidator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        ResponsePayload responsePayload = new ResponsePayload();

        try
        {
            String dateFrom = ServletUtils.getDateFromParameter(request);
            String dateTo = ServletUtils.getDateToParameter(request);

            ActivityLogSearchRequestParams params = new ActivityLogSearchRequestParams();
            params.dateFrom = dateFrom;
            params.dateTo = dateTo;

            validateRequest(offset, limit, params, responsePayload);

            if (!responsePayload.hasErrors())
            {
                setDownloadStatusCookie(response);
                processGetRequest(response, params, offset, limit, responsePayload);
            }
            else
            {
                handleError(response, responsePayload);
            }

        }
        catch (AWInvalidParameterException | IOException e)
        {
            LOGGER.error(e);
            handleError(response, responsePayload);
        }
    }

    private void handleError(HttpServletResponse response, ResponsePayload responsePayload) throws IOException
    {
        Object payload = ServletUtils.getNoResponse();
        responsePayload.setData(payload);
        ServletUtils.setResponseClientError(response);
        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    private void validateRequest(Integer offset, Integer limit, ActivityLogSearchRequestParams params,
        ResponsePayload responsePayload)
    {
        ValidatorUtils.validateRequest(params, responsePayload, mRequestValidator);
        ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
            mRequestValidator);
    }

    private void processGetRequest(HttpServletResponse response, ActivityLogSearchRequestParams params, Integer offset,
        Integer limit, ResponsePayload responsePayload) throws AWInvalidParameterException, IOException
    {
        List<ActivityLog> logs = mActivityLogController.getActivityLogsBetween(params.dateFrom, params.dateTo, offset,
            limit);

        String fileName = getCsvFileName();

        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        writeCsv(logs, fileName, response.getOutputStream());
    }

    private void setDownloadStatusCookie(HttpServletResponse response)
    {
        Cookie cookie = new Cookie(DOWNLOAD_STATUS_COOKIE_NAME, DOWNLOAD_STATUS_COOKIE_VALUE);
        cookie.setMaxAge(DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC);
        cookie.setPath(DOWNLOAD_STATUS_COOKIE_PATH);
        cookie.setHttpOnly(false);

        response.addCookie(cookie);
    }

    private String getCsvFileName()
    {
        String date = DATE_FORMATTER.format(new Date(System.currentTimeMillis()));
        return String.format("%s-%s-%s.csv", Constants.OUTPUT_FILES_PREFIX, "activity-logs", date);
    }

    private void writeCsv(List<ActivityLog> logs, String fileName, OutputStream output) throws IOException
    {
        BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(output, Constants.OUTPUT_ENCODING_UTF_8));
        CSVWriter csvWriter = new CSVWriter(buffWriter);

        // Column headers
        csvWriter.writeNext(new String[] {"Date and time", "Action", "Username"});

        for (ActivityLog log : logs)
        {
            String[] entries = new String[] {log.getDateTime(), log.getAction(), log.getUsername()};
            csvWriter.writeNext(entries);
        }

        csvWriter.close();
    }
}
