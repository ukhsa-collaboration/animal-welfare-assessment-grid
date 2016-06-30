package uk.gov.phe.erdst.sc.awag.service.extractor.assessment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

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
public class AssessmentUniqueValuesExtractor
{
    @Inject
    private AssessmentUniqueEntityValuesExtractor mUniqueEntityValuesExtractor;

    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    public UniqueEntitySelectCollectionDto extract(Collection<Assessment> assessments, boolean isStudySet)
    {
        Map<Class<? extends EntitySelect>, EntitySelectDtoCollection> dtosToEntityMap = new HashMap<>();
        dtosToEntityMap.put(Study.class, null);
        dtosToEntityMap.put(StudyGroup.class, null);
        dtosToEntityMap.put(Animal.class, null);
        dtosToEntityMap.put(AssessmentReason.class, null);
        dtosToEntityMap.put(User.class, null);

        if (!isStudySet)
        {
            dtosToEntityMap.remove(StudyGroup.class);
        }

        for (Class<? extends EntitySelect> entityClass : dtosToEntityMap.keySet())
        {
            EntitySelectDtoCollection dtos = mUniqueEntityValuesExtractor.extract(assessments, entityClass,
                mEntitySelectDtoFactory);

            dtosToEntityMap.put(entityClass, dtos);
        }

        return new UniqueEntitySelectCollectionDto(dtosToEntityMap.get(Study.class),
            dtosToEntityMap.get(StudyGroup.class), dtosToEntityMap.get(Animal.class),
            dtosToEntityMap.get(AssessmentReason.class), dtosToEntityMap.get(User.class));
    }

    public DatesBorderValues extractDatesBorderValues(Collection<Assessment> assessments)
    {
        String startDate = null;
        String endDate = null;

        for (Assessment assessment : assessments)
        {
            String assessmentDate = assessment.getDate();

            if (startDate == null)
            {
                startDate = assessmentDate;
            }
            if (endDate == null)
            {
                endDate = assessmentDate;
            }

            if (assessmentDate.compareTo(startDate) < 0)
            {
                startDate = assessmentDate;
            }

            if (assessmentDate.compareTo(endDate) > 0)
            {
                endDate = assessmentDate;
            }
        }

        return new DatesBorderValues(startDate, endDate);
    }

    // CS:OFF: VisibilityModifier|HiddenField
    public static class DatesBorderValues
    {
        public final String dateOfFirstAssessment;
        public final String dateOfLastAssessment;

        public DatesBorderValues(String dateOfFirstAssessment, String dateOfLastAssessment)
        {
            this.dateOfFirstAssessment = dateOfFirstAssessment;
            this.dateOfLastAssessment = dateOfLastAssessment;
        }
    }
}
