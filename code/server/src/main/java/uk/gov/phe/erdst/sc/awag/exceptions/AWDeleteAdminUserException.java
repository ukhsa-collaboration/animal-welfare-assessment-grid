package uk.gov.phe.erdst.sc.awag.exceptions;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class AWDeleteAdminUserException extends Exception
{
    private static final long serialVersionUID = 1L;

    public AWDeleteAdminUserException()
    {
        super(ValidationConstants.ERR_DELETE_ADMIN_NOT_ALLOWED);
    }

    public AWDeleteAdminUserException(String message)
    {
        super(message);
    }
}
