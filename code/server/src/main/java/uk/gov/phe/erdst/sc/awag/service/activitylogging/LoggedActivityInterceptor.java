package uk.gov.phe.erdst.sc.awag.service.activitylogging;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import uk.gov.phe.erdst.sc.awag.exceptions.AWRecoverableException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

@LoggedActivity(actionName = "")
@Interceptor
public class LoggedActivityInterceptor implements Serializable
{
    private static final Logger LOGGER = LogManager.getLogger(LoggedActivityInterceptor.class.getName());
    private static final String LOGGED_USER_PARAMETER_IS_MISSING_MSG = "loggedUser parameter is missing";
    private static final long serialVersionUID = 1L;
    private static Gson sGSON = new Gson();

    @Inject
    private ActivityLogger activityLogger;

    public LoggedActivityInterceptor()
    {
    }

    @AroundInvoke
    public Object track(InvocationContext context) throws Throwable
    {
        // Pre target class/interceptor chain
        String actionName = context.getMethod().getAnnotation(LoggedActivity.class).actionName();

        Object result = null;

        // CS:OFF: IllegalCatch
        try
        {
            result = context.proceed();
        }
        catch (Throwable throwable)
        {
            // Throwable handled to capture all issues
            handleInvocationWithException(context, actionName, throwable);
        }
        // CS:ON

        // Past target class/interceptor chain
        Object[] params = context.getParameters();

        InspectedParameters inspectedParameters = getInspectedParameters(params, result);

        InterceptedData interceptedData = new InterceptedData(actionName, inspectedParameters.loggedUser,
            inspectedParameters.responseDto);

        activityLogger.logActivity(interceptedData);

        return result;
    }

    private void handleInvocationWithException(InvocationContext context, String actionName, Throwable throwable)
        throws Throwable
    {

        Object[] params = context.getParameters();
        InspectedParameters inspectedParameters = getInspectedParameters(params);

        InterceptedData interceptedData = new InterceptedData(actionName, inspectedParameters.loggedUser,
            throwable.getMessage());

        String errorJson = sGSON.toJson(interceptedData);

        // Important note - it was not possible to write exception data into database as container
        // would throw EJBTransactionRolledBack exception as soon as the logger was called.
        // Logging only to log file.

        if (throwable instanceof AWRecoverableException)
        {
            // Recoverable exception not logged by default

            if (LOGGER.isDebugEnabled())
            {
                String message = "Invocation caused " + throwable.getClass().getSimpleName() + " with exception, data: "
                    + errorJson;
                LOGGER.debug(message, throwable);
            }

            throw throwable;
        }
        else
        {
            String message = "Invocation caused " + AWSeriousException.class.getSimpleName() + " with exception, data: "
                + errorJson;
            LOGGER.error(message, throwable);
            throw new AWSeriousException(throwable);
        }
    }

    private InspectedParameters getInspectedParameters(Object[] params)
    {
        Object result = null;
        return getInspectedParameters(params, result);
    }

    private InspectedParameters getInspectedParameters(Object[] params, Object result)
    {
        LoggedUser loggedUser = null;
        ResponseDto responseDto = result != null ? (ResponseDto) result : null;

        if (params == null)
        {
            throw new IllegalStateException(LOGGED_USER_PARAMETER_IS_MISSING_MSG);
        }

        for (Object param : params)
        {
            if (param instanceof LoggedUser)
            {
                loggedUser = (LoggedUser) param;
            }
        }

        if (loggedUser == null || loggedUser.username == null)
        {
            throw new IllegalStateException(LOGGED_USER_PARAMETER_IS_MISSING_MSG);
        }

        return new InspectedParameters(loggedUser, responseDto);
    }

    private static class InspectedParameters
    {
        private final LoggedUser loggedUser;
        private final ResponseDto responseDto;

        public InspectedParameters(LoggedUser loggedUser, ResponseDto responseDto)
        {
            this.loggedUser = loggedUser;
            this.responseDto = responseDto;
        }
    }
}
