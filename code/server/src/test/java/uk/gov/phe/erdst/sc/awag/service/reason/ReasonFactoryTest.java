package uk.gov.phe.erdst.sc.awag.service.reason;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentReasonClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentReasonFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ReasonFactoryTest
{
    private static final Long REASON_ONE_ID = 10000L;
    private static final String REASON_ON_NAME = "Reason 1";
    @Inject
    private RequestConverter mRequestConverter;
    @Inject
    private AssessmentReasonFactory mAssessmentReasonFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testParseAssessmentReasonClientData()
    {
        AssessmentReasonClientData clientData = (AssessmentReasonClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_REASON_RAW_DATA, AssessmentReasonClientData.class);
        Assert.assertEquals(clientData.reasonId, REASON_ONE_ID);
        Assert.assertEquals(clientData.reasonName, REASON_ON_NAME);
    }

    @Test
    public void testCreateAssessmentReason()
    {
        AssessmentReasonClientData clientData = new AssessmentReasonClientData(REASON_ONE_ID, REASON_ON_NAME);
        AssessmentReason reason = mAssessmentReasonFactory.create(clientData);
        Assert.assertEquals(reason.getName(), clientData.reasonName);
    }

    @Test
    public void testUpdateAssessmentReason()
    {
        AssessmentReasonClientData clientData = new AssessmentReasonClientData(REASON_ONE_ID, REASON_ON_NAME);
        AssessmentReason reason = new AssessmentReason();
        mAssessmentReasonFactory.update(reason, clientData);
        Assert.assertEquals(reason.getId(), clientData.reasonId);
        Assert.assertEquals(reason.getName(), clientData.reasonName);
    }
}
