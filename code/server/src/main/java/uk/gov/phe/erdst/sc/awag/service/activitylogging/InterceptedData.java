package uk.gov.phe.erdst.sc.awag.service.activitylogging;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class InterceptedData
{
    final String actionName;
    final LoggedUser loggedUser;
    final ResponseDto responseDto;
    final String exceptionMsg;

    private InterceptedData(String actionName, LoggedUser loggedUser, ResponseDto responseDto,
        String exceptionMsg)
    {
        this.actionName = actionName;
        this.loggedUser = loggedUser;
        this.responseDto = responseDto;
        this.exceptionMsg = exceptionMsg;
    }

    public InterceptedData(String actionName, LoggedUser loggedUser, ResponseDto responseDto)
    {
        this(actionName, loggedUser, responseDto, null);
    }

    public InterceptedData(String actionName, LoggedUser loggedUser, String exceptionMsg)
    {
        this(actionName, loggedUser, null, exceptionMsg);
    }
}
