package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.Min;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class PagingQueryParams
{
    @Min(value = ValidationConstants.MIN_PAGING_OFFSET_VALUE, message = ValidationConstants.PAGING_OFFSET_NAME,
        payload = ValidationConstants.NonNegativeInteger.class)
    public final Integer offset;

    @Min(value = ValidationConstants.MIN_PAGING_LIMIT_VALUE, message = ValidationConstants.PAGING_LIMIT_NAME,
        payload = ValidationConstants.NonZeroPositiveInteger.class)
    public final Integer limit;

    public PagingQueryParams(Integer offset, Integer limit)
    {
        this.offset = offset;
        this.limit = limit;
    }

    public boolean isParamsSet()
    {
        return offset != null && limit != null;
    }
}
