package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWTemplateInUseException extends Exception
{
    private static final long serialVersionUID = 6010125721828367121L;

    public AWTemplateInUseException(String message)
    {
        super(message);
    }

    public AWTemplateInUseException()
    {
    }
}
