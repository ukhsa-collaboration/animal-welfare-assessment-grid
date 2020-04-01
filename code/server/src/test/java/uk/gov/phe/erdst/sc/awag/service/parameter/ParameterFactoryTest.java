package uk.gov.phe.erdst.sc.awag.service.parameter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.ParameterClientData;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ParameterFactoryTest
{
    private static final Long PARAMETER_ONE_ID = 10000L;
    private static final String PARAMETER_ONE_NAME = "Parameter 1";

    @Inject
    private RequestConverter mRequestConverter;
    @Inject
    private ParameterFactory mParameterFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testParseParameterClientData()
    {
        ParameterClientData clientData = (ParameterClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_PARAMETER_RAW_DATA, ParameterClientData.class);
        Assert.assertEquals(clientData.parameterId, PARAMETER_ONE_ID);
        Assert.assertEquals(clientData.parameterName, PARAMETER_ONE_NAME);
    }

    @Test
    public void testCreateParameter()
    {
        ParameterClientData clientData = new ParameterClientData(PARAMETER_ONE_ID, PARAMETER_ONE_NAME);
        Parameter parameter = mParameterFactory.create(clientData);
        Assert.assertEquals(parameter.getName(), clientData.parameterName);
    }

    @Test
    public void testUpdateParameter()
    {
        ParameterClientData clientData = new ParameterClientData(PARAMETER_ONE_ID, PARAMETER_ONE_NAME);
        Parameter parameter = new Parameter();
        mParameterFactory.update(parameter, clientData);
        Assert.assertEquals(parameter.getId(), clientData.parameterId);
        Assert.assertEquals(parameter.getName(), clientData.parameterName);
    }
}
