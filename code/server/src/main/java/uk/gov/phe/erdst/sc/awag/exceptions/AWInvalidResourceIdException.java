package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWInvalidResourceIdException extends Exception
{
    private static final long serialVersionUID = -2830836630596326663L;

    public AWInvalidResourceIdException()
    {
        super();
    }

    public AWInvalidResourceIdException(String message)
    {
        super(message);
    }
}
