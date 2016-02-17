package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.DaoErrorMessageProvider;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoConstants;
import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.utils.DataRetrievalUtils;

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
}
