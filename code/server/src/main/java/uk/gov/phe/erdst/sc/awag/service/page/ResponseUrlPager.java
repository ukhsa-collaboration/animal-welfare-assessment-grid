package uk.gov.phe.erdst.sc.awag.service.page;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayloadConstants;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayloadUtils;

@Stateless
public class ResponseUrlPager
{
    private static final String SEPARATOR = "/";
    private static final String URL_PARAM_VALUE_ASSIGN = "=";
    private static final String URL_PARAM_SPLIT = "&";

    /**
     * String should be formatted accordingly.
     * 0 - servlet path.
     * 1 - offset.
     * 2 - limit
     * 3 - any other parameters that need to be added (must be in get parameter format i.e separated
     * by &)
     */
    private static final String PAGE_TEMPLATE = "%s?%s&%s&%s";

    /**
     * Creates URLs to get the next, previous and current set of results via a HTTP GET request.
     * @param request
     * @param offset
     * @param limit
     * @param responsePayload
     * @deprecated Create unit tests before using this functionality.
     */
    @Deprecated
    public void setPagingOffsetMetadata(HttpServletRequest request, Integer offset, Integer limit,
        ResponsePayload responsePayload)
    {
        String self = getSelfPage(request);
        String next = getNextPage(request, offset, limit);
        String previous = getPreviousPage(request, offset, limit);

        responsePayload.getMetadata().put(ResponsePayloadConstants.META_OFFSET, offset);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_LIMIT, limit);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_SELF, self);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_NEXT, next);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_PREV, previous);
    }

    private String getPassThroughParams(HttpServletRequest request)
    {
        StringBuilder passThroughParams = new StringBuilder();

        for (String key : request.getParameterMap().keySet())
        {
            // if the key is not to do with paging add it to passThroughParams
            if (!key.equals(ResponsePayloadConstants.META_LIMIT) && !key.equals(ResponsePayloadConstants.META_OFFSET)
                && !key.equals(ResponsePayloadConstants.CALLBACK))
            {
                passThroughParams.append(key).append(URL_PARAM_VALUE_ASSIGN).append(request.getParameterValues(key)[0])
                    .append(URL_PARAM_SPLIT);
            }
        }
        int paramsLength = passThroughParams.length();
        passThroughParams.delete(paramsLength - 1, paramsLength);

        return passThroughParams.toString();
    }

    private String getSelfPage(HttpServletRequest request)
    {
        String self = ResponsePayloadUtils.getEncodedUrl(request.getServletPath() + SEPARATOR
            + request.getQueryString());
        return self;
    }

    private String getNextPage(HttpServletRequest request, Integer offset, Integer limit)
    {
        String nextPage = null;
        if (offset != null && limit != null)
        {
            Integer increment = offset + limit;

            String offsetParam = ResponsePayloadConstants.META_OFFSET + URL_PARAM_VALUE_ASSIGN
                + String.valueOf(increment);
            String limitParam = ResponsePayloadConstants.META_LIMIT + URL_PARAM_VALUE_ASSIGN + String.valueOf(limit);

            String unencodedNextPage = String.format(PAGE_TEMPLATE, request.getServletPath(), offsetParam, limitParam,
                getPassThroughParams(request));

            nextPage = ResponsePayloadUtils.getEncodedUrl(unencodedNextPage);
        }

        return nextPage;
    }

    private String getPreviousPage(HttpServletRequest request, Integer offset, Integer limit)
    {
        String prevPage = null;
        if (offset != null && limit != null)
        {
            Integer decrement = offset - limit;
            String offsetParam;
            if (decrement >= 0)
            {
                offsetParam = ResponsePayloadConstants.META_OFFSET + URL_PARAM_VALUE_ASSIGN + String.valueOf(decrement);
            }
            else
            {
                offsetParam = ResponsePayloadConstants.META_OFFSET + URL_PARAM_VALUE_ASSIGN + String.valueOf(0);
            }

            String limitParam = ResponsePayloadConstants.META_LIMIT + URL_PARAM_VALUE_ASSIGN + String.valueOf(limit);

            String unencodedPrevPage = String.format(PAGE_TEMPLATE, request.getServletPath(), offsetParam, limitParam,
                getPassThroughParams(request));

            prevPage = ResponsePayloadUtils.getEncodedUrl(unencodedPrevPage);
        }

        return prevPage;
    }
}
