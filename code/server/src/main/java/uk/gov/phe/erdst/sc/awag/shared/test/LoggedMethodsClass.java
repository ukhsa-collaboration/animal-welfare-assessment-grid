package uk.gov.phe.erdst.sc.awag.shared.test;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

@Stateless
public class LoggedMethodsClass
{
    public static final String LOGGED_METHOD_MISSING_LOGGED_USER = "loggedMethodMissingLoggedUser";
    public static final String LOGGED_METHOD_WITH_RESPONSE = "loggedMethodWithResponse";
    public static final String LOGGED_METHOD_MISSING_LOGGED_USER_OTHER_PARAM_PRESENT = "loggedMethodMissingLoggedUserOtherParamPresent";
    public static final String LOGGED_METHOD_THROWS_EXCEPTION = "loggedMethodThrowsException";
    public static final String THROWN_EXCEPTION_MSG = "Test exception message";

    public LoggedMethodsClass()
    {
    }

    @LoggedActivity(actionName = LOGGED_METHOD_WITH_RESPONSE)
    public ResponseDto loggedMethodWithResponse(LoggedUser loggedUser)
    {
        return DummyDto.create();
    }

    @LoggedActivity(actionName = LOGGED_METHOD_MISSING_LOGGED_USER)
    public ResponseDto loggedMethodMissingLoggedUser()
    {
        return DummyDto.create();
    }

    @LoggedActivity(actionName = LOGGED_METHOD_MISSING_LOGGED_USER_OTHER_PARAM_PRESENT)
    public ResponseDto loggedMethodMissingLoggedUserOtherParamPresent(String param)
    {
        return DummyDto.create();
    }

    @LoggedActivity(actionName = LOGGED_METHOD_THROWS_EXCEPTION)
    public ResponseDto loggedMethodThrowsException(LoggedUser loggedUser) throws AWNonUniqueException
    {
        throw new AWNonUniqueException(THROWN_EXCEPTION_MSG);
    }

    public void notLoggedMethod()
    {
    }

    public static class DummyDto implements ResponseDto
    {
        public static DummyDto create()
        {
            return new DummyDto();
        }
    }
}
