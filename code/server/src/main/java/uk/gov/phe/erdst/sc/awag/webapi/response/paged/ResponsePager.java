package uk.gov.phe.erdst.sc.awag.webapi.response.paged;

import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;

public final class ResponsePager
{
    private ResponsePager()
    {
    }

    public static PagingInfo getPagingInfo(PagingQueryParams pagingParams, Long totalPageableObjects)
    {
        Long totalPages = getTotalPages(pagingParams.limit, totalPageableObjects);
        Integer pageNumber = getPageNumber(pagingParams.offset, pagingParams.limit, totalPageableObjects);

        return new PagingInfo(totalPageableObjects, pageNumber, totalPages);
    }

    private static Integer getPageNumber(Integer offset, Integer limit, Long totalPagableObjects)
    {
        if (totalPagableObjects == 0)
        {
            return Constants.WebApi.PAGING_RESPONSE_INVALID_PAGE_NUMBER;
        }

        Long offsetLong = Long.valueOf(offset);
        if (totalPagableObjects.equals(offsetLong))
        {
            return Constants.WebApi.PAGING_RESPONSE_INVALID_PAGE_NUMBER;
        }

        return (offset / limit) + 1;
    }

    private static long getTotalPages(Integer limit, Long totalPageableObjects)
    {
        Long modTotal = totalPageableObjects % limit;

        if (modTotal == 0)
        {
            return totalPageableObjects / limit;
        }

        double totalPages = totalPageableObjects / limit;
        return ((long) totalPages) + 1;
    }

}
