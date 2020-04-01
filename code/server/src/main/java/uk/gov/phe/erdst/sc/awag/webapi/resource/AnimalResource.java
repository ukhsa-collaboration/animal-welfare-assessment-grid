package uk.gov.phe.erdst.sc.awag.webapi.resource;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;

@Path(WebApi.RESOURCE_ANIMAL_API)
public class AnimalResource extends GlobalResource
{
    @Inject
    private AnimalController mAnimalController;

    /**
     * Create animal.
     * @param data
     *            <i>dob</i> mvnDateFormatJavaDoc
     * @return entity create dto
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(AnimalClientData data, @Context SecurityContext sc)
        throws AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        return onCreatedSuccess(mAnimalController.newApiCreateAnimal(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Get non deleted animal by id.
     * @param id
     *            has to be > 0
     * @return animal dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) Long id)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mAnimalController.newApiGetNonDeletedAnimalById(id));
    }

    /**
     * Get all non deleted animals. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return animals dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ALL_PATH)
    @GET
    public Response getAll(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(
            mAnimalController.newApiGetNonDeletedAnimals(WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get non deleted animals filtered by animal number. The likeFilter query parameter is used to
     * match animal number property. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return animals dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_LIKE_PATH)
    @GET
    public Response getLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mAnimalController.newApiGetNonDeletedAnimalsLike(
            WebApiUtils.getLikeFilterParam(likeFilter), WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get non deleted female animals filtered by animal number. The likeFilter query parameter is
     * used to match animal number property. Optionally response can be paged if <b>both</b> offset
     * and limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return animals dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ANIMAL_FEMALE_LIKE_PATH)
    @GET
    public Response getFemaleLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mAnimalController.newApiGetNonDeletedFemaleAnimalsLike(
            WebApiUtils.getLikeFilterParam(likeFilter), WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get non deleted male animals filtered by animal number. The likeFilter query parameter is
     * used to match animal number property. Optionally response can be paged if <b>both</b> offset
     * and limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return animals dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ANIMAL_MALE_LIKE_PATH)
    @GET
    public Response getMaleLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mAnimalController.newApiGetNonDeletedMaleAnimalsLike(
            WebApiUtils.getLikeFilterParam(likeFilter), WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get deleted animals. Optionally response can be paged if <b>both</b> offset and limit properties are
     * specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return animals dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ANIMAL_DELETED_PATH)
    @GET
    public Response getDeletedAnimals(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mAnimalController.newApiGetDeletedAnimals(WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Update details of a given animal.<br />
     * <i>dob</i> mvnDateFormatJavaDoc
     * @param id
     *            has to be > 0
     * @param data
     * @return animal dto with updated details
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @PUT
    public Response update(@PathParam(ID_PATH_PARAM) Long id, AnimalClientData data, @Context SecurityContext sc)
        throws AWNonUniqueException, AWNoSuchEntityException, AWInputValidationException, AWSeriousException
    {
        return onUpdateSuccess(
            mAnimalController.newApiUpdateAnimal(id, data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Marks animal with the given id as deleted but <b>does not</b> actually remove it from the system.
     * @param id
     *            has to be > 0
     * @return animal dto of animal that has been marked as deleted
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @DELETE
    public Response delete(@PathParam(ID_PATH_PARAM) Long id, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onDeleteSuccess(mAnimalController.newApiDeleteAnimal(id, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Upload a list of animals<br />
     * @param uploadFile
     * @return upload record list of new, duplicates and errors
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_UPLOAD_PATH)
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream uploadFile,
        @FormDataParam("file") FormDataContentDisposition fileDetails, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException
    {
        return onUploadSuccess(mAnimalController.uploadAnimal(uploadFile, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

}
