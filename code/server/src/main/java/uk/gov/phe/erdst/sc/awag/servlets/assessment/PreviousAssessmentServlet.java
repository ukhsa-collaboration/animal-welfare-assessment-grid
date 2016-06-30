package uk.gov.phe.erdst.sc.awag.servlets.assessment;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.assessment.PreviousAssessmentDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidResourceIdException;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;

@WebServlet({"/prev-assessment"})
@ServletSecurity(@HttpConstraint(rolesAllowed = {ServletSecurityUtils.RolesAllowed.AW_ASSESSMENT_USER}))
public class PreviousAssessmentServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Inject
    private ResponseFormatter mResponseFormatter;

    @Inject
    private AssessmentController mAssessmentController;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String callback;
        ResponsePayload responsePayload = new ResponsePayload();
        ServletUtils.setJsonContentType(response);
        try
        {
            callback = ServletUtils.getCallbackParameter(request);
            try
            {
                String sId = ServletUtils.getAnimalIdParameter(request);
                Long animalId = ServletUtils.getParseResourceId(sId);

                String date = ServletUtils.getDateParameter(request);

                PreviousAssessmentDto dto;
                if (date == null)
                {
                    dto = mAssessmentController.getPreviousAssessment(animalId);
                }
                else
                {
                    Long currentAssessmentId = ServletUtils
                        .getParseResourceId((ServletUtils.getNonNullParameter(request, ServletConstants.REQ_PARAM_ID,
                            ValidationConstants.VALID_ID_ANNOT_DEFAULT_MESSAGE)));

                    if (ValidatorUtils.isDateValid(date))
                    {
                        dto = mAssessmentController.getPreviousAssessmentByDate(animalId, date, currentAssessmentId);
                    }
                    else
                    {
                        throw new AWInvalidParameterException(
                            String.format(ValidationConstants.ERR_DATE_FORMAT, "Date"));
                    }
                }

                responsePayload.setData(dto);
            }
            catch (AWInvalidParameterException | AWInvalidResourceIdException ex)
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
}
