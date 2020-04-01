package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterPK;

public interface AssessmentTemplateParameterDao extends CommonDao<AssessmentTemplateParameter>
{
    AssessmentTemplateParameter getAssessmentTemplateParameterById(AssessmentTemplateParameterPK id);
}
