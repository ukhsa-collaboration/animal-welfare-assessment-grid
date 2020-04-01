package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentReasonController;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentReason;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

@Stateless
public class ImportAssessmentReasonFactory
{
    @Inject
    private AssessmentReasonController assessmentReasonController;

    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_ASSESSMENT_REASON_NAME = 1;

    public ImportAssessmentReason create(String[] importAssessmentReasonCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importAssessmentReasonCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String assessmentReasonName = String
            .valueOf(importAssessmentReasonCSVLineData[CSV_COLUMN_ASSESSMENT_REASON_NAME]);

        ImportAssessmentReason importAssessmentReason = new ImportAssessmentReason();
        importAssessmentReason.setLineNumber(lineNumber);
        importAssessmentReason.setAssessmentReasonName(assessmentReasonName);
        try
        {
            Long assessmentReasonId = assessmentReasonController.getReasonNonApiMethod(assessmentReasonName).getId();
            importAssessmentReason.setAssessmentreasonid(assessmentReasonId);
        }
        catch (AWNoSuchEntityException ex)
        {
            importAssessmentReason.setAssessmentreasonid(null);
        }

        return importAssessmentReason;
    }

}
