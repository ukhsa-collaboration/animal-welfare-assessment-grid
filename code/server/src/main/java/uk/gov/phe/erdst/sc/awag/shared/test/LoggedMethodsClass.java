package uk.gov.phe.erdst.sc.awag.shared.test;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

@Stateless
public class LoggedMethodsClass
{
    public static final String LOGGED_METHOD_MISSING_LOGGED_USER = "loggedMethodMissingLoggedUser";
    public static final String LOGGED_METHOD_WITH_RESPONSE_PAYLOAD = "loggedMethodWithResponsePayload";
    public static final String LOGGED_METHOD_NO_RESPONSE_PAYLOAD = "loggedMethodNoResponsePayload";
    public static final String LOGGED_METHOD_MISSING_LOGGED_USER_OTHER_PARAM_PRESENT = "loggedMethodMissingLoggedUserOtherParamPresent";
    public static final String LOGGED_METHOD_THROWS_EXCEPTION = "loggedMethodThrowsException";
    public static final String THROWN_EXCEPTION_MSG = "Test exception message";

    public LoggedMethodsClass()
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_NO_RESPONSE_PAYLOAD)
    public void loggedMethodNoResponsePayload(LoggedUser loggedUser)
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_WITH_RESPONSE_PAYLOAD)
    public void loggedMethodWithResponsePayload(ResponsePayload responsePayload, LoggedUser loggedUser)
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_MISSING_LOGGED_USER)
    public void loggedMethodMissingLoggedUser()
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_MISSING_LOGGED_USER_OTHER_PARAM_PRESENT)
    public void loggedMethodMissingLoggedUserOtherParamPresent(String param)
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_THROWS_EXCEPTION)
    public void loggedMethodThrowsException(LoggedUser loggedUser) throws AWNonUniqueException
    {
        throw new AWNonUniqueException(THROWN_EXCEPTION_MSG);
    }

    public void notLoggedMethod()
    {
    }
}
