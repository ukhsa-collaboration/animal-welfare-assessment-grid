package uk.gov.phe.erdst.sc.awag.service.factor;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.FactorClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class FactorFactoryTest
{
    private static final Long FACTOR_ID = -1L;
    private static final String FACTOR_NAME = "Factor 1";
    private static final Long FACTOR_ID_NEW = 10001L;
    private static final String FACTOR_NAME_NEW = "Factor 1 new";
    @Inject
    private RequestConverter mRequestConverter;
    @Inject
    private FactorFactory mFactorFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testParseFactorClientData()
    {
        FactorClientData clientData = (FactorClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_FACTOR_RAW_DATA, FactorClientData.class);
        Assert.assertEquals(clientData.factorId, FACTOR_ID);
        Assert.assertEquals(clientData.factorName, FACTOR_NAME);
    }

    @Test
    public void testCreate()
    {
        FactorClientData clientData = (FactorClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_FACTOR_RAW_DATA, FactorClientData.class);
        Factor factor = mFactorFactory.create(clientData);
        Assert.assertEquals(factor.getName(), clientData.factorName);
    }

    @Test
    public void testUpdate()
    {
        Factor factorToUpdate = new Factor();
        FactorClientData clientData = (FactorClientData) mRequestConverter
            .convert(TestConstants.DUMMY_UPDATE_FACTOR_RAW_DATA, FactorClientData.class);
        mFactorFactory.update(factorToUpdate, clientData);
        Assert.assertEquals(factorToUpdate.getId(), FACTOR_ID_NEW);
        Assert.assertEquals(factorToUpdate.getName(), FACTOR_NAME_NEW);
    }

}
