package uk.gov.phe.erdst.sc.awag.servlets.utils;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

public final class ServletSecurityUtils
{

    private ServletSecurityUtils()
    {
    }

    public static class RolesAllowed
    {
        public static final String AW_ADMIN = "admin";
    }

    public static String getLoggedInUserName(HttpServletRequest request)
    {
        Principal principal = request.getUserPrincipal();
        return principal != null ? principal.getName() : null;
    }
}
