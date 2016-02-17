package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.UserAuthDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWDeleteAdminUserException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;

public interface UserAuthController
{
    void storeUser(UserAuthClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser);

    List<EntitySelectDto> getUserAuthDtosLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    void updateUser(String username, UserAuthClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser);

    UserAuthDto getUserAuthById(String username) throws AWNoSuchEntityException;

    void deleteUser(String username, LoggedUser loggedUser) throws AWNoSuchEntityException, AWDeleteAdminUserException;
}
