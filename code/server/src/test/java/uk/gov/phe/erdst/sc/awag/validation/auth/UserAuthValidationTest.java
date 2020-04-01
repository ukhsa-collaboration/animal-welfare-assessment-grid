package uk.gov.phe.erdst.sc.awag.validation.auth;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.utils.UserAuthTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.WebSecurityUtils;
import uk.gov.phe.erdst.sc.awag.webapi.request.UserAuthClientData;

@Test(groups = {TestConstants.TESTNG_CONTAINER_TESTS_GROUP})
public class UserAuthValidationTest
{
    private Validator mValidator;

    @BeforeClass
    private void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        mValidator = validatorFactory.getValidator();
    }

    @Test
    private void testPasswordNotEqualRetypedPassword()
    {
        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData("admin", "adminadmin", "notadminadmin",
            WebSecurityUtils.RolesAllowed.AW_ADMIN);
        Set<ConstraintViolation<UserAuthClientData>> testConstraintViolations = mValidator.validate(clientData);
        Assert.assertEquals(testConstraintViolations.size(), 1);
    }

    /*    @Test
    private void testInvalidGroupSupplied()
    {
        UserAuthClientData clientData = UserAuthTestUtils.getUserAuthClientData("admin", "adminadmin", "adminadmin",
            "bogus group name");
        Set<ConstraintViolation<UserAuthClientData>> testConstraintViolations = mValidator.validate(clientData);
        Assert.assertEquals(testConstraintViolations.size(), 1);
    }*/
}
