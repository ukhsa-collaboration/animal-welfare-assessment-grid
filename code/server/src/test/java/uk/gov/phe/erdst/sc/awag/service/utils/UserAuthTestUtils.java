package uk.gov.phe.erdst.sc.awag.service.utils;

import uk.gov.phe.erdst.sc.awag.webapi.request.UserAuthClientData;

public final class UserAuthTestUtils
{
    private UserAuthTestUtils()
    {
    }

    public static final UserAuthClientData getUserAuthClientData(String username, String password,
        String passwordRetyped, String groupName)
    {
        UserAuthClientData clientData = new UserAuthClientData();
        clientData.userName = username;
        clientData.password = password;
        clientData.retypedPassword = passwordRetyped;
        clientData.groupName = groupName;

        return clientData;
    }
}
