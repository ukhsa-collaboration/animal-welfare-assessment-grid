package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

public class LikeFilterParam
{
    @NotNull(message = Constants.WebApi.QUERY_PARAM_LIKE_FILTER,
        payload = ValidationConstants.RequestQueryParameterMustBeProvided.class)
    public final String value;

    public LikeFilterParam(String value)
    {
        this.value = value;
    }
}
