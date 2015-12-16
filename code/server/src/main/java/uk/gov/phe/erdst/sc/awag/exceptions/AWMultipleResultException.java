package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWMultipleResultException extends Exception
{
    private static final long serialVersionUID = 1433812709988636101L;

    public AWMultipleResultException()
    {
    }

    public AWMultipleResultException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}
