package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import java.util.HashSet;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.dao.AnimalHousingDao;
import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.dao.SpeciesDao;
import uk.gov.phe.erdst.sc.awag.dao.StudyDao;
import uk.gov.phe.erdst.sc.awag.dao.UserDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessment;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentParameterFactorScore;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Stateless
public class ImportAssessmentFactory
{

    @Inject
    private AssessmentReasonDao mAssessmentReasonDao;

    @Inject
    private UserDao mUserDao;

    @Inject
    private AnimalDao mAnimalDao;

    @Inject
    private AnimalHousingDao mAnimalHousingDao;

    @Inject
    private StudyDao mStudyDao;

    @Inject
    private SpeciesDao mSpeciesDao;

    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_DATE_ASSESSMENT = 1;
    private static int CSV_COLUMN_IS_COMPLETE = 2;
    private static int CSV_COLUMN_ANIMAL_NUMBER = 3;
    private static int CSV_COLUMN_ANIMAL_HOUSING_NAME = 4;
    private static int CSV_COLUMN_ASSESSMENT_REASON_NAME = 5;
    private static int CSV_COLUMN_ASSESSMENT_PERFORMED_BY = 6;
    private static int CSV_COLUMN_START_PARAMETER_COMMENTS_AND_SCORING = 7;
    private static Long START_PARAMETER_NUMBER = 1L;

    public ImportAssessment create(Long assessmentTemplateId, String[] importAssessmentCSVLineData)
    {
        // TODO Will accept the import format to default to '1'.
        // TODO The template has already been validated

        final Long lineNumber = Long.parseLong(importAssessmentCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String dateAssessment = importAssessmentCSVLineData[CSV_COLUMN_DATE_ASSESSMENT];
        final Boolean isAssessmentComplete = UploadUtils
            .convertToBoolean(importAssessmentCSVLineData[CSV_COLUMN_IS_COMPLETE]);
        final String animalNumber = importAssessmentCSVLineData[CSV_COLUMN_ANIMAL_NUMBER];
        final String animalHousingName = importAssessmentCSVLineData[CSV_COLUMN_ANIMAL_HOUSING_NAME];
        final String assessmentReasonName = importAssessmentCSVLineData[CSV_COLUMN_ASSESSMENT_REASON_NAME];
        final String performedByUser = importAssessmentCSVLineData[CSV_COLUMN_ASSESSMENT_PERFORMED_BY];

        final Long animalNumberId = getAnimal(animalNumber);
        final Long animalHousingId = getAnimalHousing(animalHousingName);
        final Long assessmentReasonId = getAssessmentReason(assessmentReasonName);

        ImportAssessment importAssessment = new ImportAssessment();
        importAssessment.setLineNumber(lineNumber);
        importAssessment.setImportassessmenttemplateid(assessmentTemplateId);
        importAssessment.setDateAssessment(dateAssessment); // TODO validate the format
        importAssessment.setIscomplete(isAssessmentComplete);
        importAssessment.setAnimalNumber(animalNumber);
        importAssessment.setAnimalHousingName(animalHousingName);
        importAssessment.setAssessmentreasonName(assessmentReasonName);
        importAssessment.setAnimalnumberid(animalNumberId);
        importAssessment.setAnimalhousingid(animalHousingId);
        importAssessment.setAssessmentreasonid(assessmentReasonId);
        importAssessment.setPerformedByUser(performedByUser);
        importAssessment.setImportAssessmentParameterFactorScores(new HashSet<>());

        Long parameterNumber = START_PARAMETER_NUMBER;
        for (int index = CSV_COLUMN_START_PARAMETER_COMMENTS_AND_SCORING; index < importAssessmentCSVLineData.length; ++index)
        {
            final String parameterFactorScores = importAssessmentCSVLineData[index];
            final String parameterComments = importAssessmentCSVLineData[index + 1];

            ImportAssessmentParameterFactorScore assessmentParameterFactorScore = new ImportAssessmentParameterFactorScore();
            assessmentParameterFactorScore.setFactorScores(parameterFactorScores);
            assessmentParameterFactorScore.setParameterComments(parameterComments);
            assessmentParameterFactorScore.setParameterNumber(parameterNumber);
            importAssessment.addImportAssessmentParameterFactorScore(assessmentParameterFactorScore);

            ++index;
            ++parameterNumber;
        }

        return importAssessment;
    }

    private Long getAnimal(final String animalNumber)
    {
        try
        {
            Animal animal = mAnimalDao.getAnimal(animalNumber);
            return animal.getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getSpecies(final String speciesName)
    {

        try
        {
            return mSpeciesDao.getSpecies(speciesName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getStudy(final String studyNumber)
    {
        try
        {
            return mStudyDao.getEntityByNameField(studyNumber).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getAssessmentReason(final String assessmentReasonName)
    {
        try
        {
            return mAssessmentReasonDao.getEntityByNameField(assessmentReasonName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getAnimalHousing(final String animalHousingName)
    {
        try
        {

            return mAnimalHousingDao.getEntityByNameField(animalHousingName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {

            return null;
        }
    }

}
