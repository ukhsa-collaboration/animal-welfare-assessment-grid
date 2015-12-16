package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWInvalidParameterException extends Exception
{
    private static final long serialVersionUID = -8142348654900689611L;

    public AWInvalidParameterException()
    {
        super();
    }

    public AWInvalidParameterException(String message)
    {
        super(message);
    }
}
