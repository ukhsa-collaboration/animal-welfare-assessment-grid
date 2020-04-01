package uk.gov.phe.erdst.sc.awag.webapi.response.paged;

public final class PagingInfo
{
    public final Long total;
    public final Integer page;
    public final Long totalPages;

    public PagingInfo(Long total, Integer page, Long totalPages)
    {
        this.total = total;
        this.page = page;
        this.totalPages = totalPages;
    }
}
