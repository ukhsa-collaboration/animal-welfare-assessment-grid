package uk.gov.phe.erdst.sc.awag.exceptions;

public class AWAssessmentCreationException extends Exception
{
    private static final long serialVersionUID = 1L;

    public AWAssessmentCreationException()
    {
    }

    public AWAssessmentCreationException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}
