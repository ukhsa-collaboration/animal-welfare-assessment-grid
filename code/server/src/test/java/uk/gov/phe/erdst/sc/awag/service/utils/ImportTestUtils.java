package uk.gov.phe.erdst.sc.awag.service.utils;

import java.util.HashSet;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessment;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentParameterFactorScore;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;

public final class ImportTestUtils
{
    public static ImportHeader createDummyImportHeader(String userName, String activityLogAction)
    {
        ImportHeader importHeader = new ImportHeader();
        importHeader.setDateImport(new java.util.Date());
        importHeader.setUserName(userName);
        importHeader.setActivityLogAction(activityLogAction);
        importHeader.setImportAnimals(new HashSet<>());
        // TODO specifics?
        importHeader.setImportAssessments(new HashSet<>());
        return importHeader;
    }

    public static ImportAnimal createDummyAnimal(String animalNumber)
    {
        ImportAnimal importAnimal = new ImportAnimal();
        importAnimal.setAnimalNumber(animalNumber);
        return importAnimal;
    }

    public static ImportAssessment createDummyAssessmentForAnimal(Long importAssessmentTemplateId, String animalName)
    {
        ImportAssessmentParameterFactorScore importScores = new ImportAssessmentParameterFactorScore();
        importScores.setFactorScores("1,2,3,4");
        importScores.setParameterComments("Dummy Comments");

        ImportAssessment importAssessment = new ImportAssessment();
        importAssessment.setImportassessmenttemplateid(importAssessmentTemplateId);
        importAssessment.setAnimalHousingName("Housing New");
        importAssessment.setAssessmentreasonName("Reason New");
        importAssessment.setImportAssessmentParameterFactorScores(new HashSet<>());
        importAssessment.addImportAssessmentParameterFactorScore(importScores);

        return importAssessment;
    }
}
