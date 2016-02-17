package uk.gov.phe.erdst.sc.awag.service.logging;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;

@LoggedActivity(actionName = "")
@Interceptor
public class LoggedActivityInterceptor implements Serializable
{
    private static final String LOGGED_USER_PARAMETER_IS_MISSING_MSG = "loggedUser parameter is missing";
    private static final long serialVersionUID = 1L;

    @Inject
    private ActivityLogger mActivityLogger;

    public LoggedActivityInterceptor()
    {
    }

    @AroundInvoke
    public Object track(InvocationContext context) throws Exception
    {
        // Pre target class/interceptor chain
        String actionName = context.getMethod().getAnnotation(LoggedActivity.class).actionName();

        Object result = null;

        // CS:OFF: IllegalCatch
        try
        {
            result = context.proceed();
        }
        catch (Exception e)
        {
            handleInvocationWithException(context, actionName, e);
            throw e;
        }
        // CS:ON

        // Past target class/interceptor chain
        Object[] params = context.getParameters();

        InspectedParameters inspectedParameters = getInspectedParameters(params);

        InterceptedData interceptedData = new InterceptedData(actionName, inspectedParameters.mLoggedUser,
            inspectedParameters.mResponsePayload);

        if (inspectedParameters.mResponsePayload != null)
        {
            handleInvocationWithResponsePayload(interceptedData);
        }
        else
        {
            handleInvocationNoResponsePayload(interceptedData);
        }

        return result;
    }

    private void handleInvocationNoResponsePayload(InterceptedData interceptedData)
    {
        mActivityLogger.logActivityNoResponsePayload(interceptedData);
    }

    private void handleInvocationWithResponsePayload(InterceptedData interceptedData)
    {
        mActivityLogger.logActivityWithResponsePayload(interceptedData);
    }

    private void handleInvocationWithException(InvocationContext context, String actionName, Exception e)
    {
        Object[] params = context.getParameters();
        InspectedParameters inspectedParameters = getInspectedParameters(params);

        InterceptedData interceptedData = new InterceptedData(actionName, inspectedParameters.mLoggedUser,
            e.getMessage());

        mActivityLogger.logActivityThrewException(interceptedData);
    }

    private InspectedParameters getInspectedParameters(Object[] params)
    {
        LoggedUser loggedUser = null;
        ResponsePayload responsePayload = null;

        if (params == null)
        {
            throw new IllegalStateException(LOGGED_USER_PARAMETER_IS_MISSING_MSG);
        }

        for (Object param : params)
        {
            if (param instanceof ResponsePayload)
            {
                responsePayload = (ResponsePayload) param;
            }
            else if (param instanceof LoggedUser)
            {
                loggedUser = (LoggedUser) param;
            }
        }

        if (loggedUser == null || loggedUser.username == null)
        {
            throw new IllegalStateException(LOGGED_USER_PARAMETER_IS_MISSING_MSG);
        }

        return new InspectedParameters(loggedUser, responsePayload);
    }

    private static class InspectedParameters
    {
        private final LoggedUser mLoggedUser;
        private final ResponsePayload mResponsePayload;

        public InspectedParameters(LoggedUser loggedUser, ResponsePayload responsePayload)
        {
            mLoggedUser = loggedUser;
            mResponsePayload = responsePayload;
        }
    }
}
