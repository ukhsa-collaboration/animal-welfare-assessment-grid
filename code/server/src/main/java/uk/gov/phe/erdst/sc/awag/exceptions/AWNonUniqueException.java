package uk.gov.phe.erdst.sc.awag.exceptions;

import java.util.Arrays;
import java.util.List;

public class AWNonUniqueException extends AWRecoverableException implements AWWebApiException
{
    private static final long serialVersionUID = 1433812709988636101L;

    public AWNonUniqueException()
    {
    }

    public AWNonUniqueException(String exceptionMessage)
    {
        super(exceptionMessage);
    }

    @Override
    public List<String> getErrors()
    {
        return Arrays.asList(getMessage());
    }
}
