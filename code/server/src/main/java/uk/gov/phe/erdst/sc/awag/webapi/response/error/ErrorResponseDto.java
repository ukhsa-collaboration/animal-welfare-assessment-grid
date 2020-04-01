package uk.gov.phe.erdst.sc.awag.webapi.response.error;

import java.util.ArrayList;
import java.util.List;

public abstract class ErrorResponseDto
{
    public List<String> errors = new ArrayList<String>();

    public boolean hasErrors()
    {
        return (!errors.isEmpty());
    }

    public boolean hasNoErrorsPresent()
    {
        return errors.isEmpty();
    }
}
