package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWNonUniqueException extends Exception
{
    private static final long serialVersionUID = 1433812709988636101L;

    public AWNonUniqueException()
    {
    }

    public AWNonUniqueException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}
