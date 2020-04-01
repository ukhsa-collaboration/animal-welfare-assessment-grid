package uk.gov.phe.erdst.sc.awag.webapi.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.export.AssessmentExporter;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentCreateRequest;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentScoreComparisonRequest;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentUpdateRequest;

@Path(WebApi.RESOURCE_ASSESSMENT_API)
public class AssessmentResource extends GlobalResource
{
    @Inject
    private AssessmentController mAssessmentController;

    @Inject
    private AssessmentExporter mAssessmentExporter;

    /**
     * Create an assessment.
     * @param data
     *            <i>id</i> parameter has to be -1.
     *            <i>isScoresVerified</i> parameter must be set to true to indicate scores have been verified.<br />
     *            If <i>isSubmit</i> is true, assessment has to be complete and all fields will be validated.<br />
     *            <i>averageScores</i> field structure: {parameterId: 0.0, ...} .<br />
     *            <i>parameterComments</i> field structure: {parameterId: "string", ...} .<br />
     *            <i>score</i> field structure: {parameterId: {factorId: {score: 0, isIgnored: false}}}<br />
     *            <i>date</i> mvnDateFormatJavaDoc
     * @return entity create dto
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @POST
    public Response create(AssessmentCreateRequest data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException, AWSeriousException
    {
        return onCreatedSuccess(mAssessmentController.createAssessment(data, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    /**
     * Get all, fully populated assessments. Not allowed - use search or export API endpoints instead.
     * Getting all, fully populated assessments is not allowed as it would essentially trigger a download of the entire
     * database.
     */
    @Path(Constants.WebApi.RESOURCE_ALL_PATH)
    @GET
    public Response getAll()
    {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    /**
     * Search assessments. Response will contain unique values for different components of returned assessments.
     * For example, specifying a study id will result in returning assessments, study groups, animals, etc. that are
     * only linked to the specified study.<br />
     * <i>dateFrom</i> and <i>dateTo</i> mvnDateFormatJavaDoc
     * @param studyId
     * @param studyGroupId
     * @param animalId
     * @param userId
     * @param reasonId
     * @param dateFrom
     * @param dateTo
     * @return assessments dto
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_SEARCH_GET_UNIQUE_FILTERS_PATH)
    @GET
    public Response searchAssessmentsAndGetUniqueSearchFilters(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_ID) Long studyId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_GROUP_ID) Long studyGroupId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID) Long animalId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_USER_ID) Long userId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_REASON_ID) Long reasonId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PARAM_DATE_FROM) String dateFrom,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PARAM_DATE_TO) String dateTo) throws AWInputValidationException
    {
        String dateFromChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateFrom);
        String dateToChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateTo);

        return onRetrieveSuccess(mAssessmentController.searchAssessmentsAndGetUniqueSearchFilters(studyId, studyGroupId,
            animalId, dateFromChecked, dateToChecked, userId, reasonId));
    }

    /**
     * Search complete assessments for an animal between dates. Optionally response can be paged if <b>both</b> offset
     * and limit properties are specified.<br />
     * <i>dateFrom</i> and <i>dateTo</i> mvnDateFormatJavaDoc <br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @param animalId
     * @param dateFrom
     * @param dateTo
     * @return assessments dto - includes paging information if offset and limit have been provided
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_SEARCH_COMPLETE_BETWEEN_DATES_PATH)
    @GET
    public Response searchCompleteAssessmentsForAnimalBetweenDates(
        @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID) Long animalId,
        @QueryParam(PARAM_DATE_FROM) String dateFrom, @QueryParam(PARAM_DATE_TO) String dateTo,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mAssessmentController.searchCompleteAssessmentsForAnimalBetweenDates(dateFrom, dateTo,
            animalId, WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Search assessments. Optionally response can be paged if <b>both</b> offset and limit properties are
     * specified.<br />
     * <i>dateFrom</i> and <i>dateTo</i> mvnDateFormatJavaDoc <br />
     * <i>offset</i> mvnPagingOffsetValidValueJavaDoc <br/>
     * <i>limit</i> mvnPagingLimitValidValueJavaDoc <br/>
     * @param offset
     * @param limit
     * @param studyId
     * @param animalId
     * @param userId
     * @param reasonId
     * @param dateFrom
     * @param dateTo
     * @param isComplete
     * @param offset
     * @param limit
     * @return assessments dto
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_SEARCH_PATH)
    @GET
    public Response searchAssessments(
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_ID) Long studyId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID) Long animalId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_USER_ID) Long userId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_REASON_ID) Long reasonId,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PARAM_DATE_FROM) String dateFrom,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PARAM_DATE_TO) String dateTo,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_IS_COMPLETE) String isComplete,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_OFFSET_QUERY_PARAM) Integer offset,
        @DefaultValue(OPTIONAL_PARAM_VALUE) @QueryParam(PAGING_REQUEST_LIMIT_QUERY_PARAM) Integer limit)
        throws AWInputValidationException
    {
        String dateFromChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateFrom);
        String dateToChecked = WebApiUtils.changeDateParamToNullIfEmpty(dateTo);
        Boolean isCompleteChecked = WebApiUtils.getBooleanParameterFromString(isComplete);

        return onRetrieveSuccess(mAssessmentController.searchAssessments(animalId, dateFromChecked, dateToChecked,
            userId, reasonId, studyId, isCompleteChecked, WebApiUtils.getPagingParams(offset, limit)));
    }

    /**
     * Get assessment by id.
     * @param id
     *            has to be > 0
     * @return assessment dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getById(@PathParam(ID_PATH_PARAM) Long id)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentController.getAssessmentById(id));
    }

    /**
     * Get a count of all assessments.
     * @return count
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COUNT_ALL)
    @GET
    public Response getCountAll()
    {
        return onRetrieveSuccess(mAssessmentController.getAssessmentsCount());
    }

    /**
     * Get a count of completed assessments.
     * @return count
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COUNT_BY_COMPLETED_PATH)
    @GET
    public Response getCountOfCompleted()
    {
        final boolean isCompleted = true;
        return onRetrieveSuccess(mAssessmentController.getAssessmentsCountByCompleteness(isCompleted));
    }

    /**
     * Get a count of incomplete assessments.
     * @return count
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COUNT_BY_INCOMPLETE_PATH)
    @GET
    public Response getCountOfIncomplete()
    {
        final boolean isCompleted = false;
        return onRetrieveSuccess(mAssessmentController.getAssessmentsCountByCompleteness(isCompleted));
    }

    /**
     * Get a count of assessments for a given animal.
     * @param id
     *            has to be > 0
     * @return count
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COUNT_BY_ANIMAL_PATH + "/" + Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getCountByAnimal(@PathParam(ID_PATH_PARAM) Long id) throws AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentController.getAssessmentsCountByAnimalId(id));
    }

    /**
     * Get a count of assessments with a given template.
     * @param id
     *            has to be > 0
     * @return count
     * @response mvn400ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COUNT_BY_TEMPLATE_PATH + "/" + Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getCountByTemplate(@PathParam(ID_PATH_PARAM) Long id) throws AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentController.getAssessmentsCountByTemplateId(id));
    }

    /**
     * Get previous assessment date-wise for an animal.
     * @param id
     *            has to be > 0
     * @return assessment dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_PREVIOUS_FOR_ANIMAL_PATH + "/" + Constants.WebApi.PATH_ID_PARAM)
    @GET
    public Response getPreviousAssessmentForAnimal(@PathParam(ID_PATH_PARAM) Long animalId)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        return onRetrieveSuccess(mAssessmentController.getPreviousAssessmentForAnimal(animalId));
    }

    /**
     * Get previous assessment for an animal date-wise in relation to the specified assessment. <br />
     * <i>assessmentDate</i> mvnDateFormatJavaDoc
     * @param animalId
     * @param assessmentId
     * @param assessmentDate
     * @return assessment dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_PREVIOUS_PREVIEW_BY_DATE)
    @GET
    public Response getPreviousAssessmentPreviewByDate(
        @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID) Long animalId,
        @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_ID) Long assessmentId,
        @QueryParam(RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_DATE) String assessmentDate)
        throws AWNoSuchEntityException, AWInputValidationException
    {
        return onRetrieveSuccess(
            mAssessmentController.getPreviousAssessmentPreviewByDate(animalId, assessmentDate, assessmentId));
    }

    /**
     * Compare assessment score against previous assessment.
     * @param data
     *            <b>Required fields:</b>id, date, animalId, score and previousAssessmentId.<br />
     *            <i>id</i> parameter of new assessment has to be -1.<br />
     *            <i>parameterComments & averageScores</i> can be empty.<br />
     *            <i>score</i> field structure: {parameterId: {factorId: {score: 0, isIgnored: false}}}<br />
     *            <i>date</i> mvnDateFormatJavaDoc
     * @return scores comparison result dto
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_ASSESSMENT_COMPARE_SCORES_WITH_PREVIOUS)
    @POST
    public Response compareAssessmentScores(AssessmentScoreComparisonRequest data)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        return onRetrieveSuccess(mAssessmentController.compareAssessmentScores(data));
    }

    /**
     * Export assessments. Call this endpoint with an HTML form.
     * <i>assessmentIds</i> parameter structure: assessmentIds='["513","541","569"]'<br />
     * The parameter gets interpreted as JSON and parsed into Long[].<br />
     * When the request is served, a file download disposition will be set.
     * Also, a cookie with name: <i>assessmentsExportDownloadStatus</i> will be returned by the server.
     * @param assessmentIds
     * @response mvn400ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.RESOURCE_EXPORT_PATH)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void export(@FormParam(RESOURCE_ASSESSMENT_EXPORT_FORM_PARAM_ASSESSMENT_IDS) String assessmentIds,
        @Context HttpHeaders headers, @Context HttpServletResponse response, @Context SecurityContext sc)
        throws AWInputValidationException, IOException
    {
        setDownloadStatusCookie(response, mAssessmentExporter);
        mAssessmentExporter.exportData(assessmentIds, response, WebSecurityUtils.getNewApiLoggedUser(sc));
    }

    /**
     * Update a given assessment.
     * @param id
     *            has to be > 0
     * @param data
     *            If <i>isSubmit</i> is true, assessment has to be complete and all fields will be validated.<br />
     *            <i>averageScores</i> field structure: {parameterId: 0.0, ...} .<br />
     *            <i>parameterComments</i> field structure: {parameterId: "string", ...} .<br />
     *            <i>score</i> field structure: {parameterId: {factorId: {score: 0, isIgnored: false}}}<br />
     *            <i>date</i> mvnDateFormatJavaDoc
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @PUT
    public Response update(@PathParam(ID_PATH_PARAM) Long id, AssessmentUpdateRequest data, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException, AWSeriousException
    {
        mAssessmentController.updateAssessment(id, data, WebSecurityUtils.getNewApiLoggedUser(sc));
        return onUpdateSuccessNoContent();
    }

    /**
     * Permanently deletes assessment with the given id from the system.
     * @param id
     *            has to be > 0
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     */
    @Path(Constants.WebApi.PATH_ID_PARAM)
    @DELETE
    public Response delete(@PathParam(ID_PATH_PARAM) Long id, @Context SecurityContext sc)
        throws AWInputValidationException, AWNoSuchEntityException
    {
        mAssessmentController.deleteAssessment(id, WebSecurityUtils.getNewApiLoggedUser(sc));
        return onDeleteSuccessNoContent();
    }

    /**
     * Upload a list of assessments<br />
     * @param uploadFile
     * @return upload record list of new, duplicates and errors
     * @response mvn400ResponseJson
     * @response mvn404ResponseJson
     * @response mvn500ResponseJson
     *           TODO need assessment id!
     */
    @Path(Constants.WebApi.RESOURCE_UPLOAD_PATH)
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("id") Long id, @FormDataParam("file") InputStream uploadFile,
        @FormDataParam("file") FormDataContentDisposition fileDetails, @Context SecurityContext sc)
        throws AWInputValidationException, AWNonUniqueException
    {
        return onUploadSuccess(
            mAssessmentController.uploadAssessment(id, uploadFile, WebSecurityUtils.getNewApiLoggedUser(sc)));
    }

    private void setDownloadStatusCookie(HttpServletResponse response, AssessmentExporter exporter)
    {
        Cookie cookie = new Cookie(exporter.getDownloadStatusCookieName(), exporter.getDownloadStatusCookieValue());
        cookie.setMaxAge(exporter.getDownloadStatusCookieExpirationTimeSeconds());
        cookie.setPath(Constants.WebApi.DOWNLOAD_STATUS_COOKIE_PATH);
        cookie.setHttpOnly(false);

        response.addCookie(cookie);
    }

}
