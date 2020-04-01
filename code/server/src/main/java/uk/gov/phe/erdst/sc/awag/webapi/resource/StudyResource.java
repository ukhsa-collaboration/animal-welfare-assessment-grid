package uk.gov.phe.erdst.sc.awag.webapi.resource;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;

@Path(WebApi.RESOURCE_STUDY_API)
public class StudyResource extends GlobalResource
{
    @Inject
    private StudyController mStudyController;

    /**
     * Create study.
     * @param data
     * @return entity create dto
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(StudyClientData data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        return onCreatedSuccess(mStudyController.createStudy(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Get study by id.
     * @param id
     *            has to be > 0
     * @return study dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) Long id)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mStudyController.getStudyById(id));
    }

    /**
     * Get study with a particular animal.
     * @param animalId
     *            has to be > 0
     * @return study dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_STUDY_WITH_ANIMAL + "/" + Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getStudyWithAnimal(@PathParam(ID_PATH_PARAM) Long animalId)
        throws AWNoSuchEntityException, AWSeriousException, AWInputValidationException
    {
        return onRetrieveSuccess(mStudyController.getStudyWithAnimal(animalId));
    }

    /**
     * Get studies filtered by name. The likeFilter query parameter is used to
     * match study name property. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return studies dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_LIKE_PATH)
    @GET
    public Response getLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mStudyController.getStudiesLike(WebApiUtils.getLikeFilterParam(likeFilter),
            WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get all studies. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return studies dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ALL_PATH)
    @GET
    public Response getAll(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mStudyController.getAllStudies(WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Update details of a given study.<br />
     * @param id
     *            has to be > 0
     * @param data
     * @return study group dto with updated details
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @PUT
    public Response update(@PathParam(ID_PATH_PARAM) Long id, StudyClientData data, @Context SecurityContext sc)
        throws AWNonUniqueException, AWNoSuchEntityException, AWInputValidationException, AWSeriousException
    {
        return onUpdateSuccess(mStudyController.updateStudy(id, data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Upload a list of studies<br />
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

        return onUploadSuccess(mStudyController.uploadStudy(uploadFile, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

}
