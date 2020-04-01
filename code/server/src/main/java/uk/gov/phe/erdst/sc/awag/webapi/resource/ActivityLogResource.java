package uk.gov.phe.erdst.sc.awag.webapi.resource;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.service.export.ActivityLogExporter;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;

@Path(WebApi.RESOURCE_ACTIVITY_LOG_API)
public class ActivityLogResource extends GlobalResource
{
    @Inject
    private ActivityLogExporter mActivityLogExporter;

    /**
     * Export activity logs. Call this endpoint with an HTML form.<br />
     * <i>dateFrom</i> and <i>dateTo</i> mvnDateFormatJavaDoc <br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * When the request is served, a file download disposition will be set.
     * Also, a cookie with name: <i>activityLogDownloadStatus</i> will be returned by the server.
     * @param dateFrom
     * @param dateTo
     * @param offset
     * @param limit
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_EXPORT_PATH)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void export(@DefaultValue(OPTIONAL_PARAM_VALUE) @FormParam(PARAM_DATE_FROM) String dateFrom,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @FormParam(PARAM_DATE_TO) String dateTo,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @FormParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @FormParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit,
        @Context HttpHeaders headers, @Context HttpServletResponse response, @Context SecurityContext sc)
        throws AWInputValidationException, IOException
    {
        String dateFromChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateFrom);
        String dateToChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateTo);

        setDownloadStatusCookie(response, mActivityLogExporter);

        mActivityLogExporter.exportData(dateFromChecked, dateToChecked, WebApiUtils.getPagingParams(offset, limit),
            response, WebSecurityUtils.getNewApiLoggedUser(sc));
    }

    private void setDownloadStatusCookie(HttpServletResponse response, ActivityLogExporter exporter)
    {
        Cookie cookie = new Cookie(exporter.getDownloadStatusCookieName(), exporter.getDownloadStatusCookieValue());
        cookie.setMaxAge(exporter.getDownloadStatusCookieExpirationTimeSeconds());
        cookie.setPath(Constants.WebApi.DOWNLOAD_STATUS_COOKIE_PATH);
        cookie.setHttpOnly(false);

        response.addCookie(cookie);
    }

}
