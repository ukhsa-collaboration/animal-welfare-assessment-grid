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

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentTemplateClientData;

@Path(WebApi.RESOURCE_ASSESSMENT_TEMPLATE_API)
public class AssessmentTemplateResource extends GlobalResource
{
    @Inject
    private AssessmentTemplateController mAssessmentTemplateController;

    /**
     * Create assessment template.
     * @param data
     * @return assessment template dto
     * @response mvn404ResponseJson
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(AssessmentTemplateClientData data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException, AWNoSuchEntityException
    {
        return onCreatedSuccess(
            mAssessmentTemplateController.createAssessmentTemplate(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Update details of a given assessment template.<br />
     * @param id
     *            has to be > 0
     * @param data
     * @return assessment template dto with updated details
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @PUT
    public Response update(@PathParam(ID_PATH_PARAM) Long id, AssessmentTemplateClientData data,
        @Context SecurityContext sc)
        throws AWNonUniqueException, AWNoSuchEntityException, AWInputValidationException, AWSeriousException
    {
        return onUpdateSuccess(
            mAssessmentTemplateController.updateAssessmentTemplate(id, data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Deletes a particular parameter from a given template.
     * @param templateId
     *            has to be > 0
     * @param parameterId
     *            has to be > 0
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM + Constants.WebApi.RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_PATH + "/"
        + Constants.WebApi.RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_PATH_ID_PARAM)
    @DELETE
    public Response delete(@PathParam(ID_PATH_PARAM) Long templateId,
        @PathParam(RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_ID_PATH_PARAM) Long parameterId, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        mAssessmentTemplateController.deleteTemplateParameter(templateId, parameterId,
            WebSecurityUtils.getNewApiLoggedUser(sc));
        return onDeleteSuccessNoContent();
    }

    /**
     * Get assessment template by id.
     * @param id
     *            has to be > 0
     * @return assessment template dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) Long id)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mAssessmentTemplateController.getAssessmentTemplateById(id));
    }

    /**
     * Get assessment templates filtered by name. The likeFilter query parameter is used to
     * match assessment template ame property. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param likeFilter
     * @param offset
     * @param limit
     * @return assessment templates dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_LIKE_PATH)
    @GET
    public Response getLike(@QueryParam(QUERY_PARAM_LIKE_FILTER) String likeFilter,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentTemplateController.getAssessmentTemplatesLike(
            WebApiUtils.getLikeFilterParam(likeFilter), WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get assessment template used for a particular animal.
     * @param animalId
     *            has to be > 0
     * @return assessment template dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_TEMPLATE_FOR_ANIMAL + "/" + Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getAssessmentTemplateForAnimal(@PathParam(ID_PATH_PARAM) Long animalId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentTemplateController.getAssessmentTemplateByAnimalId(animalId));
    }

    /**
     * Get all assessment templates. Optionally response can be paged if <b>both</b> offset and
     * limit properties are specified.<br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @return assessment templates dto - includes paging information if offset and limit have been provided
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
            mAssessmentTemplateController.getAllAssessmentTemplates(WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get a count of all assessments.
     * @return count
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_TEMPLATE_COUNT_ALL)
    @GET
    public Response getCountAll()
    {
        return onRetrieveSuccess(mAssessmentTemplateController.getAssessmentTemplatesCount());
    }

    /**
     * Upload a list of templates. <br />
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
        return onUploadSuccess(mAssessmentTemplateController.uploadAssessmentTemplate(uploadFile,
            WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

}
