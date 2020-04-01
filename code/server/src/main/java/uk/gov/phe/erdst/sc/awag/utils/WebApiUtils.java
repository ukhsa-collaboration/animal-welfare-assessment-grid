package uk.gov.phe.erdst.sc.awag.utils;

import javax.servlet.http.HttpServletResponse;

import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;

public final class WebApiUtils
{
    private WebApiUtils()
    {
    }

    public static PagingQueryParams getPagingParams(Integer offset, Integer limit)
    {
        return new PagingQueryParams(offset, limit);
    }

    public static LikeFilterParam getLikeFilterParam(String likeParam)
    {
        return new LikeFilterParam(likeParam);
    }

    public static String changeDateParamToNullIfEmpty(String dateParam)
    {
        if (dateParam == null || !dateParam.isEmpty())
        {
            return dateParam;
        }

        return null;
    }

    public static Boolean getBooleanParameterFromString(String value)
    {
        if (value != null && !value.isEmpty())
        {
            return Boolean.parseBoolean(value);
        }

        return null;
    }

    public static void setContentTypeToCsv(HttpServletResponse response)
    {
        response.setHeader("Content-Type", "text/csv");
    }

    public static void setContentDispositionHeader(HttpServletResponse response, String fileName)
    {
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
    }
}
