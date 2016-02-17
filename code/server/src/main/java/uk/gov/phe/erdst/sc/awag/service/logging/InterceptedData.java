package uk.gov.phe.erdst.sc.awag.service.logging;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;

public class InterceptedData
{
    final String actionName;
    final LoggedUser loggedUser;
    final ResponsePayload responsePayload;
    final String exceptionMsg;

    private InterceptedData(String actionName, LoggedUser loggedUser, ResponsePayload responsePayload,
        String exceptionMsg)
    {
        this.actionName = actionName;
        this.loggedUser = loggedUser;
        this.responsePayload = responsePayload;
        this.exceptionMsg = exceptionMsg;
    }

    public InterceptedData(String actionName, LoggedUser loggedUser, ResponsePayload responsePayload)
    {
        this(actionName, loggedUser, responsePayload, null);
    }

    public InterceptedData(String actionName, LoggedUser loggedUser, String exceptionMsg)
    {
        this(actionName, loggedUser, null, exceptionMsg);
    }
}
