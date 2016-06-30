package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface Exporter
{

    void processParameters(HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException;

    void export(HttpServletResponse response, ResponsePayload responsePayload, LoggedUser loggedUser)
        throws IOException;

    String getDownloadStatusCookieName();

    String getDownloadStatusCookieValue();

    int getDownloadStatusCookieExpirationTimeSeconds();
}
