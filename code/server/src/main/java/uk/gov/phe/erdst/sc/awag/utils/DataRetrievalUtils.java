package uk.gov.phe.erdst.sc.awag.utils;

import java.util.List;

public final class DataRetrievalUtils
{
    private DataRetrievalUtils()
    {
    }

    /**
     * Helper method which returns the first item from the result list, which comes from the usage
     * of getResultList() rather than getSingleResult() in the DAO layer.
     * @return the entity or null if the result List is empty
     */
    public static <T> T getEntityFromListResult(List<T> result)
    {
        if (!result.isEmpty())
        {
            return result.get(0);
        }

        return null;
    }
}
