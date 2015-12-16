package uk.gov.phe.erdst.sc.awag.servlets.study;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "study", urlPatterns = {"/study/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ADMIN}))
public class StudyServlet extends HttpServlet
{
    @Inject
    private StudyController mStudyController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private RequestConverter mRequestConverter;

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
            ServletUtils.setResponseClientError(response);
        }
    }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWInvalidParameterException, AWInvalidResourceIdException, AWNoSuchEntityException
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
                    payload = mStudyController.getStudies(offset, limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID:
                payload = getStudyById(ServletUtils.getSelectEntityIdParameter(request));
                break;
            case ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE:
                ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                    mRequestValidator);

                if (!responsePayload.hasErrors())
                {
                    payload = mStudyController.getStudyLikeDtos(ServletUtils.getSelectLikeParameter(request), offset,
                        limit, responsePayload, includeMetadata);
                }
                break;
            case ServletConstants.REQ_PARAM_STUDY_WITH_ANIMAL:
                payload = getStudyWithAnimal(ServletUtils.getSelectEntityIdParameter(request));
                break;
            default:
                payload = ServletUtils.getNoResponsePayload();
                break;
        }

        responsePayload.setData(payload);
    }

    private Object getStudyById(String id) throws AWInvalidResourceIdException, AWNoSuchEntityException
    {
        Long studyId;
        studyId = ServletUtils.getParseResourceId(id);
        return mStudyController.getStudy(studyId);
    }

    private Object getStudyWithAnimal(String animalIdStr) throws AWInvalidResourceIdException, AWNoSuchEntityException
    {
        Long animalId = ServletUtils.getParseResourceId(animalIdStr);
        return mStudyController.getStudyWithAnimalDto(animalId);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String requestStudyJson = ServletUtils.getRequestBody(request);
        StudyClientData clientData = (StudyClientData) mRequestConverter.convert(requestStudyJson,
            StudyClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long studyId = ServletUtils.getResourceId(request);

        if (!ValidatorUtils.isResourceValid(studyId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mStudyController.updateStudy(studyId, clientData, responsePayload);

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String requestStudyJson = request.getParameter("study");
        StudyClientData clientData = (StudyClientData) mRequestConverter.convert(requestStudyJson,
            StudyClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mStudyController.storeStudy(clientData, responsePayload);

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
