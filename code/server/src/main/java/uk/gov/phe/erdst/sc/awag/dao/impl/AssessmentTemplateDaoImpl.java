package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;
import uk.gov.phe.erdst.sc.awag.utils.DataRetrievalUtils;
import uk.gov.phe.erdst.sc.awag.utils.UploadUtils;

@Stateless
public class AssessmentTemplateDaoImpl extends CommonDaoImpl<AssessmentTemplate> implements AssessmentTemplateDao
{
    private static final Logger LOGGER = LogManager.getLogger(AssessmentTemplateDaoImpl.class.getName());

    public AssessmentTemplateDaoImpl()
    {
        super("mId", "mName", new DaoErrorMessageProvider() {
            @Override
            public String getNonUniqueEntityErrorMessage(Object entity)
            {
                return DaoUtils.getUniqueConstraintViolationMessage(DaoConstants.PROP_ASSESSMENT_TEMPLATE_NAME,
                    ((AssessmentTemplate) entity).getName());
            }

            @Override
            public String getNoSuchEntityMessage(Object id)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_ASSESSMENT_TEMPLATE_ID, id);
            }

            @Override
            public String getNoSuchEntityMessage(String nameValue)
            {
                return DaoUtils.getNoSuchEntityMessage(DaoConstants.PROP_ASSESSMENT_TEMPLATE_NAME, nameValue);
            }
        });
    }

    @Override
    public AssessmentTemplate getAssessmentTemplateByAnimalId(Long animalId) throws AWNoSuchEntityException
    {
        List<AssessmentTemplate> result = getEntityManager()
            .createNamedQuery(Animal.Q_FIND_ANIMAL_ASSESSMENT_TEMPLATE, AssessmentTemplate.class)
            .setParameter("id", animalId).getResultList();

        if (result.size() == 0)
        {
            String errMsg = String.format("Could not find AssessmentTemplate using animal id %d", animalId);
            LOGGER.error(errMsg);
            throw new AWNoSuchEntityException(errMsg);
        }

        return DataRetrievalUtils.getEntityFromListResult(result);
    }

    @Override
    public Long getCountAssessmentTemplates()
    {
        return getEntityManager().createNamedQuery(AssessmentTemplate.Q_FIND_COUNT_ALL, Long.class).getResultList()
            .get(0);
    }

    @Override
    public String[] getAssessmentTemplateSuffixUploadHeaders(Long assessmentTemplateId) throws AWNoSuchEntityException
    {
        final int columnCount = getEntityById(assessmentTemplateId).getAssessmentTemplateParameters().size();
        final String[] headerColumns = UploadUtils.retrieveAssessmentUploadSuffixHeader(columnCount);
        return headerColumns;
    }

    @Override
    public void upload(Collection<AssessmentTemplate> assessmentTemplates) throws AWNonUniqueException
    {
        AssessmentTemplate lastAssessmentTemplate = null;
        try
        {
            for (AssessmentTemplate assessmentTemplate : assessmentTemplates)
            {
                lastAssessmentTemplate = assessmentTemplate;
                getEntityManager().persist(assessmentTemplate);
            }
            getEntityManager().flush();
        }
        catch (PersistenceException ex)
        {
            if (DaoUtils.isUniqueConstraintViolation(ex))
            {
                String errMsg = getMessageProvider().getNonUniqueEntityErrorMessage(lastAssessmentTemplate);
                LOGGER.error(errMsg);
                throw new AWNonUniqueException(errMsg);
            }
            else
            {
                LOGGER.error(ex.getMessage());
                throw ex;
            }
        }
    }

}
