package uk.gov.phe.erdst.sc.awag.validation.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.validation.Payload;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationAnnotationUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ValidationAnnotationUtilsTest
{
    private static final String EXPECTED_MESSAGE = "Mock message";

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMemberValues() throws Exception
    {
        Annotation annotation;
        annotation = TestClass.class.getDeclaredField("mField").getAnnotation(TestAnnotation.class);
        Map memberDefaults = ValidationAnnotationUtils.getAnnotationMemberDefaults(annotation);

        Assert.assertEquals(memberDefaults.get("message"), EXPECTED_MESSAGE);
    }

    @Target({ElementType.FIELD})
    @Retention(value = RetentionPolicy.RUNTIME)
    private @interface TestAnnotation
    {
        String message() default EXPECTED_MESSAGE;

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    private static class TestClass
    {
        @TestAnnotation(message = "Custom message")
        private String mField;
    }
}
