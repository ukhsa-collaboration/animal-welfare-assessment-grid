package uk.gov.phe.erdst.sc.awag.service.factory.animal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AnimalHousingController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentReasonController;
import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentTemplateController;
import uk.gov.phe.erdst.sc.awag.businesslogic.ImportAnimalController;
import uk.gov.phe.erdst.sc.awag.businesslogic.SexController;
import uk.gov.phe.erdst.sc.awag.businesslogic.SourceController;
import uk.gov.phe.erdst.sc.awag.businesslogic.SpeciesController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.businesslogic.UserController;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Stateless
public class ImportAnimalFactory
{
    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_ANIMAL_NUMBER = 1;
    private static int CSV_COLUMN_DATE_OF_BIRTH = 2;
    private static int CSV_COLUMN_SEX = 3;
    private static int CSV_COLUMN_SPECIES_NAME = 4;
    private static int CSV_COLUMN_SOURCE_NAME = 5;
    private static int CSV_COLUMN_DAM_ANIMAL_NAME = 6;
    private static int CSV_COLUMN_FATHER_ANIMAL_NAME = 7;
    private static int CSV_COLUMN_ASSESSMENT_TEMPLATE_NAME = 8;
    private static int CSV_COLUMN_ASSESSMENT_IS_ALIVE = 9;

    @Inject
    private SexController sexController;

    @Inject
    private SourceController sourceController;

    @Inject
    private SpeciesController speciesController;

    @Inject
    private AssessmentController assessmentController;

    @Inject
    private AnimalController animalController;

    @Inject
    private AnimalHousingController animalHousingController;

    @Inject
    private AssessmentReasonController assessmentReasonController;

    @Inject
    private UserController userController;

    @Inject
    private StudyController studyController;

    @Inject
    private AssessmentTemplateController assessmentTemplateController;

    @Inject
    private ImportAnimalController importAnimalController;

    private Long sexFemaleId;
    private Long sexMaleId;

    public ImportAnimal create(String[] importAnimalCSVLine)
    {
        storeSexes();

        final Long lineNumber = Long.parseLong(importAnimalCSVLine[CSV_COLUMN_LINE_NUMBER]);
        final String animalNumber = String.valueOf(importAnimalCSVLine[CSV_COLUMN_ANIMAL_NUMBER]);
        final String dateOfBirth = String.valueOf(importAnimalCSVLine[CSV_COLUMN_DATE_OF_BIRTH]);
        final String sex = String.valueOf(importAnimalCSVLine[CSV_COLUMN_SEX]);
        final String speciesName = String.valueOf(importAnimalCSVLine[CSV_COLUMN_SPECIES_NAME]);
        final String sourceName = String.valueOf(importAnimalCSVLine[CSV_COLUMN_SOURCE_NAME]);
        String damAnimalName = (String.valueOf(importAnimalCSVLine[CSV_COLUMN_DAM_ANIMAL_NAME]));
        if (damAnimalName.length() == 0)
        {
            damAnimalName = null;
        }

        String fatherAnimalName = String.valueOf(importAnimalCSVLine[CSV_COLUMN_FATHER_ANIMAL_NAME]);
        if (fatherAnimalName.length() == 0)
        {
            fatherAnimalName = null;
        }
        final String assessmentTemplateName = String.valueOf(importAnimalCSVLine[CSV_COLUMN_ASSESSMENT_TEMPLATE_NAME]);
        final Boolean isAlive = UploadUtils.convertToBoolean(importAnimalCSVLine[CSV_COLUMN_ASSESSMENT_IS_ALIVE]); // TODO?

        final Long assessmentTemplateId = getAssessmentTemplate(assessmentTemplateName);
        final Long animalId = getAnimal(animalNumber);
        final Long damAnimalId = getAnimal(damAnimalName);
        final Long fatherAnimalId = getAnimal(fatherAnimalName);
        final Long speciesId = getSpecies(speciesName);
        final Long sourceId = getSource(sourceName);

        ImportAnimal importAnimal = new ImportAnimal();
        importAnimal.setLineNumber(lineNumber);
        importAnimal.setAnimalNumber(animalNumber);
        importAnimal.setDateBirth(dateOfBirth);
        importAnimal.setSex(sex);
        importAnimal.setSpeciesName(speciesName);
        importAnimal.setSourceName(sourceName);
        importAnimal.setDamAnimalName(damAnimalName);
        importAnimal.setFatherAnimalName(fatherAnimalName);
        importAnimal.setAssessmentTemplateName(assessmentTemplateName);
        importAnimal.setIsalive(isAlive);
        importAnimal.setSexid(getSex(sex));
        importAnimal.setAssessmenttemplateid(assessmentTemplateId);
        importAnimal.setAnimalid(animalId);
        importAnimal.setDamanimalid(damAnimalId);
        importAnimal.setFatheranimalid(fatherAnimalId);
        importAnimal.setSpeciesid(speciesId);
        importAnimal.setSourceid(sourceId);

        return importAnimal;
    }

    public void update(ImportAnimal importAnimal, ImportHeader importHeader)
    {
        if (importAnimal.getDamAnimalName() != null)
        {
            ImportAnimal damImportAnimal = getImportAnimal(importAnimal.getDamAnimalName(), importHeader);
            if (damImportAnimal != null)
            {
                importAnimal.setDamImportanimalid(damImportAnimal.getImportanimalid());
            }
        }

        if (importAnimal.getFatherAnimalName() != null)
        {
            ImportAnimal fatherImportAnimal = getImportAnimal(importAnimal.getFatherAnimalName(), importHeader);
            if (fatherImportAnimal != null)
            {
                importAnimal.setFatherImportanimalid(fatherImportAnimal.getImportanimalid());
            }
        }

    }

    private final AssessmentTemplate getAssessmentTemplate(Long assessmentTemplateId)
    {
        // TODO tests?
        try
        {
            return assessmentTemplateController.getAssessmentTemplateByIdNonApiMethod(assessmentTemplateId);
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getAssessmentTemplate(final String assessmentName)
    {
        try
        {
            return assessmentTemplateController.getAssessmentTemplateByNameNonApiMethod(assessmentName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getAnimal(final String animalNumber)
    {
        try
        {
            return animalController.getAnimalNonApiMethod(animalNumber).getId();
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
            return speciesController.getSpeciesByNameNonApiMethod(speciesName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private final Long getSource(final String sourceName)
    {
        try
        {
            return sourceController.getSourceByNameNonApiMethod(sourceName).getId();
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private Long getSex(final String sex)
    {
        Long sexId = null;
        if (Constants.Upload.SEX_FEMALE_CASE_INSENSITIVE.equals(sex.toLowerCase()))
        {
            sexId = sexFemaleId;
        }
        else if (Constants.Upload.SEX_MALE_CASE_INSENSITIVE.equals(sex.toLowerCase()))
        {
            sexId = sexMaleId;
        }
        else
        {
            sexId = null;
        }

        return sexId;
    }

    private final ImportAnimal getImportAnimal(final String animalNumber, final ImportHeader importHeader)
    {
        try
        {
            return importAnimalController.getAnimalByNameNonApiMethod(animalNumber, importHeader.getImportheaderid());
        }
        catch (AWNoSuchEntityException ex)
        {
            return null;
        }
    }

    private void storeSexes()
    {
        sexFemaleId = sexController.getSexFemale().getId();
        sexMaleId = sexController.getSexFemale().getId();
    }

}
