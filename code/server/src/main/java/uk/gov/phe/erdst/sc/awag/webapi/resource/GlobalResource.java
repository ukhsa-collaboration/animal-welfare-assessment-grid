package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class GlobalResource
{
    // Jax-rs-analyzer tool fails when params are not fields of declaring class or superclass.
    // Hence a copy here.
    protected static final String ID_PATH_PARAM = Constants.WebApi.ID_PATH_PARAM;
    protected static final String PAGING_REQUEST_OFFSET_QUERY_PARAM = Constants.WebApi.PAGING_REQUEST_OFFSET_QUERY_PARAM;
    protected static final String PAGING_REQUEST_LIMIT_QUERY_PARAM = Constants.WebApi.PAGING_REQUEST_LIMIT_QUERY_PARAM;
    protected static final String QUERY_PARAM_LIKE_FILTER = Constants.WebApi.QUERY_PARAM_LIKE_FILTER;
    protected static final String OPTIONAL_PARAM_VALUE = Constants.WebApi.OPTIONAL_PARAM_VALUE;

    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_ID;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_GROUP_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_GROUP_ID;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_USER_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_USER_ID;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_REASON_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_REASON_ID;
    protected static final String PARAM_DATE_FROM = Constants.WebApi.PARAM_DATE_FROM;
    protected static final String PARAM_DATE_TO = Constants.WebApi.PARAM_DATE_TO;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_IS_COMPLETE = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_IS_COMPLETE;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_DATE = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_DATE;
    protected static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_ID = Constants.WebApi.RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_ID;
    protected static final String RESOURCE_ASSESSMENT_EXPORT_FORM_PARAM_ASSESSMENT_IDS = Constants.WebApi.RESOURCE_ASSESSMENT_EXPORT_FORM_PARAM_ASSESSMENT_IDS;
    protected static final String RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_ID_PATH_PARAM = Constants.WebApi.RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_ID_PATH_PARAM;

    @Context
    protected HttpServletRequest mServletRequest;

    protected Response onCreatedSuccess(ResponseDto responseDto)
    {
        return Response.status(Status.CREATED).entity(responseDto).build();
    }

    protected Response onRetrieveSuccess(ResponseDto responseDto)
    {
        return Response.status(Status.OK).entity(responseDto).build();
    }

    protected Response onUpdateSuccess(ResponseDto responseDto)
    {
        return Response.status(Status.OK).entity(responseDto).build();
    }

    protected Response onUploadSuccess(ResponseDto responseDto)
    {
        return Response.status(Status.CREATED).entity(responseDto).build();
    }

    protected Response onUpdateSuccessNoContent()
    {
        return Response.status(Status.OK).build();
    }

    protected Response onDeleteSuccess(ResponseDto responseDto)
    {
        return Response.status(Status.OK).entity(responseDto).build();
    }

    protected Response onDeleteSuccessNoContent()
    {
        return Response.status(Status.OK).build();
    }

    protected Response onUploadSuccessNoContent()
    {
        return Response.status(Status.OK).entity(new UploadResponseDto()).build();
    }

}
