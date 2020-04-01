package uk.gov.phe.erdst.sc.awag.service.page;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.ResponsePager;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ResponsePagerTest
{
    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testPagingEvenPageLimit()
    {
        final Integer expectedPageLimit = 10;
        final Long expectedTotalObjects = 1000L;
        final Long expectedMetaTotalPages = 100L;

        runPagingTest(expectedPageLimit, expectedTotalObjects, expectedMetaTotalPages);
    }

    @Test
    public void testPagingOddPageLimit()
    {
        final Integer expectedPageLimit = 7;
        final Long expectedTotalObjects = 1000L;
        final Long expectedMetaTotalPages = 143L;

        runPagingTest(expectedPageLimit, expectedTotalObjects, expectedMetaTotalPages);
    }

    private void runPagingTest(Integer expectedPageLimit, Long expectedTotalObjects, Long expectedMetaTotalPages)
    {
        int offset = 0;
        int pageNumber = 0;

        for (long i = 0, loopLimit = expectedTotalObjects.longValue(); i < loopLimit; i++, offset++)
        {
            PagingInfo pagingInfo = ResponsePager
                .getPagingInfo(WebApiUtils.getPagingParams(offset, expectedPageLimit), expectedTotalObjects);

            if (offset % expectedPageLimit == 0)
            {
                pageNumber++;
            }

            assertPagingInfo(pagingInfo, expectedMetaTotalPages, pageNumber, expectedTotalObjects);
        }
    }

    @Test
    public void testPagingOffsetSameAsTotalObjects()
    {
        final Long expectedMetaTotalPages = 10L;
        final Integer expectedMetaPage = -1;
        final Long expectedTotalObjects = 100L;

        final Integer offset = 100;
        final Integer limit = 10;
        final Long totalObjects = 100L;

        PagingInfo pagingInfo = ResponsePager.getPagingInfo(WebApiUtils.getPagingParams(offset, limit),
            totalObjects);

        assertPagingInfo(pagingInfo, expectedMetaTotalPages, expectedMetaPage, expectedTotalObjects);
    }

    @Test
    public void testPagingTotalObjectsZero()
    {
        final Long expectedMetaTotalPages = 0L;
        final Integer expectedMetaPage = -1;
        final Long expectedTotalObjects = 0L;

        final Integer offset = 0;
        final Integer limit = 10;
        final Long totalObjects = 0L;

        PagingInfo pagingInfo = ResponsePager.getPagingInfo(WebApiUtils.getPagingParams(offset, limit),
            totalObjects);

        assertPagingInfo(pagingInfo, expectedMetaTotalPages, expectedMetaPage, expectedTotalObjects);
    }

    @Test
    public void testPagingNullOffset()
    {
        // Contollers should guard pager against this use case
    }

    @Test
    public void testPagingNullPageLimit()
    {
        // Contollers should guard pager against this use case
    }

    private void assertPagingInfo(PagingInfo pagingInfo, Long exMetaTotalPages, Integer exMetaPageNo,
        Long exMetatotalObjs)
    {
        Long actualTotalPages = pagingInfo.totalPages;
        Integer actualPageNo = pagingInfo.page;
        Long actualTotalObjs = pagingInfo.total;

        Assert.assertEquals(actualTotalPages.longValue(), exMetaTotalPages.longValue());
        Assert.assertEquals(actualPageNo.longValue(), exMetaPageNo.longValue());
        Assert.assertEquals(actualTotalObjs.longValue(), exMetatotalObjs.longValue());
    }
}
