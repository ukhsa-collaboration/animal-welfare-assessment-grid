package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.UserAuthController;
import uk.gov.phe.erdst.sc.awag.dao.UserAuthDao;
import uk.gov.phe.erdst.sc.awag.dao.UserGroupAuthDao;
import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.UserAuthDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWDeleteAdminUserException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.GroupAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserGroupAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletSecurityUtils;

@Stateless
public class UserAuthControllerImpl implements UserAuthController
{
    @Inject
    private UserAuthFactory mUserAuthFactory;
    @Inject
    private GroupAuthFactory mGroupAuthFactory;
    @Inject
    private UserGroupAuthFactory mUserGroupAuthFactory;
    @Inject
    private UserAuthDtoFactory mUserAuthDtoFactory;
    @Inject
    private Validator mUserAuthValidator;
    @Inject
    private UserAuthDao mUserAuthDao;
    @Inject
    private UserGroupAuthDao mUserGroupAuthDao;
    @Inject
    private ResponsePager mResponsePager;
    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Override
    @LoggedActivity(actionName = LoggedActions.STORE_USER_ACCOUNT)
    public void storeUser(UserAuthClientData clientData, ResponsePayload responsePayload, LoggedUser loggedUser)
    {
        Set<ConstraintViolation<UserAuthClientData>> userAuthConstraintViolations = mUserAuthValidator
            .validate(clientData);

        if (userAuthConstraintViolations.isEmpty())
        {
            try
            {
                UserAuth user = mUserAuthFactory.create(clientData);
                List<GroupAuth> groups = mGroupAuthFactory.create(clientData);
                List<UserGroupAuth> userGroups = mUserGroupAuthFactory.create(user, groups);
                user.setUserGroups(userGroups);
                mUserAuthDao.store(user);
                UserAuthDto userDto = mUserAuthDtoFactory.create(user);
                responsePayload.setData(userDto);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(userAuthConstraintViolations);
        }
    }

    @Override
    @LoggedActivity(actionName = LoggedActions.DELETE_USER_ACCOUNT)
    public void deleteUser(String username, LoggedUser loggedUser) throws AWNoSuchEntityException,
        AWDeleteAdminUserException
    {
        if (username.equals(ServletSecurityUtils.AW_ADMIN_USER))
        {
            throw new AWDeleteAdminUserException();
        }
        UserAuth user = mUserAuthDao.getEntityById(username);
        mUserAuthDao.removeEntity(user);
    }

    @Override
    public List<EntitySelectDto> getUserAuthDtosLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<UserAuth> authUsers = mUserAuthDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long userAuthCount = mUserAuthDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, userAuthCount, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(authUsers);

    }

    @Override
    @LoggedActivity(actionName = LoggedActions.UPDATE_USER_ACCOUNT)
    public void updateUser(String username, UserAuthClientData clientData, ResponsePayload responsePayload,
        LoggedUser loggedUser)
    {
        Set<ConstraintViolation<UserAuthClientData>> userAuthConstraintViolations = mUserAuthValidator
            .validate(clientData);

        if (userAuthConstraintViolations.isEmpty())
        {
            try
            {
                UserAuth user = mUserAuthDao.getEntityById(username);
                mUserAuthFactory.update(user, clientData);
                user = mUserAuthDao.update(user);

                for (UserGroupAuth userGroupAuth : user.getUserGroups())
                {
                    mUserGroupAuthDao.removeEntity(userGroupAuth);
                }

                List<GroupAuth> groups = mGroupAuthFactory.create(clientData);
                List<UserGroupAuth> userGroups = mUserGroupAuthFactory.create(user, groups);

                user.setUserGroups(userGroups);
                mUserAuthDao.store(user);

                UserAuthDto userDto = mUserAuthDtoFactory.create(user);
                responsePayload.setData(userDto);
            }
            catch (AWNonUniqueException | AWNoSuchEntityException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(userAuthConstraintViolations);
        }
    }

    @Override
    public UserAuthDto getUserAuthById(String username) throws AWNoSuchEntityException
    {
        UserAuth user = mUserAuthDao.getEntityById(username);
        return mUserAuthDtoFactory.create(user);
    }
}
