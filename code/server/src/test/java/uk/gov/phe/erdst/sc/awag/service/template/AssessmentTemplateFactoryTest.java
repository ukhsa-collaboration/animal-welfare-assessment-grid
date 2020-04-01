package uk.gov.phe.erdst.sc.awag.service.template;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentTemplateClientData;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentTemplateFactoryTest
{

    private final Long NEW_TEMPLATE_ID = Constants.UNASSIGNED_ID;
    private final String TEMPLATE_NAME = "Template 1";

    @Inject
    private AssessmentTemplateFactory mAssessmentTemplateFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testAssessmentTemplateFactoryCreate()
    {
        AssessmentTemplateClientData templateClientData = new AssessmentTemplateClientData();
        templateClientData.templateId = NEW_TEMPLATE_ID;
        templateClientData.templateName = TEMPLATE_NAME;

        AssessmentTemplate assessmentTemplate = mAssessmentTemplateFactory.create(templateClientData);

        Assert.assertNotNull(assessmentTemplate);
        Assert.assertNotEquals(assessmentTemplate.getId(), NEW_TEMPLATE_ID);
        Assert.assertEquals(assessmentTemplate.getName(), TEMPLATE_NAME);
    }
}
