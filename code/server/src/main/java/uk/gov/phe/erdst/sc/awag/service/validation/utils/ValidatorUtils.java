package uk.gov.phe.erdst.sc.awag.service.validation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.HttpMethod;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

public final class ValidatorUtils
{
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private ValidatorUtils()
    {
    }

    // CS:OFF: ReturnCount
    public static boolean isResourceValid(Long resourceId, String method)
    {
        if (resourceId == null)
        {
            return false;
        }

        switch (method)
        {
            case HttpMethod.GET:
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
                return resourceId >= Constants.MIN_VALID_ID;
            case HttpMethod.POST:
                return resourceId.equals(Constants.UNASSIGNED_ID);
            default:
                return false;
        }
    }

    // CS:ON

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

    public static <T> void validateRequest(T objectToValidate, ResponsePayload responsePayload,
        Validator requestValidator)
    {
        Set<ConstraintViolation<T>> violations = requestValidator.validate(objectToValidate);

        if (!violations.isEmpty())
        {
            responsePayload.addValidationErrors(violations);
        }
    }
}
