package uk.gov.phe.erdst.sc.awag.service.source;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.source.SourceDaoImplTest;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SourceClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.source.SourceFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class SourceFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private SourceFactory mSourceFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate()
    {
        SourceClientData clientData = (SourceClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_SOURCE_RAW_DATA, SourceClientData.class);
        Source source = mSourceFactory.create(clientData);

        assertNonIdProperties(source, clientData);
    }

    @Test
    public void testUpdate()
    {
        Source sourceToUpdate = new Source(SourceDaoImplTest.SOURCE_ONE_ID);

        SourceClientData sourceClientData = (SourceClientData) mRequestConverter
            .convert(TestConstants.DUMMY_UPDATE_SPECIES_RAW_DATA, SourceClientData.class);

        mSourceFactory.update(sourceToUpdate, sourceClientData);

        assertNonIdProperties(sourceToUpdate, sourceClientData);
    }

    private void assertNonIdProperties(Source source, SourceClientData clientData)
    {
        Assert.assertEquals(source.getName(), clientData.sourceName);
    }

}
