package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.businesslogic.UserController;
import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.datamodel.client.UserClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserFactory;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;

@Stateless
public class UserControllerImpl implements UserController
{
    @Inject
    private UserFactory mUserFactory;

    @Inject
    private UserDao mUserDao;

    @Inject
    private Validator mUserValidator;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    private ResponsePager mResponsePager;

    @Override
    public Collection<User> getUsers(Integer offset, Integer limit, ResponsePayload responsePayload)
    {
        // XXX: make sure to return user DTOs if this is ever used
        Collection<User> users = mUserDao.getEntities(offset, limit);
        long userCount = mUserDao.getEntityCount();
        mResponsePager.setPagingTotalsMetadata(offset, limit, userCount, responsePayload);
        return users;
    }

    @Override
    public User getUser(String name) throws AWNoSuchEntityException
    {
        return mUserDao.getEntityByNameField(name);
    }

    @Override
    public Collection<EntitySelectDto> getUsersLike(String like, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata)
    {
        List<User> users = mUserDao.getEntitiesLike(like, offset, limit);
        if (includeMetadata)
        {
            Long count = mUserDao.getEntityCountLike(like);
            mResponsePager.setPagingTotalsMetadata(offset, limit, count, responsePayload);
        }
        return mEntitySelectDtoFactory.createEntitySelectDtos(users);
    }

    @Override
    public void storeUser(UserClientData clientData, ResponsePayload responsePayload)
    {
        Set<ConstraintViolation<UserClientData>> constraintViolations = mUserValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            User user = mUserFactory.create(clientData);
            try
            {
                mUserDao.store(user);
                clientData.userId = user.getId();
                responsePayload.setData(clientData);
            }
            catch (AWNonUniqueException ex)
            {
                responsePayload.addError(ex.getMessage());
            }
        }
        else
        {
            responsePayload.addValidationErrors(constraintViolations);
        }
    }
}
