package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudyGroupAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.studygroup.StudyGroupDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.studygroup.StudyGroupsDto;

@Stateless
public class StudyGroupController
{
    @Inject
    private StudyGroupDao mStudyGroupDao;

    @Inject
    private StudyGroupDtoFactory mStudyGroupDtoFactory;

    @Inject
    private Validator mValidator;

    @Inject
    private StudyGroupFactory mStudyGroupFactory;

    @Inject
    private AnimalDao mAnimalDao;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private StudyGroupFactory importStudyGroupFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_STUDY_GROUP)
    public EntityCreateResponseDto createStudyGroup(StudyGroupClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<StudyGroupClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            StudyGroup studyGroup = mStudyGroupFactory.create(clientData);
            studyGroup = mStudyGroupDao.store(studyGroup);
            response.id = studyGroup.getId();
            response.value = studyGroup.getStudyGroupNumber();

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_STUDY_GROUP)
    public StudyGroupDto updateStudyGroup(Long studyGroupId, StudyGroupClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<StudyGroupClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.studyGroupId, studyGroupId,
                ValidationConstants.UPDATE_ID_MISMATCH);

            StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(studyGroupId);
            mStudyGroupFactory.update(studyGroup, clientData);
            studyGroup = mStudyGroupDao.store(studyGroup);
            return mStudyGroupDtoFactory.createDto(studyGroup);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public StudyGroupsDto getStudyGroupsLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            Set<StudyGroup> studyGroups = mStudyGroupDao.getStudyGroupsLike(likeFilterParam.value, offset, limit);
            StudyGroupsDto response = new StudyGroupsDto();
            response.studyGroups = mStudyGroupDtoFactory.createStudyGroupDtos(studyGroups);

            if (pagingParams.isParamsSet())
            {
                Long studyGroupsCount = mStudyGroupDao.getCountStudyGroupsLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, studyGroupsCount);
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

    public StudyGroupDto getStudyGroupById(Long id) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(id);
        StudyGroup studyGroup = mStudyGroupDao.getStudyGroup(id);
        return mStudyGroupDtoFactory.createDto(studyGroup);
    }

    public StudyGroup getStudyGroupWithAnimalNonApiMethod(Animal animal, Study study)
    {
        for (StudyGroup group : study.getStudyGroups())
        {
            if (group.getAnimals().contains(animal))
            {
                return group;
            }
        }

        return null;
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_STUDY_GROUP)
    public ResponseDto uploadStudyGroup(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {

        try
        {
            final String[] expectedHeaderColumns = new String[] {"study-group-number", "study-group-animals"};
            final ArrayList<String[]> csvLines = UploadUtils.retrieveCSVLines(uploadFile, expectedHeaderColumns);

            ImportHeader importHeader = uploadToImportUploadStudyGroupTables(csvLines, loggedUser);

            upload(importHeader);

            importHeaderDao.realDelete(importHeader.getImportheaderid());

        }
        catch (AWNoSuchEntityException ex)
        {
            throw new AWInputValidationException(ex.getMessage());

        }
        catch (AWNonUniqueException ex)
        {
            throw new AWInputValidationException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new AWInputValidationException(Constants.Upload.ERR_IMPORT_INVALID_FORMAT_ABORT);
        }

        return new UploadResponseDto();
    }

    // TODO validate existing
    // TODO Collection not Set
    private void upload(ImportHeader importHeader) throws AWNoSuchEntityException, AWNonUniqueException
    {
        List<StudyGroup> studyGroups = new ArrayList<>();

        for (ImportStudyGroup importStudyGroup : importHeader.getImportStudyGroups())
        {
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setStudyGroupNumber(importStudyGroup.getStudygroupnumber());

            Set<Animal> studyGroupAnimals = new HashSet<>();
            for (ImportStudyGroupAnimal importStudyGroupAnimal : importStudyGroup.getImportStudyGroupAnimals())
            {
                if (importStudyGroupAnimal.getStudygroupanimalnumberid() != null)
                {
                    Animal animal = mAnimalDao.getAnimal(importStudyGroupAnimal.getStudygroupanimalnumberid());
                    studyGroupAnimals.add(animal);
                }
            }
            studyGroup.setAnimals(studyGroupAnimals);
            studyGroups.add(studyGroup);
        }

        mStudyGroupDao.upload(studyGroups);
    }

    private ImportHeader uploadToImportUploadStudyGroupTables(ArrayList<String[]> csvLines, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException
    {

        ImportHeader importHeader = new ImportHeader();
        Set<ImportStudyGroup> importStudyGroups = new HashSet<>();
        importHeader.setUserName(loggedUser.username);
        importHeader.setDateImport(new java.util.Date());
        importHeader.setImportStudyGroups(importStudyGroups);

        for (String[] csvLine : csvLines)
        {
            final String studyGroupNumber = csvLine[0];
            final String studyGroupAnimalNumbersList = csvLine[1];
            final List<String> studyGroupAnimalNumber = Arrays.asList(studyGroupAnimalNumbersList.split(","));
            final Long studyGroupNumberId = getStudyGroup(studyGroupNumber);

            ImportStudyGroup importStudyGroup = new ImportStudyGroup();
            importStudyGroup.setStudygroupnumber(studyGroupNumber);
            importStudyGroup.setStudygroupnumberid(studyGroupNumberId);
            importStudyGroup.setStudygroupanimalnumbers(studyGroupAnimalNumbersList);
            importStudyGroup.setImportStudyGroupAnimals(new HashSet<>());

            for (String animalNumber : studyGroupAnimalNumber)
            {
                final String trimmedAnimalNumber = animalNumber.trim();
                final Long animalNumberId = getAnimal(trimmedAnimalNumber);

                ImportStudyGroupAnimal importStudyGroupAnimal = new ImportStudyGroupAnimal();
                importStudyGroupAnimal.setStudygroupanimalnumber(animalNumber);
                importStudyGroupAnimal.setStudygroupanimalnumberid(animalNumberId);

                importStudyGroup.addImportStudyGroupAnimal(importStudyGroupAnimal);
            }

            importHeader.addImportStudyGroup(importStudyGroup);
        }

        importHeader = importHeaderDao.store(importHeader);

        return importHeader;
    }

    private final Long getStudyGroup(String studyGroupNumber)
    {
        try
        {
            return mStudyGroupDao.getStudyGroup(studyGroupNumber).getId();
        }
        catch (AWNoSuchEntityException ex)
        {

            return null;
        }
    }

    private final Long getAnimal(String animalNumber)
    {
        try
        {
            return mAnimalDao.getAnimal(animalNumber).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

}
