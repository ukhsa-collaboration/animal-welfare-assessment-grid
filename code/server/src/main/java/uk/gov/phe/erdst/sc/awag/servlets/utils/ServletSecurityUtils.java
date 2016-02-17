package uk.gov.phe.erdst.sc.awag.servlets.utils;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public final class ServletSecurityUtils
{
    public static final String AW_ADMIN_USER = "admin";
    public static final String AW_AUTH_TYPE_PARAMETER = "authType";

    private ServletSecurityUtils()
    {
    }

    public static class RolesAllowed
    {
        public static final String AW_ADMIN = "admin";
        public static final String AW_ASSESSMENT_USER = "assessmentuser";
    }

    public static String getLoggedInUserName(HttpServletRequest request)
    {
        Principal principal = request.getUserPrincipal();
        return principal != null ? principal.getName() : null;
    }

    public static LoggedUser getLoggedUser(HttpServletRequest request)
    {
        return new LoggedUser(getLoggedInUserName(request));
    }

    public static String getAuthType(HttpServletRequest request)
    {
        return request.getServletContext().getInitParameter(ServletSecurityUtils.AW_AUTH_TYPE_PARAMETER);
    }

    public static String getGroupName(HttpServletRequest request)
    {
        if (request.isUserInRole(RolesAllowed.AW_ADMIN))
        {
            return RolesAllowed.AW_ADMIN;
        }
        else
        {
            return RolesAllowed.AW_ASSESSMENT_USER;
        }
    }
}
