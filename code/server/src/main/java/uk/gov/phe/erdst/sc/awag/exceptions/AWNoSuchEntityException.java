package uk.gov.phe.erdst.sc.awag.exceptions;

import java.util.Arrays;
import java.util.List;

public class AWNoSuchEntityException extends AWRecoverableException implements AWWebApiException
{
    private static final long serialVersionUID = 7557330197731344411L;

    public AWNoSuchEntityException(String message)
    {
        super(message);
    }

    public AWNoSuchEntityException()
    {
    }

    @Override
    public List<String> getErrors()
    {
        return Arrays.asList(getMessage());
    }
}
