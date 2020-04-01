package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.user.UserDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.user.UsersDto;

@Stateless
public class UserController
{
    @Inject
    private UserFactory mUserFactory;

    @Inject
    private UserDtoFactory mUserDtoFactory;

    @Inject
    private UserDao mUserDao;

    @Inject
    private Validator mValidator;

    @LoggedActivity(actionName = LoggedActions.CREATE_USER)
    public EntityCreateResponseDto createUser(UserClientData clientData, LoggedUser loggedUser)
        throws AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        Set<ConstraintViolation<UserClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            User user = mUserFactory.create(clientData);
            user = mUserDao.store(user);
            response.id = user.getId();
            response.value = user.getName();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public UsersDto getAllUsers(PagingQueryParams pagingParams) throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);

        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty())
        {
            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            Collection<User> users = mUserDao.getEntities(offset, limit);
            UsersDto response = new UsersDto();
            response.users = mUserDtoFactory.createDtos(users);

            if (pagingParams.isParamsSet())
            {
                long userCount = mUserDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, userCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public UsersDto getUsersLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
        throws AWInputValidationException
    {
        Set<ConstraintViolation<PagingQueryParams>> pagingParamsViolations = new HashSet<>(0);

        Set<ConstraintViolation<LikeFilterParam>> likeParamViolations = mValidator.validate(likeFilterParam);
        boolean isPagingParamsSet = pagingParams.isParamsSet();

        if (isPagingParamsSet)
        {
            pagingParamsViolations = mValidator.validate(pagingParams);
        }

        if (pagingParamsViolations.isEmpty() && likeParamViolations.isEmpty())
        {
            Integer offset = isPagingParamsSet ? pagingParams.offset : null;
            Integer limit = isPagingParamsSet ? pagingParams.limit : null;

            List<User> users = mUserDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            UsersDto response = new UsersDto();
            response.users = mUserDtoFactory.createDtos(users);

            if (pagingParams.isParamsSet())
            {
                Long count = mUserDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, count);
            }

            return response;
        }
        else
        {
            ValidatorUtils
                .throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations, likeParamViolations));
            return null;
        }
    }

    public UserDto getUserById(Long id) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(id);
        User user = mUserDao.getEntityById(id);
        return mUserDtoFactory.createUserDto(user);
    }

    public User getUserByNameNonApiMethod(String name) throws AWNoSuchEntityException
    {
        return mUserDao.getEntityByNameField(name);
    }

    public User createUserNonApi(UserClientData clientData, LoggedUser loggedUser) throws AWNonUniqueException
    {
        // TODO unit test
        User user = mUserFactory.create(clientData);
        user = mUserDao.store(user);
        return user;
    }

}
