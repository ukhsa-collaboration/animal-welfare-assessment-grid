package uk.gov.phe.erdst.sc.awag.service.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDtoCollection;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueEntityValuesExtractor;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

public abstract class AssessmentUniqueEntitiesExtractorTestTemplate
{
    @Inject
    protected EntitySelectDtoFactory mEntitySelectDtoFactory;

    @Inject
    protected AssessmentUniqueEntityValuesExtractor mExtractor;

    private Class<?> mTestEntityClass;

    public void setUp() throws Exception
    {
        mTestEntityClass = getTestEntityClass();

        GuiceHelper.injectTestDependencies(this);
    }

    protected abstract Class<?> getTestEntityClass();

    public void testWithIdentical()
    {
        final Long id = 1L;
        final int entities = 5;

        Collection<Assessment> data = new ArrayList<>();

        for (int i = 0; i < entities; i++)
        {
            data.add(createTestAssessment(id, String.valueOf(id)));
        }

        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, mTestEntityClass, mEntitySelectDtoFactory);

        EntitySelectDto value = extractedValues.values.toArray(new EntitySelectDto[0])[0];

        Assert.assertEquals(extractedValues.values.size(), 1);
        Assert.assertEquals(value.id, id);
    }

    public void testWithDifferent()
    {
        final int entities = 5;

        Collection<Assessment> data = new ArrayList<>();
        Set<Long> ids = new HashSet<>();

        for (int i = 0; i < entities; i++)
        {
            Long id = new Long(i);
            ids.add(id);
            data.add(createTestAssessment(id, String.valueOf(i)));
        }

        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, mTestEntityClass, mEntitySelectDtoFactory);

        Assert.assertEquals(extractedValues.values.size(), entities);

        for (EntitySelectDto dto : extractedValues.values)
        {
            Assert.assertTrue(ids.contains(dto.id));
        }
    }

    public void testWithNull()
    {
        final int entities = 5;

        Collection<Assessment> data = new ArrayList<>();
        Set<Long> ids = new HashSet<>();

        for (int i = 0; i < entities; i++)
        {
            Long id = new Long(i);
            ids.add(id);
            data.add(createTestAssessment(id, String.valueOf(i)));
        }

        Assessment assessment = createTestAssessment(1L, String.valueOf(1L));
        nullifyTestEntity(assessment);
        ids.add(null);

        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, mTestEntityClass, mEntitySelectDtoFactory);

        Assert.assertEquals(extractedValues.values.size(), entities);

        boolean hasFoundNull = false;
        for (EntitySelectDto dto : extractedValues.values)
        {
            if (dto == null)
            {
                if (hasFoundNull)
                {
                    Assert.fail();
                }
                else
                {
                    hasFoundNull = true;
                    continue;
                }
            }
            else
            {
                Assert.assertTrue(ids.contains(dto.id));
            }
        }
    }

    protected abstract Assessment createTestAssessment(Long entityId, String entityNumber);

    protected abstract void nullifyTestEntity(Assessment assessment);
}
