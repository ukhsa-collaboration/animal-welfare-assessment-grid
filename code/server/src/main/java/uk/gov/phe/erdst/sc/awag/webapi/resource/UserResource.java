package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import uk.gov.phe.erdst.sc.awag.businesslogic.UserController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserClientData;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;

@Path(WebApi.RESOURCE_USER_API)
public class UserResource extends GlobalResource
{
    @Inject
    private UserController mUserController;

    /**
     * Create user.
     * @param data
     * @return entity create dto
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(UserClientData data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        return onCreatedSuccess(mUserController.createUser(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Get user by id.
     * @param name
     * @return user dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) Long id)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mUserController.getUserById(id));
    }

    /**
     * Get users filtered by name. The likeFilter query parameter is used to
     * match user name property. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return users dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_LIKE_PATH)
    @GET
    public Response getLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mUserController.getUsersLike(WebApiUtils.getLikeFilterParam(likeFilter),
            WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get all users. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return users dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ALL_PATH)
    @GET
    public Response getAll(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mUserController.getAllUsers(WebApiUtils.getPagingParams(offset, limit)));
    }
}
