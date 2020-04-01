package uk.gov.phe.erdst.sc.awag.webapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAuthUser;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAuthUser
public class UserAuthClientData
{
    @NotNull(message = ValidationConstants.AUTH_USER_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.AUTH_USER_NAME_SIZE_MIN, max = ValidationConstants.AUTH_USER_NAME_SIZE_MAX,
        message = ValidationConstants.AUTH_USER_NAME_PROPERTY + "|" + ValidationConstants.AUTH_USER_NAME_SIZE_MIN + "|"
            + ValidationConstants.AUTH_USER_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.AUTH_USER_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String userName;

    @NotNull(message = ValidationConstants.AUTH_PASSWORD_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.AUTH_PASSWORD_SIZE_MIN, max = ValidationConstants.AUTH_PASSWORD_SIZE_MAX,
        message = ValidationConstants.AUTH_PASSWORD_PROPERTY + "|" + ValidationConstants.AUTH_PASSWORD_SIZE_MIN + "|"
            + ValidationConstants.AUTH_PASSWORD_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = ValidationConstants.AUTH_PASSWORD_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String password;

    @NotNull(message = ValidationConstants.AUTH_RETYPED_PASSWORD_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.AUTH_PASSWORD_SIZE_MIN, max = ValidationConstants.AUTH_PASSWORD_SIZE_MAX,
        message = ValidationConstants.AUTH_RETYPED_PASSWORD_PROPERTY + "|" + ValidationConstants.AUTH_PASSWORD_SIZE_MIN
            + "|" + ValidationConstants.AUTH_PASSWORD_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = ValidationConstants.AUTH_RETYPED_PASSWORD_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    public String retypedPassword;

    @NotNull(message = ValidationConstants.AUTH_GROUP_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Size(min = ValidationConstants.AUTH_GROUP_SIZE_MIN, max = ValidationConstants.AUTH_GROUP_SIZE_MAX,
        message = ValidationConstants.AUTH_GROUP_PROPERTY + "|" + ValidationConstants.AUTH_GROUP_SIZE_MIN + "|"
            + ValidationConstants.AUTH_GROUP_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    public String groupName;
}
