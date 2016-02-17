package uk.gov.phe.erdst.sc.awag.service.factory.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class UserAuthFactory
{
    private static final Logger LOGGER = LogManager.getLogger(UserAuthFactory.class.getName());

    public UserAuth create(UserAuthClientData clientData)
    {
        UserAuth user = new UserAuth();
        user.setUsername(clientData.userName);
        setNonIdProperties(user, clientData);
        return user;
    }

    public void update(UserAuth user, UserAuthClientData clientData)
    {
        setNonIdProperties(user, clientData);
    }

    private String getHashedPassword(String password)
    {
        MessageDigest digest = null;
        String hashedEncodedPassword = null;
        try
        {
            digest = MessageDigest.getInstance(Constants.AW_DIGEST);
            hashedEncodedPassword = DatatypeConverter
                .printHexBinary(digest.digest(password.getBytes(Constants.AW_CHARSET)));

        }
        catch (NoSuchAlgorithmException ex)
        {
            LOGGER.error(ex.getMessage());
        }

        return hashedEncodedPassword;
    }

    private void setNonIdProperties(UserAuth user, UserAuthClientData clientData)
    {
        user.setPassword(getHashedPassword(clientData.password));
    }
}
