package uk.gov.phe.erdst.sc.awag.exceptions;

/**
 * This exception should be thrown when an error doesn't indicate any problems with the system.
 * Usually when user input is incorrect, etc.
 */
public class AWRecoverableException extends Exception
{
    private static final long serialVersionUID = 1L;

    public AWRecoverableException()
    {
    }

    public AWRecoverableException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}
