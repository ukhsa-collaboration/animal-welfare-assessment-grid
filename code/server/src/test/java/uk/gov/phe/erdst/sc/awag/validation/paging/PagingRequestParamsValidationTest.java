package uk.gov.phe.erdst.sc.awag.validation.paging;

import java.util.Arrays;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.client.PagingGetRequestParams;
import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTest;
import uk.gov.phe.erdst.sc.awag.validation.utils.ValidationTestHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class PagingRequestParamsValidationTest
{
    private static final String LIMIT_FIELD_NAME = "limit";
    private static final String OFFSET_FIELD_NAME = "offset";
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    public void testPagingRequestParamsValidation()
    {
        final int expectedOffsetValidationErrors = 1;

        List<Integer> invalidOffsetValues = Arrays.asList(-1);

        ValidationTest offsetTest = new ValidationTest(expectedOffsetValidationErrors,
            ServletUtils.getPagingRequestParams(null, null), OFFSET_FIELD_NAME, invalidOffsetValues, mValidator);

        final int expectedLimitValidationErrors = 2;
        List<Integer> invalidLimitValues = Arrays.asList(-1, 0);

        ValidationTest limitTest = new ValidationTest(expectedLimitValidationErrors,
            ServletUtils.getPagingRequestParams(null, null), LIMIT_FIELD_NAME, invalidLimitValues, mValidator);

        ValidationTestHelper.run(Arrays.asList(offsetTest, limitTest), PagingGetRequestParams.class);
    }

    @Test
    public void testValidPagingRequestParams()
    {
        final int expectedViolations = 0;

        // CS:OFF: MagicNumber
        List<Integer> validOffsetValues = Arrays.asList(null, 0, 10);
        // CS:ON

        ValidationTest offsetTest = new ValidationTest(expectedViolations, ServletUtils.getPagingRequestParams(null,
            null), OFFSET_FIELD_NAME, validOffsetValues, mValidator);

        List<Integer> invalidLimitValues = Arrays.asList(null, 1);

        ValidationTest limitTest = new ValidationTest(expectedViolations, ServletUtils.getPagingRequestParams(null,
            null), LIMIT_FIELD_NAME, invalidLimitValues, mValidator);

        ValidationTestHelper.run(Arrays.asList(offsetTest, limitTest), PagingGetRequestParams.class);
    }

}
