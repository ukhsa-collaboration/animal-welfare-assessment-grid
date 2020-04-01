package uk.gov.phe.erdst.sc.awag.webapi.response.error;

import java.util.Collection;

public class NotFoundResponse extends ErrorResponseDto
{
    public static NotFoundResponse create()
    {
        return new NotFoundResponse();
    }

    public NotFoundResponse addError(String error)
    {
        errors.add(error);
        return this;
    }

    public NotFoundResponse addErrors(Collection<String> newErrors)
    {
        errors.addAll(newErrors);
        return this;
    }
}
