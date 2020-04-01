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

import uk.gov.phe.erdst.sc.awag.dao.UserAuthDao;
import uk.gov.phe.erdst.sc.awag.dao.UserGroupAuthDao;
import uk.gov.phe.erdst.sc.awag.datamodel.GroupAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserAuth;
import uk.gov.phe.erdst.sc.awag.datamodel.UserGroupAuth;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.GroupAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.auth.UserGroupAuthFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserAuthClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.UserAuthDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.userauth.UserAuthsDto;

@Stateless
public class UserAuthController
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
    private Validator mValidator;

    @Inject
    private UserAuthDao mUserAuthDao;

    @Inject
    private UserGroupAuthDao mUserGroupAuthDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_USER_ACCOUNT)
    public UserAuthDto createUser(UserAuthClientData clientData, LoggedUser loggedUser)
        throws AWNonUniqueException, AWInputValidationException, AWSeriousException
    {
        Set<ConstraintViolation<UserAuthClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            UserAuth user = mUserAuthFactory.create(clientData);
            List<GroupAuth> groups = mGroupAuthFactory.create(clientData);
            List<UserGroupAuth> userGroups = mUserGroupAuthFactory.create(user, groups);
            user.setUserGroups(userGroups);
            user = mUserAuthDao.store(user);
            return mUserAuthDtoFactory.create(user);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.DELETE_USER_ACCOUNT)
    public void deleteUser(String username, LoggedUser loggedUser)
        throws AWNoSuchEntityException, AWInputValidationException, AWSeriousException
    {
        UserAuth userAuth = mUserAuthDao.getEntityById(username);
        if (username.equals(WebSecurityUtils.AW_ADMIN_USER))
        {
            ValidatorUtils.throwInputValidationExceptionWith(ValidationConstants.ERR_DELETE_ADMIN_NOT_ALLOWED);
        }

        mUserAuthDao.removeEntity(userAuth);
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_USER_ACCOUNT)
    public UserAuthDto updateUser(String username, UserAuthClientData clientData, LoggedUser loggedUser)
        throws AWSeriousException, AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException
    {
        Set<ConstraintViolation<UserAuthClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
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
            user = mUserAuthDao.store(user);

            return mUserAuthDtoFactory.create(user);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public UserAuthDto getUserAuthById(String username) throws AWNoSuchEntityException, AWInputValidationException
    {
        UserAuth user = mUserAuthDao.getEntityById(username);
        return mUserAuthDtoFactory.create(user);
    }

    public UserAuthsDto getAllUserAuths(PagingQueryParams pagingParams) throws AWInputValidationException
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

            Collection<UserAuth> authUsers = mUserAuthDao.getEntities(offset, limit);
            UserAuthsDto response = new UserAuthsDto();
            response.userAuths = mUserAuthDtoFactory.createDtos(authUsers);

            if (pagingParams.isParamsSet())
            {
                Long userAuthCount = mUserAuthDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, userAuthCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public UserAuthsDto getUserAuthLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            List<UserAuth> authUsers = mUserAuthDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            UserAuthsDto response = new UserAuthsDto();
            response.userAuths = mUserAuthDtoFactory.createDtos(authUsers);

            if (pagingParams.isParamsSet())
            {
                Long userAuthCount = mUserAuthDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, userAuthCount);
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
}
