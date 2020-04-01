package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.dao.GroupAuthDao;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAuthUser;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserAuthClientData;

@Stateless
public class UserAuthValidator implements ConstraintValidator<ValidAuthUser, UserAuthClientData>
{
    @Inject
    private GroupAuthDao groupDao;

    @Override
    public void initialize(ValidAuthUser constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(UserAuthClientData clientData, ConstraintValidatorContext context)
    {
        context.disableDefaultConstraintViolation();
        return (isPasswordEqualsRetypedPassword(clientData, context) && isValidGroup(clientData, context)
            && isNotAdminRoleChange(clientData, context));
    }

    private boolean isNotAdminRoleChange(UserAuthClientData clientData, ConstraintValidatorContext context)
    {
        if (clientData.userName.equals(WebSecurityUtils.AW_ADMIN_USER)
            && !clientData.groupName.equals(WebSecurityUtils.RolesAllowed.AW_ADMIN))
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_ADMIN_ROLE_CHANGE)
                .addConstraintViolation();
            return false;

        }
        return true;
    }

    private boolean isValidGroup(UserAuthClientData clientData, ConstraintValidatorContext context)
    {
        if (clientData.groupName == null)
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_GROUP_NAME_NULL)
                .addConstraintViolation();
            return false;
        }

        try
        {
            groupDao.getEntityById(clientData.groupName);
        }
        catch (AWNoSuchEntityException ex)
        {
            context.buildConstraintViolationWithTemplate(ex.getMessage()).addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isPasswordEqualsRetypedPassword(UserAuthClientData clientData, ConstraintValidatorContext context)
    {
        if (null == clientData.password || null == clientData.retypedPassword
            || !clientData.password.equals(clientData.retypedPassword))
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_PASSWORD_RETYPED_PASSWORD)
                .addConstraintViolation();
            return false;
        }
        return true;
    }

}
