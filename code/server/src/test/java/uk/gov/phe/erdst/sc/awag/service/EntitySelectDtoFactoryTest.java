package uk.gov.phe.erdst.sc.awag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.EntitySelect;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.service.factory.EntitySelectDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class EntitySelectDtoFactoryTest
{
    @Inject
    private EntitySelectDtoFactory mEntitySelectDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCollectionConversion()
    {
        final int entitiesLimit = 5;
        Map<Long, TestEntity> entities = new HashMap<>();

        for (int i = 0; i < entitiesLimit; i++)
        {
            Long key = new Long(i);
            entities.put(key, new TestEntity(key, String.valueOf(i)));
        }

        List<EntitySelectDto> dtos = mEntitySelectDtoFactory.createEntitySelectDtos(entities.values());

        for (EntitySelectDto dto : dtos)
        {
            TestEntity entity = entities.get(dto.id);

            if (entity == null)
            {
                Assert.fail();
            }

            Assert.assertTrue(isConvertedOk(entity, dto));
        }

        entities.clear();
        dtos = mEntitySelectDtoFactory.createEntitySelectDtos(entities.values());

        Assert.assertTrue(dtos.isEmpty());
    }

    @Test
    public void testSingleConversion()
    {
        final Long id = 1L;
        TestEntity entity = new TestEntity(id, String.valueOf(id));

        EntitySelectDto dto = mEntitySelectDtoFactory.createEntitySelectDto(entity);

        Assert.assertTrue(isConvertedOk(entity, dto));
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testNullValueCollection()
    {
        mEntitySelectDtoFactory.createEntitySelectDtos(null);
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testNullValueSingle()
    {
        mEntitySelectDtoFactory.createEntitySelectDto(null);
    }

    @Test
    public void testNameChange()
    {
        final Long id = 1L;
        TestEntity entity = new TestEntity(id, String.valueOf(id));

        String newName = "newName";
        EntitySelectDto dto = mEntitySelectDtoFactory.createEntitySelectDto(entity, newName);
        TestEntity expectedEntity = new TestEntity(id, newName);

        Assert.assertTrue(isConvertedOk(expectedEntity, dto));
    }

    private boolean isConvertedOk(TestEntity testEntity, EntitySelectDto dto)
    {
        return testEntity.mId == dto.id && testEntity.getEntitySelectName().equals(dto.entityName);
    }

    private static class TestEntity implements EntitySelect
    {
        private Long mId;
        private String mName;

        public TestEntity(Long id, String name)
        {
            mId = id;
            mName = name;
        }

        @Override
        public Object getEntitySelectId()
        {
            return mId;
        }

        @Override
        public String getEntitySelectName()
        {
            return mName;
        }
    }
}
