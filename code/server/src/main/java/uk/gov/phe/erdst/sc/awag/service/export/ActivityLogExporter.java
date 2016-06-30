package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.ActivityLogController;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ActivityLogSearchRequestParams;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

import com.opencsv.CSVWriter;

@RequestScoped
public class ActivityLogExporter implements Exporter
{
    private static final String DOWNLOAD_STATUS_COOKIE_NAME = "activityLogDownloadStatus";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT);

    @Inject
    private ActivityLogController mActivityLogController;

    @Inject
    private Validator mRequestValidator;

    private ActivityLogSearchRequestParams mParams;
    private Integer mOffset;
    private Integer mLimit;

    @Override
    public void processParameters(HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException
    {
        String dateFrom = ServletUtils.getDateFromParameter(request);
        String dateTo = ServletUtils.getDateToParameter(request);

        mOffset = ServletUtils.getOffsetParameter(request);
        mLimit = ServletUtils.getLimitParameter(request);

        mParams = new ActivityLogSearchRequestParams();
        mParams.dateFrom = dateFrom;
        mParams.dateTo = dateTo;

        validateRequest(mOffset, mLimit, mParams, responsePayload);
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.EXPORT_ACTIVITY_LOGS)
    public void export(HttpServletResponse response, ResponsePayload responsePayload, LoggedUser loggedUser)
        throws IOException
    {
        List<ActivityLog> logs = mActivityLogController.getActivityLogsBetween(mParams.dateFrom, mParams.dateTo,
            mOffset, mLimit);

        String fileName = getCsvFileName();

        ServletUtils.setContentTypeToCsv(response);
        ServletUtils.setContentDispositionHeader(response, fileName);

        writeCsv(logs, fileName, response.getOutputStream());
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

    private void validateRequest(Integer offset, Integer limit, ActivityLogSearchRequestParams params,
        ResponsePayload responsePayload)
    {
        ValidatorUtils.validateRequest(params, responsePayload, mRequestValidator);
        ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
            mRequestValidator);
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
