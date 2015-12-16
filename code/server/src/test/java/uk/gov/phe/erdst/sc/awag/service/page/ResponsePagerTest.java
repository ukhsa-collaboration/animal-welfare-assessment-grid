package uk.gov.phe.erdst.sc.awag.service.page;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayloadConstants;
import uk.gov.phe.erdst.sc.awag.exceptions.AWInvalidParameterException;
import uk.gov.phe.erdst.sc.awag.service.page.ResponsePager;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ResponsePagerTest
{
    @Inject
    private ResponsePager mRrequestPager;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testPagingEvenPageLimit() throws AWInvalidParameterException
    {
        final Integer expectedPageLimit = 10;
        final Long expectedTotalObjects = 1000L;
        final Long expectedMetaTotalPages = 100L;

        runPagingTest(expectedPageLimit, expectedTotalObjects, expectedMetaTotalPages);
    }

    @Test
    public void testPagingOddPageLimit() throws AWInvalidParameterException
    {
        final Integer expectedPageLimit = 7;
        final Long expectedTotalObjects = 1000L;
        final Long expectedMetaTotalPages = 143L;

        runPagingTest(expectedPageLimit, expectedTotalObjects, expectedMetaTotalPages);
    }

    private void runPagingTest(Integer expectedPageLimit, Long expectedTotalObjects, Long expectedMetaTotalPages)
        throws AWInvalidParameterException
    {
        int offset = 0;
        int pageNumber = 0;
        ResponsePayload responsePayload = null;

        for (long i = 0, loopLimit = expectedTotalObjects.longValue(); i < loopLimit; i++, offset++)
        {
            responsePayload = new ResponsePayload();
            mRrequestPager.setPagingTotalsMetadata(offset, expectedPageLimit, expectedTotalObjects, responsePayload);

            if (offset % expectedPageLimit == 0)
            {
                pageNumber++;
            }

            assertPagingTotalsMetadata(responsePayload, expectedMetaTotalPages, pageNumber, expectedTotalObjects);
        }
    }

    @Test
    public void testPagingOffsetSameAsTotalObjects() throws AWInvalidParameterException
    {
        final Long expectedMetaTotalPages = 10L;
        final Integer expectedMetaPage = -1;
        final Long expectedTotalObjects = 100L;

        ResponsePayload responsePayload = new ResponsePayload();
        final Integer offset = 100;
        final Integer limit = 10;
        final Long totalObject = 100L;

        mRrequestPager.setPagingTotalsMetadata(offset, limit, totalObject, responsePayload);

        assertPagingTotalsMetadata(responsePayload, expectedMetaTotalPages, expectedMetaPage, expectedTotalObjects);
    }

    @Test
    public void testPagingTotalObjectsZero() throws AWInvalidParameterException
    {
        final Long expectedMetaTotalPages = 0L;
        final Integer expectedMetaPage = -1;
        final Long expectedTotalObjects = 0L;

        ResponsePayload responsePayload = new ResponsePayload();
        final Integer offset = 0;
        final Integer limit = 10;
        final Long totalObject = 0L;

        mRrequestPager.setPagingTotalsMetadata(offset, limit, totalObject, responsePayload);

        assertPagingTotalsMetadata(responsePayload, expectedMetaTotalPages, expectedMetaPage, expectedTotalObjects);
    }

    @Test
    public void testPagingNullOffset() throws AWInvalidParameterException
    {
        ResponsePayload responsePayload = new ResponsePayload();
        final Integer offset = null;
        final Integer limit = 10;
        final Long totalObjects = null;

        mRrequestPager.setPagingTotalsMetadata(offset, limit, totalObjects, responsePayload);
        Assert.assertTrue(responsePayload.getMetadata().isEmpty());
    }

    @Test
    public void testPagingNullPageLimit() throws AWInvalidParameterException
    {
        ResponsePayload responsePayload = new ResponsePayload();
        final Integer offset = 10;
        final Integer limit = null;
        final Long totalObjects = null;

        mRrequestPager.setPagingTotalsMetadata(offset, limit, totalObjects, responsePayload);
        Assert.assertTrue(responsePayload.getMetadata().isEmpty());
    }

    private void assertPagingTotalsMetadata(ResponsePayload responsePayload, Long exMetaTotalPages,
        Integer exMetaPageNo, Long exMetatotalObjs)
    {
        Map<String, Object> metadata = responsePayload.getMetadata();

        Long actualTotalPages = (Long) metadata.get(ResponsePayloadConstants.META_TOTAL_PAGES);
        Integer actualPageNo = (Integer) metadata.get(ResponsePayloadConstants.META_PAGE);
        Long actualTotalObjs = (Long) metadata.get(ResponsePayloadConstants.META_TOTAL_OBJS);

        Assert.assertEquals(actualTotalPages.longValue(), exMetaTotalPages.longValue());
        Assert.assertEquals(actualPageNo.longValue(), exMetaPageNo.longValue());
        Assert.assertEquals(actualTotalObjs.longValue(), exMetatotalObjs.longValue());
    }
}
