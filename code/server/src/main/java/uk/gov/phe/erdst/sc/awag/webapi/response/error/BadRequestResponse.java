package uk.gov.phe.erdst.sc.awag.webapi.response.error;

import java.util.Collection;

public class BadRequestResponse extends ErrorResponseDto
{
    public static BadRequestResponse create()
    {
        return new BadRequestResponse();
    }

    public BadRequestResponse addError(String error)
    {
        errors.add(error);
        return this;
    }

    public BadRequestResponse addErrors(Collection<String> newErrors)
    {
        errors.addAll(newErrors);
        return this;
    }
}
