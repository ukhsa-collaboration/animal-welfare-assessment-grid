package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;

public interface AssessmentTemplateParameterFactorDao extends CommonDao<AssessmentTemplateParameterFactor>
{
    AssessmentTemplateParameterFactor getAssessmentTemplateParameterFactorById(AssessmentTemplateParameterFactorPK id);
}
