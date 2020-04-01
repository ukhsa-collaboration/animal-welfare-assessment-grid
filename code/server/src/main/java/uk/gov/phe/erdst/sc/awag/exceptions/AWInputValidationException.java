package uk.gov.phe.erdst.sc.awag.exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class AWInputValidationException extends AWRecoverableException implements AWWebApiException
{
    private static final long serialVersionUID = 1L;

    private List<String> errors = new ArrayList<String>();

    public AWInputValidationException()
    {
    }

    public AWInputValidationException(String message)
    {
        super(message);
        errors.add(message);
    }

    public void addValidationErrors(Set<? extends ConstraintViolation<? extends Object>> validationErrors)
    {
        Iterator<? extends ConstraintViolation<? extends Object>> it = validationErrors.iterator();
        while (it.hasNext())
        {
            errors.add(it.next().getMessage());
        }
    }

    public void addValidationErrors(Collection<String> validationErrors)
    {
        errors.addAll(validationErrors);
    }

    @Override
    public List<String> getErrors()
    {
        return errors;
    }

    /**
     * Use errors field getter instead.
     */
    @Deprecated
    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
