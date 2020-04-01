package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.ImportHeaderDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyGroupDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudy;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInputValidationException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWSeriousException;
import uk.gov.phe.erdst.sc.awag.service.ImportHeaderFactory;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActions;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedActivity;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.service.factory.study.ImportStudyFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyFactory;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.LikeFilterParam;
import uk.gov.phe.erdst.sc.awag.webapi.request.PagingQueryParams;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.EntityCreateResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.UploadResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudiesDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudyDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudySimpleDto;

@Stateless
public class StudyController
{
    private static final Logger LOGGER = LogManager.getLogger(StudyController.class.getName());

    @Inject
    private AnimalController mAnimalController;

    @Inject
    private StudyDao studyDao;

    @Inject
    private Validator mValidator;

    @Inject
    private StudyFactory studyFactory;

    @Inject
    private StudyDtoFactory mStudyDtoFactory;

    @Inject
    private StudyGroupDao mStudyGroupDao;

    @Inject
    private ImportHeaderFactory importHeaderFactory;

    @Inject
    private ImportStudyFactory importStudyFactory;

    @Inject
    private ImportHeaderDao importHeaderDao;

    @LoggedActivity(actionName = LoggedActions.CREATE_STUDY)
    public EntityCreateResponseDto createStudy(StudyClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(clientData);
        if (constraintViolations.isEmpty())
        {
            EntityCreateResponseDto response = new EntityCreateResponseDto();
            Study study = studyFactory.create(clientData);
            study = studyDao.store(study);
            response.id = study.getId();
            response.value = study.getStudyNumber();
            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    @LoggedActivity(actionName = LoggedActions.UPDATE_STUDY)
    public StudyDto updateStudy(Long studyId, StudyClientData clientData, LoggedUser loggedUser)
        throws AWInputValidationException, AWNoSuchEntityException, AWNonUniqueException, AWSeriousException
    {
        Set<ConstraintViolation<StudyClientData>> constraintViolations = mValidator.validate(clientData);

        if (constraintViolations.isEmpty())
        {
            ValidatorUtils.validateUpdateId(clientData.studyId, studyId, ValidationConstants.UPDATE_ID_MISMATCH);

            Study study = studyDao.getEntityById(studyId);
            studyFactory.update(study, clientData);
            study = studyDao.update(study);
            return mStudyDtoFactory.createStudyDto(study);
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(constraintViolations));
            return null;
        }
    }

    public StudiesDto getAllStudies(PagingQueryParams pagingParams) throws AWInputValidationException
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

            Collection<Study> studies = studyDao.getEntities(offset, limit);

            StudiesDto response = new StudiesDto();
            response.studies = mStudyDtoFactory.createStudyDtos(studies);

            if (pagingParams.isParamsSet())
            {
                Long studiesCount = studyDao.getEntityCount();
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, studiesCount);
            }

