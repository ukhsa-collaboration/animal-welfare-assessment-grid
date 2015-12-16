package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWNoSuchEntityException extends Exception
{
    private static final long serialVersionUID = 7557330197731344411L;

    public AWNoSuchEntityException(String message)
    {
        super(message);
    }

    public AWNoSuchEntityException()
    {
    }
}
