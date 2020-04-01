package uk.gov.phe.erdst.sc.awag.service.template;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplateParameterFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.service.factory.template.AssessmentTemplateDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.template.AssessmentTemplateDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentTemplateDtoFactoryTest
{

    private final Long TEMPLATE_ID = 10000L;
    private final String TEMPLATE_NAME = "Template 1";

    private final Long SCALE_ID = 10000L;
    private final String SCALE_NAME = "Scale 1";

    private final Long FACTOR_ID = 10000L;
    private final String FACTOR_NAME = "Factor 1";
    private final String FACTOR_DESCRIPTION = null;

    private final Long PARAMETER_ID = 10000L;
    private final String PARAMETER_NAME = "Parameter 1";

    private final Long CLOCKWISE_DISPLAY_ORDER_NOT_SET = 0L;

    @Inject
    private AssessmentTemplateDtoFactory mTemplateDtoFactory;

    public AssessmentTemplateDtoFactoryTest()
    {
    }

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testCreateAssessmentTemplateDto()
    {
        Scale scale = new Scale();
        scale.setId(SCALE_ID);
        scale.setName(SCALE_NAME);

        AssessmentTemplate template = new AssessmentTemplate();
        template.setId(TEMPLATE_ID);
        template.setName(TEMPLATE_NAME);
        template.setScale(scale);

        Factor factor = new Factor();
        factor.setId(FACTOR_ID);
        factor.setName(FACTOR_NAME);

        Parameter parameter = new Parameter();
        parameter.setId(PARAMETER_ID);
        parameter.setName(PARAMETER_NAME);

        List<AssessmentTemplateParameterFactor> templateParameterFactors = new ArrayList<AssessmentTemplateParameterFactor>(
            1);
        AssessmentTemplateParameterFactor templateParameterFactor = new AssessmentTemplateParameterFactor();
        templateParameterFactor.setAssessmentTemplate(template);
        templateParameterFactor.setParameter(parameter);
        templateParameterFactor.setFactor(factor);
        templateParameterFactors.add(templateParameterFactor);
        template.setAssessmentTemplateParameterFactors(templateParameterFactors);

        AssessmentTemplateDto templateDto = mTemplateDtoFactory.create(template);

        Assert.assertNotNull(templateDto);

        Assert.assertEquals(templateDto.templateId, TEMPLATE_ID);
        Assert.assertEquals(templateDto.templateName, TEMPLATE_NAME);
        Assert.assertEquals(templateDto.templateScale.scaleId, SCALE_ID);
        Assert.assertEquals(templateDto.templateScale.scaleName, SCALE_NAME);
        Assert.assertEquals(templateDto.templateParameters.toArray(new ParameterDto[] {})[0].parameterId, PARAMETER_ID);
        Assert.assertEquals(templateDto.templateParameters.toArray(new ParameterDto[] {})[0].parameterName,
            PARAMETER_NAME);
        Assert.assertEquals(
            templateDto.templateParameters.toArray(new ParameterDto[] {})[0].parameterDisplayOrderNumber,
            CLOCKWISE_DISPLAY_ORDER_NOT_SET); // TODO
        FactorDto factorDto = templateDto.templateParameters.toArray(new ParameterDto[] {})[0].parameterFactors
            .toArray(new FactorDto[] {})[0];

        Assert.assertEquals(factorDto.factorId, FACTOR_ID);
        Assert.assertEquals(factorDto.factorName, FACTOR_NAME);
        Assert.assertEquals(factorDto.factorName, FACTOR_DESCRIPTION);
    }
}
