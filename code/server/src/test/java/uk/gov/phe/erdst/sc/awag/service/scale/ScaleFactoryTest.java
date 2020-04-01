package uk.gov.phe.erdst.sc.awag.service.scale;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.ScaleClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ScaleFactoryTest
{
    private static final Long SCALE_ID = 10000L;
    private static final String SCALE_NAME = "1 to 10";
    private static final int SCALE_MIN = 1;
    private static final int SCALE_MAX = 10;

    @Inject
    private ScaleFactory mScaleFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testCreate()
    {
        ScaleClientData clientData = new ScaleClientData(SCALE_ID, SCALE_NAME, SCALE_MIN, SCALE_MAX);
        Scale scale = mScaleFactory.create(clientData);
        Assert.assertEquals(scale.getName(), SCALE_NAME);
        Assert.assertEquals(scale.getMin(), SCALE_MIN);
        Assert.assertEquals(scale.getMax(), SCALE_MAX);
    }

    @Test
    private void testUpdate()
    {
        ScaleClientData clientData = new ScaleClientData(SCALE_ID, SCALE_NAME, SCALE_MIN, SCALE_MAX);
        Scale scale = new Scale();
        mScaleFactory.update(scale, clientData);
        Assert.assertEquals(scale.getId(), SCALE_ID);
        Assert.assertEquals(scale.getName(), SCALE_NAME);
        Assert.assertEquals(scale.getMin(), SCALE_MIN);
        Assert.assertEquals(scale.getMax(), SCALE_MAX);
    }
}
