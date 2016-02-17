package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterFactorDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactorPK;

@Stateless
public class AssessmentTemplateParameterFactorDaoImpl extends CommonDaoImpl<AssessmentTemplateParameterFactor>
    implements AssessmentTemplateParameterFactorDao
{
    public AssessmentTemplateParameterFactorDaoImpl()
    {
        super(null, null, null);
    }

    @Override
    public AssessmentTemplateParameterFactor
        getAssessmentTemplateParameterFactorById(AssessmentTemplateParameterFactorPK id)
    {
        return getEntityManager().find(AssessmentTemplateParameterFactor.class, id);
    }
}
