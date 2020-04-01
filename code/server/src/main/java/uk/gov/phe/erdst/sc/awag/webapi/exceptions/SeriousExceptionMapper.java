package uk.gov.phe.erdst.sc.awag.webapi.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;

@Provider
public class SeriousExceptionMapper implements ExceptionMapper<AWSeriousException>
{

    @Override
    public Response toResponse(AWSeriousException exception)
    {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

}
