package uk.gov.phe.erdst.sc.awag.service.validation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

public final class ValidatorUtils
{
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(Constants.DATE_FORMAT);

    private ValidatorUtils()
    {
    }

    public static void validateEntityId(Long id) throws AWInputValidationException
    {
        if (id == null || id < Constants.MIN_VALID_ID)
        {
            throw new AWInputValidationException(ValidationConstants.ERR_ID_PARAM);
        }
    }

    public static void validateOptionalEntityId(Long id) throws AWInputValidationException
    {
        if (id != null && id < Constants.MIN_VALID_ID)
        {
            throw new AWInputValidationException(ValidationConstants.ERR_OPTIONAL_ID_PARAM);
        }
    }

    public static void validateDateParameter(String dateParam) throws AWInputValidationException
    {
        if (!isDateValid(dateParam))
        {
            throw new AWInputValidationException(ValidationConstants.ERR_DATE_PARAM_FORMAT);
        }
    }

    public static void validateOptionalDateParameters(String dateFrom, String dateTo) throws AWInputValidationException
    {
        if (dateFrom != null && !isDateValid(dateFrom))
        {
            throw new AWInputValidationException(ValidationConstants.ERR_DATE_PARAM_FORMAT);
        }

        if (dateTo != null && !isDateValid(dateTo))
        {
            throw new AWInputValidationException(ValidationConstants.ERR_DATE_PARAM_FORMAT);
        }

        if (dateFrom != null && dateTo != null)
        {
            boolean isEqual = dateTo.equals(dateFrom);
            if (!isEqual && !ValidatorUtils.isFirstDateAfterSecondDate(dateTo, dateFrom))
            {
                throw new AWInputValidationException(ValidationConstants.ERR_FROM_TO_DATE_PARAMS);
            }
        }
    }

    public static void validateUpdateId(Long clientDataId, Long pathEntityId, String msgOnError)
        throws AWInputValidationException
    {
        validateEntityId(pathEntityId);
        if (pathEntityId.compareTo(clientDataId) != 0)
        {
            throw new AWInputValidationException(msgOnError);
        }
    }

    /**
     * This method is purely to hide the exception being created in controller classes from JAX-RS Analyzer tool.
     * When the exception was being created in a method of a controller, the tool would interpret it as the main return
     * type and specify the exception class instead of actual dto class in generated API document.
     * @param validationErrors
     * @throws AWInputValidationException
     */
    public static void throwInputValidationExceptionWith(
        List<Set<? extends ConstraintViolation<? extends Object>>> validationErrors) throws AWInputValidationException
    {
        AWInputValidationException e = new AWInputValidationException();
        for (Set<? extends ConstraintViolation<? extends Object>> errors : validationErrors)
        {
            e.addValidationErrors(errors);
        }
        throw e;
    }

    /**
     * @see throwInputValidationExceptionWith
     * @param message
     * @throws AWInputValidationException
     */
    public static void throwInputValidationExceptionWith(String message) throws AWInputValidationException
    {
        throw new AWInputValidationException(message);
    }

    /**
     * @see throwInputValidationExceptionWith
     * @param messages
     * @throws AWInputValidationException
     */
    public static void throwInputValidationExceptionWith(Collection<String> messages) throws AWInputValidationException
    {
        AWInputValidationException ex = new AWInputValidationException();
        ex.addValidationErrors(messages);
        throw ex;
    }

    public static boolean isFirstDateAfterSecondDate(String firstDate, String secondDate)
    {
        try
        {
            if (firstDate == null || secondDate == null)
            {
                return false;
            }

            return DATE_FORMATTER.parse(firstDate).after(DATE_FORMATTER.parse(secondDate));

        }
        catch (ParseException e)
        {
            return false;
        }
    }

    public static boolean isDateValid(String date)
    {
        try
        {
            if (date == null)
            {
                return false;
            }
            DATE_FORMATTER.parse(date);
        }
        catch (ParseException e)
        {
            return false;
        }
        return true;
    }

    public static boolean isKeysIdentical(Set<String> keySetA, Set<String> keySetB)
    {
        return keySetA.hashCode() == keySetB.hashCode();
    }
}
