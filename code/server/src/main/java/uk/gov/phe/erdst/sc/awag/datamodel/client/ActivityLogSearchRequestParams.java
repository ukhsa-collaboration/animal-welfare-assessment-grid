package uk.gov.phe.erdst.sc.awag.datamodel.client;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidActivityLogSearchRequest;

@ValidActivityLogSearchRequest
public class ActivityLogSearchRequestParams
{
    // Validated by the custom validator
    public String dateFrom;

    // Validated by the custom validator
    public String dateTo;
}
