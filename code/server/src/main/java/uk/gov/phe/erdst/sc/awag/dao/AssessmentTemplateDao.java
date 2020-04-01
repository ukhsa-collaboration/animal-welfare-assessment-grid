package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public interface AssessmentTemplateDao extends UploadCommonDao<AssessmentTemplate>
{
    AssessmentTemplate getAssessmentTemplateByAnimalId(Long animalId) throws AWNoSuchEntityException;

    Long getCountAssessmentTemplates();

    String[] getAssessmentTemplateSuffixUploadHeaders(Long assessmentTemplateId) throws AWNoSuchEntityException;
}
