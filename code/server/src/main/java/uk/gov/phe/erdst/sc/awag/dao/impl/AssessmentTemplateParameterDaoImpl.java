package uk.gov.phe.erdst.sc.awag.dao.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateParameterDao;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterPK;

@Stateless
public class AssessmentTemplateParameterDaoImpl extends CommonDaoImpl<AssessmentTemplateParameter>
    implements AssessmentTemplateParameterDao
{
    public AssessmentTemplateParameterDaoImpl()
    {
        super(null, null, null);
    }

    @Override
    public AssessmentTemplateParameter getAssessmentTemplateParameterById(AssessmentTemplateParameterPK id)
    {
        return getEntityManager().find(AssessmentTemplateParameter.class, id);
    }
}
