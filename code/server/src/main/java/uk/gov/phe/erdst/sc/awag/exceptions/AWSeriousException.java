package uk.gov.phe.erdst.sc.awag.exceptions;

/**
 * This exception should be thrown when an unexpected error has occurred and it could indicate that
 * the system is in an unstable state.
 */
public class AWSeriousException extends Exception
{
    private static final long serialVersionUID = 1L;

    public AWSeriousException(Throwable throwable)
    {
        super(throwable);
    }
}
