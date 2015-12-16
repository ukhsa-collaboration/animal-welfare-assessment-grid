package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public interface AssessmentTemplateDao extends CommonDao<AssessmentTemplate>
{
    AssessmentTemplate getAssessmentTemplateByAnimalId(Long animalId) throws AWNoSuchEntityException;
}
