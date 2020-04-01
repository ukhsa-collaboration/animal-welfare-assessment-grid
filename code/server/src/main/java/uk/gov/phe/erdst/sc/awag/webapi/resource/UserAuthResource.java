package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import uk.gov.phe.erdst.sc.awag.businesslogic.UserAuthController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;

@Path(WebApi.RESOURCE_USER_ACCOUNT_API)
public class UserAuthResource extends GlobalResource
{
    @Inject
    private UserAuthController mUserAuthController;

    /**
     * Create user account.
     * @param data
     * @return user account dto
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(UserAuthClientData data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        return onCreatedSuccess(mUserAuthController.createUser(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Update details of a given user account.<br />
     * @param username
     * @param data
     * @return user account dto with updated details
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @PUT
    public Response update(@PathParam(ID_PATH_PARAM) String username, UserAuthClientData data,
        @Context SecurityContext sc)
        throws AWNonUniqueException, AWNoSuchEntityException, AWInputValidationException, AWSeriousException
    {
        return onUpdateSuccess(
            mUserAuthController.updateUser(username, data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Deletes a user account from the system.
     * @param username
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @DELETE
    public Response delete(@PathParam(ID_PATH_PARAM) String username, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException, AWSeriousException
    {
        mUserAuthController.deleteUser(username, WebSecurityUtils.getNewApiLoggedUser(sc));
        return onDeleteSuccessNoContent();
    }

    /**
     * Get user account by username.
     * @param username
     * @return user account dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) String username)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mUserAuthController.getUserAuthById(username));
    }

    /**
     * Get user accounts filtered by name. The likeFilter query parameter is used to
     * match user account name property. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return user accounts dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_LIKE_PATH)
    @GET
    public Response getLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mUserAuthController.getUserAuthLike(WebApiUtils.getLikeFilterParam(likeFilter),
            WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get all user accounts. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return user accounts dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ALL_PATH)
    @GET
    public Response getAll(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mUserAuthController.getAllUserAuths(WebApiUtils.getPagingParams(offset, limit)));
    }
}
