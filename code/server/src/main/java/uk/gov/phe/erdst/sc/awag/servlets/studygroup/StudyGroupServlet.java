package uk.gov.phe.erdst.sc.awag.servlets.studygroup;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.ws.rs.HttpMethod;

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyGroupClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "study-group", urlPatterns = {"/study-group/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class StudyGroupServlet extends HttpServlet
{
    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private StudyGroupController mStudyGroupController;

    @Inject
    private Validator mRequestValidator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String callback;
        ResponsePayload responsePayload = new ResponsePayload();
        ServletUtils.setJsonContentType(response);
        try
        {
            callback = ServletUtils.getCallbackParameter(request);
            try
            {
                String action = ServletUtils.getSelectActionParameter(request);
                executeAction(action, request, responsePayload);
            }
            catch (AWInvalidParameterException | AWInvalidResourceIdException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
            finally
            {
                ServletUtils.printResponse(response, callback, mResponseFormatter.toJson(responsePayload));
            }
        }
        catch (AWInvalidParameterException ex)
        {
            responsePayload.addError(ex.getMessage());
            ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
        }
    }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidResourceIdException, AWNoSuchEntityException, AWInvalidParameterException
    {
        Object payload = ServletUtils.getNoResponsePayload();
        boolean includeMetadata = ServletUtils.getIncludeMetadataParameter(request);
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        switch (action)
        {
            case ServletConstants.REQ_PARAM_SELECT_ACTION_ALL:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mStudyGroupController.getStudyGroups(offset, limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = handleSelectLikeRequest(request, responsePayload, offset, limit, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                payload = getStudyGroupById(ServletUtils.getSelectEntityIdParameter(request));
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }

        responsePayload.setData(payload);
    }

    private Object handleSelectLikeRequest(HttpServletRequest request, ResponsePayload responsePayload, Integer offset,
        Integer limit, boolean includeMetadata) throws AWInvalidParameterException
    {
        Object payload;
        String like = ServletUtils.getSelectLikeParameter(request);
        String all = ServletUtils.getSelectLikeAllParameter(request);
        if (all != null)
        {
            payload = mStudyGroupController.getStudyGroupsLikeFull(like, offset, limit, responsePayload,
                includeMetadata);
        }
        else
        {
            payload = mStudyGroupController.getStudyGroupsLikeDtos(like, offset, limit, responsePayload,
                includeMetadata);
        }

        return payload;
    }

    private Object getStudyGroupById(String id) throws AWInvalidResourceIdException, AWNoSuchEntityException
    {
        Long studyGroupId;
        studyGroupId = ServletUtils.getParseResourceId(id);
        return mStudyGroupController.getStudyGroup(studyGroupId);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String requestStudyGroupJson = ServletUtils.getRequestBody(request);
        StudyGroupClientData clientData = (StudyGroupClientData) mRequestConverter.convert(requestStudyGroupJson,
            StudyGroupClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long studyGroupId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(studyGroupId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mStudyGroupController.updateStudyGroup(studyGroupId, clientData, responsePayload,
            ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.getErrors().size() > 0)
        {
            ServletUtils.setResponseClientError(response);
        }
        else
        {
            ServletUtils.setResponseOk(response);
        }

        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String requestStudyGroupJson;
        ResponsePayload responsePayload = new ResponsePayload();
        try
        {
            requestStudyGroupJson = ServletUtils.getStudyGroupParameter(request);

        }
        catch (AWInvalidParameterException ex)
        {
            responsePayload.addError(ex.getMessage());
            ServletUtils.setResponseClientError(response);
            ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
            return;
        }

        StudyGroupClientData clientData = (StudyGroupClientData) mRequestConverter.convert(requestStudyGroupJson,
            StudyGroupClientData.class);

        mStudyGroupController.storeGroup(clientData, responsePayload, ServletSecurityUtils.getLoggedUser(request));

        if (responsePayload.getErrors().size() > 0)
        {
            ServletUtils.setResponseClientError(response);
        }
        else
        {
            ServletUtils.setResponseCreated(response);
        }

        ServletUtils.setResponseBody(response, mResponseFormatter.toJson(responsePayload));
    }
}
