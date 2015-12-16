package uk.gov.phe.erdst.sc.awag.service.factory.user;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserClientData;

@Stateless
public class UserFactory
{
    public User create(UserClientData clientData)
    {
        User user = new User();
        user.setName(clientData.userName);
        return user;
    }
}
