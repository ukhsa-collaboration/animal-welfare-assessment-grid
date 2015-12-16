package uk.gov.phe.erdst.sc.awag.datamodel.client;

import javax.validation.constraints.Min;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class PagingGetRequestParams
{
    @Min(value = ValidationConstants.MIN_PAGING_OFFSET_VALUE, message = ValidationConstants.PAGING_OFFSET_NAME,
        payload = ValidationConstants.NonNegativeInteger.class)
    public Integer offset;

    @Min(value = ValidationConstants.MIN_PAGING_LIMIT_VALUE, message = ValidationConstants.PAGING_LIMIT_NAME,
        payload = ValidationConstants.NonZeroPositiveInteger.class)
    public Integer limit;
}
