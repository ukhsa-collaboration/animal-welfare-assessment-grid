package uk.gov.phe.erdst.sc.awag.webapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.webapi.response.error.NotFoundResponse;

@Provider
public class NoSuchEntityExceptionMapper implements ExceptionMapper<AWNoSuchEntityException>
{

    @Override
    public Response toResponse(AWNoSuchEntityException exception)
    {
        NotFoundResponse dto = NotFoundResponse.create().addErrors(exception.getErrors());
        return Response.status(Status.NOT_FOUND).entity(dto).build();
    }

}
