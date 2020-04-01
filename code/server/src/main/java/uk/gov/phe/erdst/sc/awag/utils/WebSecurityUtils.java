package uk.gov.phe.erdst.sc.awag.utils;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;

import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;

public final class WebSecurityUtils
{
    public static final String AW_ADMIN_USER = "admin";
    public static final String AW_AUTH_TYPE_PARAMETER = "authType";

    public static class RolesAllowed
    {
        public static final String AW_ADMIN = "admin";
        public static final String AW_ASSESSMENT_USER = "assessmentuser";
    }

    public static class ContextParameters
    {
        public static class AuthType
        {
            public static final String DATABASE = "database";
            public static final String LDAP = "ldap";
        }
    }

    private WebSecurityUtils()
    {
    }

    public static String getLoggedInUserName(SecurityContext sc)
    {
        Principal principal = sc.getUserPrincipal();
        return principal != null ? principal.getName() : Constants.Security.SECURITY_DISABLED_USER;
    }

    public static LoggedUser getNewApiLoggedUser(SecurityContext sc)
    {
        return new LoggedUser(getLoggedInUserName(sc));
    }

    public static String getAuthType(HttpServletRequest request)
    {
        return request.getServletContext().getInitParameter(AW_AUTH_TYPE_PARAMETER);
    }
}
