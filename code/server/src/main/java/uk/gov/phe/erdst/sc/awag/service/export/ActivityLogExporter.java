package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.opencsv.CSVWriter;

import uk.gov.phe.erdst.sc.awag.businesslogic.ActivityLogController;
import uk.gov.phe.erdst.sc.awag.datamodel.ActivityLog;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;

@RequestScoped
public class ActivityLogExporter
{
    private static final String DOWNLOAD_STATUS_COOKIE_NAME = "activityLogDownloadStatus";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT);

    @Inject
    private ActivityLogController mActivityLogController;

    @Inject
    private Validator mValidator;

    @LoggedActivity(actionName = LoggedActions.EXPORT_ACTIVITY_LOGS)
    public void exportData(String dateFrom, String dateTo, PagingQueryParams pagingParams,
        HttpServletResponse response, LoggedUser loggedUser) throws IOException, AWInputValidationException
    {
        ValidatorUtils.validateOptionalDateParameters(dateFrom, dateTo);

        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            List<ActivityLog> logs = mActivityLogController.getActivityLogsBetween(dateFrom, dateTo, offset, limit);

            String fileName = getCsvFileName();

            WebApiUtils.setContentTypeToCsv(response);
            WebApiUtils.setContentDispositionHeader(response, fileName);

            writeCsv(logs, fileName, response.getOutputStream());
        }

    }

    public String getDownloadStatusCookieName()
    {
        return DOWNLOAD_STATUS_COOKIE_NAME;
    }

    public String getDownloadStatusCookieValue()
    {
        return Constants.WebApi.DEFAULT_DOWNLOAD_STATUS_COOKIE_VALUE;
    }

    public int getDownloadStatusCookieExpirationTimeSeconds()
    {
        return Constants.WebApi.DEFAULT_DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC;
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
