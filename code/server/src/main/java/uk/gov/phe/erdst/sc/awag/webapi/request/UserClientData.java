package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class UserClientData
{
    @ValidId(message = ValidationConstants.USER_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.USER_ENTITY_NAME, payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long userId;

    @NotNull(message = ValidationConstants.USER_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.USER_NAME_SIZE_MIN, max = ValidationConstants.USER_NAME_SIZE_MAX,
        message = ValidationConstants.USER_NAME_PROPERTY + "|" + ValidationConstants.USER_NAME_SIZE_MIN + "|"
            + ValidationConstants.USER_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.USER_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String userName;

    public UserClientData()
    {
    }

    public UserClientData(Long userId, String userName)
    {
        this.userId = userId;
        this.userName = userName;
    }
}
