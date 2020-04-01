package uk.gov.phe.erdst.sc.awag.webapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.webapi.response.error.BadRequestResponse;

@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<AWInputValidationException>
{

    @Override
    public Response toResponse(AWInputValidationException exception)
    {
        BadRequestResponse dto = BadRequestResponse.create().addErrors(exception.getErrors());
        return Response.status(Status.BAD_REQUEST).entity(dto).build();
    }

}
