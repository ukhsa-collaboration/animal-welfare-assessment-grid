package uk.gov.phe.erdst.sc.awag.dao.impl;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterFactorDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;

public class AssessmentTemplateParameterFactorDaoImpl extends CommonDaoImpl<AssessmentTemplateParameterFactor>
    implements AssessmentTemplateParameterFactorDao
{
    @Override
    public AssessmentTemplateParameterFactor
        getAssessmentTemplateParameterFactorById(AssessmentTemplateParameterFactorPK id)
    {
        return mEntityManager.find(AssessmentTemplateParameterFactor.class, id);
    }
}
