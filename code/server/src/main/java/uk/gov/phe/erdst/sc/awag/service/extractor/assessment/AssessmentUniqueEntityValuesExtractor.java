package uk.gov.phe.erdst.sc.awag.service.extractor.assessment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.EntitySelect;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDtoCollection;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;

@Stateless
public class AssessmentUniqueEntityValuesExtractor
{
    private static final Logger LOGGER = LogManager.getLogger(AssessmentUniqueEntityValuesExtractor.class.getName());
    private static final int DEFAULT_ARRAY_SIZE = 10;

    public EntitySelectDtoCollection extract(Collection<Assessment> assessments, Class<?> entityClass,
        EntitySelectDtoFactory entitySelectDtoFactory)
    {
        if (assessments.isEmpty())
        {
            return new EntitySelectDtoCollection(0);
        }

        Set<EntitySelect> entities = new HashSet<>();

        final int size = getEntityCollectionSize(assessments);
        EntitySelectDtoCollection extractedValues = new EntitySelectDtoCollection(size);

        if (entityClass.equals(StudyGroup.class))
        {
            // This should only happen after assessment have been filtered by a study
            handleStudyGroupExtraction(entities, assessments);
        }
        else
        {
            for (Assessment assessment : assessments)
            {
                entities.add(getEntity(entityClass, assessment));
            }
        }

        for (EntitySelect entity : entities)
        {
            if (entity == null)
            {
                extractedValues.add(null);
                continue;
            }

            extractedValues.add(entitySelectDtoFactory.createEntitySelectDto(entity));
        }

        return extractedValues;
    }

    private int getEntityCollectionSize(Collection<Assessment> assessments)
    {
        // Attempt to reduce memory footprint since most assessments will have identical values.
        final int noOfAssessments = assessments.size();
        final int size = noOfAssessments > DEFAULT_ARRAY_SIZE ? noOfAssessments / 2 : noOfAssessments;
        return size;
    }

    private void handleStudyGroupExtraction(Set<EntitySelect> entities, Collection<Assessment> assessments)
    {
        Study lastStudy = null;

        for (Assessment assessment : assessments)
        {
            Study study = assessment.getStudy();

            if (study != null)
            {
                if (lastStudy == null)
                {
                    lastStudy = study;
                }

                if (!lastStudy.equals(study))
                {
                    throwAssessmentsNotFilteredByStudyFirstError();
                }

                Set<StudyGroup> groups = study.getStudyGroups();
                if (groups != null)
                {
                    for (StudyGroup group : groups)
                    {
                        entities.add(group);
                    }
                }
            }
            else
            {
                throwAssessmentsNotFilteredByStudyFirstError();
            }
        }
    }

    private void throwAssessmentsNotFilteredByStudyFirstError()
    {
        IllegalArgumentException ex = new IllegalArgumentException(
            "Trying to filter assessments by study group without filtering by study first");
        LOGGER.error(ex);
        throw ex;
    }

    // CS:OFF: ReturnCount
    private EntitySelect getEntity(Class<?> entityClass, Assessment assessment)
    {
        if (entityClass.equals(Study.class))
        {
            return assessment.getStudy();
        }
        else if (entityClass.equals(Animal.class))
        {
            return assessment.getAnimal();
        }
        else if (entityClass.equals(AssessmentReason.class))
        {
            return assessment.getReason();
        }
        else if (entityClass.equals(User.class))
        {
            return assessment.getPerformedBy();
        }
        else
        {
            IllegalArgumentException ex = new IllegalArgumentException(
                "Entity class is not linked to assessment entity");
            LOGGER.error(ex);
            throw ex;
        }
    }
    // CS:ON
}
