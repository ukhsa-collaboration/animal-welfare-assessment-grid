package uk.gov.phe.erdst.sc.awag.service.page;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayloadConstants;

@Stateless
public class ResponsePager
{
    private static final int INVALID_PAGE_NUMBER = -1;

    public void setPagingTotalsMetadata(Integer offset, Integer limit, Long totalPageableObjects,
        ResponsePayload responsePayload)
    {
        if (offset == null || limit == null)
        {
            return;
        }

        Long totalPages = getTotalPages(limit, totalPageableObjects);
        Integer pageNumber = getPageNumber(offset, limit, totalPageableObjects);

        responsePayload.getMetadata().put(ResponsePayloadConstants.META_TOTAL_PAGES, totalPages);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_PAGE, pageNumber);
        responsePayload.getMetadata().put(ResponsePayloadConstants.META_TOTAL_OBJS, totalPageableObjects);
    }

    private Integer getPageNumber(Integer offset, Integer limit, Long totalPagableObjects)
    {
        if (totalPagableObjects == 0)
        {
            return INVALID_PAGE_NUMBER;
        }

        Long offsetLong = Long.valueOf(offset);
        if (totalPagableObjects.equals(offsetLong))
        {
            return INVALID_PAGE_NUMBER;
        }

        return (offset / limit) + 1;
    }

    private long getTotalPages(Integer limit, Long totalPageableObjects)
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
