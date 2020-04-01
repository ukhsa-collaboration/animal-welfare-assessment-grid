package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import uk.gov.phe.erdst.sc.awag.businesslogic.UserAuthController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils.ContextParameters;
import uk.gov.phe.erdst.sc.awag.webapi.response.EmptyDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.LogonDetailsDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.UserAuthDto;

@Path(WebApi.RESOURCE_AUTHENTICATION_API)
public class AuthenticationResource extends GlobalResource
{
    @Inject
    private UserAuthController mUserAuthController;

    /**
     * Empty response API entry to check authentication status.<br />
     * If non-200 HTTP code returned, user session has expired.
     * @return
     */
    @Path(Constants.WebApi.RESOURCE_AUTHENTICATION_CHECK_PATH)
    @GET
    public Response authenticationCheck()
    {
        return onRetrieveSuccess(new EmptyDto());
    }

    /**
     * Get logon details for the currently logged in user.
     * @return logon details dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_AUTHENTICATION_LOGON_DETAILS_PATH)
    @GET
    public Response getLogonDetails(@Context HttpServletRequest request, @Context SecurityContext sc)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        String username = WebSecurityUtils.getLoggedInUserName(sc);

        LogonDetailsDto logonDetails = new LogonDetailsDto();
        logonDetails.username = username;
        logonDetails.authType = WebSecurityUtils.getAuthType(request);
        if (logonDetails.authType.equals(ContextParameters.AuthType.LDAP))
        {
            logonDetails.groupName = WebSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER;
        }
        else
        {
            UserAuthDto userAuthDto = mUserAuthController.getUserAuthById(username);
            logonDetails.groupName = userAuthDto.userGroup;
        }

        return onRetrieveSuccess(logonDetails);
    }

    /**
     * Empty response API entry to log out a user (i.e. invalidate user session).<br />
     * If non-200 HTTP code returned, user session has expired.
     * @return
     */
    @Path(Constants.WebApi.RESOURCE_AUTHENTICATION_LOGOUT_PATH)
    @GET
    public Response logout(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate();
        }

        return onRetrieveSuccess(new EmptyDto());
    }
}
