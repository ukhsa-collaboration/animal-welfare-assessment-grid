package uk.gov.phe.erdst.sc.awag.dao.impl.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

public final class DaoUtils
{
    private static final Pattern UNIQUE_PATTERN = Pattern.compile("^(.*?(\\bviolates unique constraint\\b)[^$]*)");

    private DaoUtils()
    {
    }

    public static void setOffset(Query query, Integer offset)
    {
        if (query != null)
        {
            if (offset != null)
            {
                query.setFirstResult(offset);
            }
            else
            {
                query.setFirstResult(0);
            }
        }
    }

    public static void setLimit(Query query, Integer limit)
    {
        if (query != null)
        {
            if (limit != null)
            {
                query.setMaxResults(limit);
            }
            else
            {
                query.setMaxResults(Integer.MAX_VALUE);
            }
        }
    }

    public static String getNoSuchEntityMessage(String entityName, Object violatingIdValue)
    {
        return String.format(ValidationConstants.ERR_NO_SUCH_ENTITY, entityName, violatingIdValue);
    }

    public static String getNoSuchEntityMessage(String entityName, String name)
    {
        return String.format(ValidationConstants.ERR_NO_SUCH_ENTITY_WITH_NAME, entityName, name);
    }

    public static String getUniqueConstraintViolationMessage(String uniquePropertyName, String violatingPropertyValue)
    {
        return String.format(DaoConstants.ERR_UNIQUE_CONSTRAINT_VIOLATION, uniquePropertyName, violatingPropertyValue);
    }

    public static boolean isUniqueConstraintViolation(PersistenceException ex)
    {
        if (ex == null)
        {
            throw new IllegalArgumentException();
        }
        return UNIQUE_PATTERN.matcher(ex.getCause().getCause().getMessage()).matches();
    }

    public static Collection<Long> formatIdsForInClause(Long... ids)
    {
        return Arrays.asList(ids);
    }

    public static String getLikeLowerCase(String like)
    {
        return getLowerCase(like).concat("%");
    }

    public static String getLowerCase(String string)
    {
        return string.toLowerCase(Constants.AW_LOCALE);
    }
}
