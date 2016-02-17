package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface UserController
{
    Collection<User> getUsers(Integer offset, Integer limit, ResponsePayload responsePayload);

    Collection<EntitySelectDto> getUsersLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    User getUser(String name) throws AWNoSuchEntityException;

    void storeUser(UserClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);
}
