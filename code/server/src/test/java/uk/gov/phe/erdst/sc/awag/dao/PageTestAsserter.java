package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;

import org.testng.Assert;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

public final class PageTestAsserter
{
    private PageTestAsserter()
    {
    }

    @SuppressWarnings("rawtypes")
    public static void assertPagedNoOffset(Collection allEntities, Collection pagedEntities)
    {
        Assert.assertEquals(pagedEntities.toArray()[0], allEntities.toArray()[0]);
    }

    @SuppressWarnings("rawtypes")
    public static void assertPagedNoLimit(Collection allEntities, Collection pagedEntities)
    {
        Assert.assertEquals(pagedEntities.size(), allEntities.size() - TestConstants.TEST_OFFSET);
    }

    @SuppressWarnings({"rawtypes"})
    public static void assertPagedValidOffsetLimit(Collection allEntities, Collection pagedEntities)
    {
        int count = 0;
        for (Object entity : pagedEntities)
        {
            int finalPosition = TestConstants.TEST_OFFSET + count;
            Assert.assertEquals(entity, allEntities.toArray()[finalPosition]);
            count++;
        }
    }
}
