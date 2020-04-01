package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentReasonDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

@Stateless
public class AssessmentReasonDaoImpl extends CommonDaoImpl<AssessmentReason> implements AssessmentReasonDao
{

    public AssessmentReasonDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_REASON_NAME,
                    ((AssessmentReason) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_REASON_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_REASON_NAME, nameValue);
            }
        });
    }

    @Override
    public void upload(Collection<AssessmentReason> assessmentReasons) throws AWNonUniqueException
    {
        AssessmentReason lastAssessmentReason = null;
        try
        {

            for (AssessmentReason assessmentReason : assessmentReasons)
            {
                lastAssessmentReason = assessmentReason;
                getEntityManager().persist(assessmentReason);
            }
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getMessageProvider().getNonUniqueEntityErrorMessage(lastAssessmentReason);
                getLogger().error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                getLogger().error(ex.getMessage());
                throw ex;
            }
        }

    }

}
