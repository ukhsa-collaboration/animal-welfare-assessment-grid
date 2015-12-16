package uk.gov.phe.erdst.sc.awag.bussinesslogic;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.businesslogic.AssessmentController;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentProvider;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentControllerImplTest
{
    private AssessmentProvider mAssessmentProvider;
    private AssessmentController mAssessmentCtrl;

    @BeforeClass
    public static void setUpClass()
    {
        // GlassfishTestsHelper.eclipsePropertiesTest();
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentCtrl = (AssessmentController) GlassfishTestsHelper.lookup("AssessmentControllerImpl");

        mAssessmentProvider = (AssessmentProvider) GlassfishTestsHelper.lookup("AssessmentProvider");
    }

    @AfterClass
    public static void tearDownClass()
    {
        GlassfishTestsHelper.onTestFinished();
    }

    @Test
    public void testStoreAndGetAssessments() throws Exception
    {
        Assert.assertEquals(mAssessmentCtrl.getAssessmentsCount().longValue(), 0);

        AssessmentClientData clientData = mAssessmentProvider.createClientData();
        boolean isSubmit = false;
        mAssessmentCtrl.store(clientData, isSubmit, new ResponsePayload());

        Assert.assertEquals(mAssessmentCtrl.getAssessmentsCount().longValue(), 1);
    }
}
