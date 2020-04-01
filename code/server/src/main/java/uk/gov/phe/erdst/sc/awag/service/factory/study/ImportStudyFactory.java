package uk.gov.phe.erdst.sc.awag.service.factory.study;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudy;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudyStudyGroup;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Stateless
public class ImportStudyFactory
{

    @Inject
    StudyController studyController;

    @Inject
    StudyGroupController studyGroupController; // TODO need?

    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_SPECIES_NAME = 1;

    public ImportStudy create(String[] importSpeciesCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importSpeciesCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String studyNumber = String.valueOf(importSpeciesCSVLineData[CSV_COLUMN_SPECIES_NAME]);

        final Boolean isStudyOpen = UploadUtils.convertToBoolean(importSpeciesCSVLineData[2]);
        final String studyGroupNumbersList = importSpeciesCSVLineData[2];
        final List<String> studyGroupNumbers = Arrays.asList(studyGroupNumbersList.split(","));
        final Long studyId = studyController.getStudyNumberNonApiMethod(studyNumber);

        ImportStudy importStudy = new ImportStudy();
        importStudy.setLineNumber(lineNumber);
        importStudy.setStudynumber(studyNumber);
        importStudy.setStudynumberid(studyId);
        importStudy.setIsstudyopen(isStudyOpen);
        importStudy.setStudystudygroupnumbers(studyGroupNumbersList);
        importStudy.setImportStudyStudyGroups(new HashSet<ImportStudyStudyGroup>());

        for (String studyGroup : studyGroupNumbers)
        {
            final String trimmedStudyGroup = studyGroup.trim();
            final Long studyGroupId = studyController.getStudyGroupNumberNonApiMethod(trimmedStudyGroup);

            // TODO duplicates in the table
            ImportStudyStudyGroup importStudyStudyGroup = new ImportStudyStudyGroup();
            importStudyStudyGroup.setStudystudygroupnumber(trimmedStudyGroup);
            importStudyStudyGroup.setStudystudygroupnumberid(studyGroupId);
            importStudy.addImportStudyStudyGroup(importStudyStudyGroup);
        }

        return importStudy;
    }

}