            return response;
        }
        else
        {
            ValidatorUtils.throwInputValidationExceptionWith(Arrays.asList(pagingParamsViolations));
            return null;
        }
    }

    public StudiesDto getStudiesLike(LikeFilterParam likeFilterParam, PagingQueryParams pagingParams)
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

            Collection<Study> studies = studyDao.getEntitiesLike(likeFilterParam.value, offset, limit);
            StudiesDto response = new StudiesDto();
            response.studies = mStudyDtoFactory.createStudyDtos(studies);

            if (pagingParams.isParamsSet())
            {
                Long studiesCount = studyDao.getEntityCountLike(likeFilterParam.value);
                response.pagingInfo = ResponsePager.getPagingInfo(pagingParams, studiesCount);
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

    public StudyDto getStudyById(Long studyId) throws AWNoSuchEntityException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(studyId);

        Study study = studyDao.getEntityById(studyId);
        return mStudyDtoFactory.createStudyDto(study);
    }

    public StudySimpleDto getStudyWithAnimal(Long animalId)
        throws AWNoSuchEntityException, AWSeriousException, AWInputValidationException
    {
        ValidatorUtils.validateEntityId(animalId);

        try
        {
            Study study = getStudyWithAnimalNonApiMethod(animalId);
            return mStudyDtoFactory.createStudySimpleDto(study);
        }
        catch (AWMultipleResultException e)
        {
            LOGGER.error(e);
            throw new AWSeriousException(e);
        }
    }

    /**
     * @see uk.gov.phe.erdst.sc.awag.dao.StudyDao.getStudyWithAnimal
     */
    public Study getStudyWithAnimalNonApiMethod(Long animalId) throws AWNoSuchEntityException, AWMultipleResultException
    {
        Animal animal = mAnimalController.getAnimalNonApiMethod(animalId);
        return studyDao.getStudyWithAnimal(animal);
    }

    @LoggedActivity(actionName = LoggedActions.UPLOAD_STUDY)
    public ResponseDto uploadStudy(InputStream uploadFile, LoggedUser loggedUser) throws AWInputValidationException
    {
        try
        {
            final ArrayList<String[]> csvLinesData = UploadUtils.retrieveCSVLines(uploadFile,
                Constants.Upload.UPLOAD_HEADER_STUDY_COLUMNS);

            ImportHeader importHeader = importHeaderFactory.createWithImportStudies(loggedUser);
            for (String[] uploadCSVLineData : csvLinesData)
            {
                ImportStudy importStudy = importStudyFactory.create(uploadCSVLineData);
                importHeader.addImportStudy(importStudy);
            }

            importHeader = importHeaderDao.store(importHeader);

            /*
            uploadScalesFromImport(importHeader); // TODO this code to be moved in the client (test for now)
            
            ImportHeader importHeader = uploadToImportUploadStudyTables(csvLines, loggedUser);
            
            upload(importHeader);
            
            importHeaderDao.realDelete(importHeader.getImportheaderid());
            */

        }
        /*
        catch (AWNoSuchEntityException ex)
        {
            throw new AWInputValidationException(ex.getMessage());
        }
        */
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

    // TODO convert for REST API
    private void uploadStudiesFromImport(ImportHeader importHeader) throws AWNonUniqueException
    {
        Collection<Study> studies = new ArrayList<>();
        for (ImportStudy importStudy : importHeader.getImportStudies())
        {
            Study study = studyFactory.create(importStudy);
            studies.add(study);
        }

        studyDao.upload(studies);

        importHeaderDao.realDelete(importHeader.getImportheaderid());
    }

    /*
    private ImportHeader uploadToImportUploadStudyTables(ArrayList<String[]> csvLines, LoggedUser loggedUser)
        throws AWInputValidationException, AWNonUniqueException
    {
        ImportHeader importHeader = new ImportHeader();
        Set<ImportStudy> importStudies = new HashSet<>();
        importHeader.setUserName(loggedUser.username);
        importHeader.setDateImport(new java.util.Date());
        importHeader.setImportStudies(importStudies);

        for (String[] csvLine : csvLines)
        {
            final String studyNumber = csvLine[0];
            final Boolean isStudyOpen = UploadUtils.convertToBoolean(csvLine[2]);
            final String studyGroupNumbersList = csvLine[2];
            final List<String> studyGroupNumbers = Arrays.asList(studyGroupNumbersList.split(","));
            final Long studyId = getStudyGroup(studyNumber);

            ImportStudy importStudy = new ImportStudy();
            importStudy.setStudynumber(studyNumber);
            importStudy.setStudynumberid(studyId);
            importStudy.setIsstudyopen(isStudyOpen);
            importStudy.setStudystudygroupnumbers(studyGroupNumbersList);
            importStudy.setImportStudyStudyGroups(new HashSet<ImportStudyStudyGroup>());

            for (String studyGroup : studyGroupNumbers)
            {
                final String trimmedStudyGroup = studyGroup.trim();
                final Long studyGroupId = getStudyGroup(trimmedStudyGroup);

                // TODO duplicates in the table
                ImportStudyStudyGroup importStudyStudyGroup = new ImportStudyStudyGroup();
                importStudyStudyGroup.setStudystudygroupnumber(trimmedStudyGroup);
                importStudyStudyGroup.setStudystudygroupnumberid(studyGroupId);
                importStudy.addImportStudyStudyGroup(importStudyStudyGroup);
            }

            importHeader.addImportStudy(importStudy);

        }

        importHeader = mImportHeaderDao.store(importHeader);

        return importHeader;

    }

    // TODO validate existing
    // TODO Collection not Set
    private void upload(ImportHeader importHeader) throws AWNoSuchEntityException, AWNonUniqueException
    {
        List<Study> studies = new ArrayList<>();

        for (ImportStudy importStudy : importHeader.getImportStudies())
        {
            Study study = new Study();
            study.setStudyNumber(importStudy.getStudynumber());

            Set<StudyGroup> studyGroups = new HashSet<>();
            for (ImportStudyStudyGroup importStudyStudyGroup : importStudy.getImportStudyStudyGroups())
            {
                if (importStudyStudyGroup.getStudystudygroupnumberid() != null)
                {
                    StudyGroup studyGroup = mStudyGroupDao
                        .getStudyGroup(importStudyStudyGroup.getStudystudygroupnumberid());
                    studyGroups.add(studyGroup);
                }

            }
            study.setStudyGroups(studyGroups);
            studies.add(study);

        }

        mStudyDao.upload(studies);
    }
    */

    public final Long getStudyNumberNonApiMethod(String studyNumber)
    {
        try
        {
            return studyDao.getEntityByNameField(studyNumber).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    // TODO: Here?
    public final Long getStudyGroupNumberNonApiMethod(String studyGroupNumber)
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

}
