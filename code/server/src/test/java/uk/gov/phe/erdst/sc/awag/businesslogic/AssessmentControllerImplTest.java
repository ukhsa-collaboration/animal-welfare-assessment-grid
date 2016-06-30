package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.service.logging.LoggedUser;
import uk.gov.phe.erdst.sc.awag.shared.test.AssessmentProvider;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GlassfishTestsHelper;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class AssessmentControllerImplTest
{
    private static final String USER_PRINCIPAL_NAME = "testUser";
    private AssessmentProvider mAssessmentProvider;
    private AssessmentController mAssessmentCtrl;
    private AssessmentDao mAssessmentDao;

    @BeforeClass
    public static void setUpClass()
    {
        GlassfishTestsHelper.preTestSetup();
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        mAssessmentCtrl = (AssessmentController) GlassfishTestsHelper.lookup("AssessmentControllerImpl");

        mAssessmentProvider = (AssessmentProvider) GlassfishTestsHelper.lookup("AssessmentProvider");

        mAssessmentDao = (AssessmentDao) GlassfishTestsHelper.lookupMultiInterface("AssessmentDaoImpl",
            AssessmentDao.class);
    }

    @AfterMethod
    public void tearDownMethod()
    {
        Collection<Assessment> all = mAssessmentDao.getAssessments(null, null);
        for (Assessment a : all)
        {
            mAssessmentDao.deleteAssessment(a);
        }
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
        mAssessmentCtrl.store(clientData, isSubmit, new ResponsePayload(), new LoggedUser(USER_PRINCIPAL_NAME));

        Assert.assertEquals(mAssessmentCtrl.getAssessmentsCount().longValue(), 1);
    }
}
