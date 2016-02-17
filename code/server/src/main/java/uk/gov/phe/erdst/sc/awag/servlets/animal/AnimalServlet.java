package uk.gov.phe.erdst.sc.awag.servlets.animal;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@SuppressWarnings("serial")
@WebServlet(name = "animal", urlPatterns = {"/animal/*"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class AnimalServlet extends HttpServlet
{
    @Inject
    private AnimalController mAnimalController;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private Validator mRequestValidator;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        ResponsePayload responsePayload = new ResponsePayload();
        ServletUtils.setJsonContentType(response);

        try
        {
            String callback = ServletUtils.getCallbackParameter(request);

            try
            {
                String action = ServletUtils.getSelectActionParameter(request);
                executeAction(action, request, responsePayload);
            }
            catch (AWNonUniqueException | AWInvalidResourceIdException | AWInvalidParameterException ex)
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String requestAnimalJson = ServletUtils.getRequestBody(request);
        AnimalClientData clientData = (AnimalClientData) mRequestConverter.convert(requestAnimalJson,
            AnimalClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();
        Long animalId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(animalId, HttpMethod.PUT))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        mAnimalController.updateAnimal(animalId, clientData, responsePayload,
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
        String requestAnimalJson = request.getParameter("animal");
        AnimalClientData clientData = (AnimalClientData) mRequestConverter.convert(requestAnimalJson,
            AnimalClientData.class);
        ResponsePayload responsePayload = new ResponsePayload();

        mAnimalController.storeAnimal(clientData, responsePayload, ServletSecurityUtils.getLoggedUser(request));

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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        Long animalId = ServletUtils.getNumberResourceId(request);

        if (!ValidatorUtils.isResourceValid(animalId, HttpMethod.DELETE))
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        try
        {
            mAnimalController.deleteAnimal(animalId, ServletSecurityUtils.getLoggedUser(request));
        }
        catch (AWNoSuchEntityException ex)
        {
            ServletUtils.setResponseResourceNotFound(response);
            return;
        }

        ServletUtils.setResponseDeleteOk(response);
    }

    private void executeAction(String action, HttpServletRequest request, ResponsePayload responsePayload)
        throws AWNonUniqueException, AWInvalidResourceIdException, AWInvalidParameterException
    {
        Object payload = ServletUtils.getNoResponsePayload();
        boolean includeMetadata = ServletUtils.getIncludeMetadataParameter(request);
        Integer offset = ServletUtils.getOffsetParameter(request);
        Integer limit = ServletUtils.getLimitParameter(request);

        if (!responsePayload.hasErrors())
        {
            switch (action)
            {
                case ServletConstants.REQ_PARAM_SELECT_ACTION_ALL:
                    ValidatorUtils.validateRequest(ServletUtils.getPagingRequestParams(offset, limit), responsePayload,
                        mRequestValidator);

                    if (!responsePayload.hasErrors())
                    {
                        payload = mAnimalController.getNonDeletedAnimals(offset, limit, responsePayload,
                            includeMetadata);
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
                    payload = getNonDeletedAnimalById(ServletUtils.getSelectEntityIdParameter(request));
                    break;
                default:
                    payload = ServletUtils.getNoResponsePayload();
                    break;
            }
        }

        responsePayload.setData(payload);
    }

    private Object handleSelectLikeRequest(HttpServletRequest request, ResponsePayload responsePayload, Integer offset,
        Integer limit, boolean includeMetadata) throws AWInvalidParameterException
    {
        Object payload;
        String like = ServletUtils.getSelectLikeParameter(request);
        String sex = ServletUtils.getSelectLikeSexParameter(request);
        String all = ServletUtils.getSelectLikeAllParameter(request);

        if (like != null)
        {
            if (all != null)
            {
                payload = getNonDeletedLikeFull(like, all, offset, limit, responsePayload, includeMetadata);
            }
            else
            {
                payload = getNonDeletedLikeSex(like, sex, offset, limit, responsePayload, includeMetadata);
            }
        }
        else
        {
            payload = ServletUtils.getNoResponsePayload();
        }
        return payload;
    }

    private List<AnimalDto> getNonDeletedLikeFull(String like, String all, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata) throws AWInvalidParameterException
    {
        if (!all.equals(ServletConstants.REQ_PARAM_ALL))
        {
            throw new AWInvalidParameterException(ValidationConstants.ERR_ALL_PARAM);
        }

        return mAnimalController.getNonDeletedAnimalLikeFullDtos(like, offset, limit, responsePayload, includeMetadata);
    }

    private AnimalDto getNonDeletedAnimalById(String id) throws AWInvalidResourceIdException
    {
        Long animalId;
        animalId = ServletUtils.getParseResourceId(id);
        return mAnimalController.getNonDeletedAnimalDtoById(animalId);
    }

    private Collection<EntitySelectDto> getNonDeletedLikeSex(String like, String sex, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata) throws AWInvalidParameterException
    {
        Collection<EntitySelectDto> animals = null;
        if (sex == null)
        {
            animals = mAnimalController.getNonDeletedAnimalsLikeDtos(like, offset, limit, responsePayload,
                includeMetadata);
        }
        else
        {
            if (!sex.equals(ServletConstants.REQ_PARAM_SEX_F) && !sex.equals(ServletConstants.REQ_PARAM_SEX_M))
            {
                throw new AWInvalidParameterException(ValidationConstants.ERR_SEX_PARAM);
            }
            animals = mAnimalController.getNonDeletedAnimalsLikeSexDtos(like, sex, offset, limit, responsePayload,
                includeMetadata);
        }

        return animals;
    }
}
