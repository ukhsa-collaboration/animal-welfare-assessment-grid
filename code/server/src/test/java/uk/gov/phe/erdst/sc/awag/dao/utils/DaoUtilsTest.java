package uk.gov.phe.erdst.sc.awag.dao.utils;

import java.util.Collection;

import javax.persistence.PersistenceException;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.dao.impl.utils.DaoUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class DaoUtilsTest
{

    private static final String TEST_ENTITY_NAME = "Animal";
    private static final Long TEST_VIOLATING_ENTITY_VALUE = 1L;
    private static final String TEST_EXPECTED_NO_SUCH_ENTITY_MSG = "Could not find entity Animal with id 1.";
    private static final String TEST_UNIQUE_PROPERTY_NAME = "Animal number";
    private static final String TEST_UNIQUE_PROPERTY_VIOLATING_VALUE = "Animal 1";
    // CS:OFF: LineLengthCheck
    private static final String TEST_EXPECTED_UNIQUE_CONSTRAINT_VIOLATION_MSG = "Animal number Animal 1 already exists in database.";
    private static final String TEST_UNIQUE_CONSTRAINT_VIOLATION_MSG = "so and so violates unique constraint of so and so";
    // CS:ON
    private static final String TEST_UPPERCASE_STRING = "ANIMAL 1";
    private static final String TEST_EXPECTED_LOWERCASE_STRING = "animal 1";
    private static final String TEST_EXPECTED_LOWERCASE_LIKE_STRING = "animal 1%";

    @Test
    private void testGetNoSuchEntityMessage()
    {
        Assert.assertEquals(DaoUtils.getNoSuchEntityMessage(TEST_ENTITY_NAME, TEST_VIOLATING_ENTITY_VALUE),
            TEST_EXPECTED_NO_SUCH_ENTITY_MSG);
    }

    @Test
    private void testGetUniqueConstraintViolationMessage()
    {
        Assert.assertEquals(DaoUtils.getUniqueConstraintViolationMessage(TEST_UNIQUE_PROPERTY_NAME,
            TEST_UNIQUE_PROPERTY_VIOLATING_VALUE), TEST_EXPECTED_UNIQUE_CONSTRAINT_VIOLATION_MSG);
    }

    @Test
    private void testIsUniqueConstraintViolationMessage()
    {
        PersistenceException ex = new PersistenceException();
        ex.initCause(new Throwable().initCause(new Throwable(TEST_UNIQUE_CONSTRAINT_VIOLATION_MSG)));

        Assert.assertTrue(DaoUtils.isUniqueConstraintViolation(ex));
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    private void testIsUniqueConstraintViolationMessageNullArg()
    {
        Assert.assertTrue(DaoUtils.isUniqueConstraintViolation(null));
    }

    @Test
    private void testFormatIdsForInClause()
    {
        Long id1 = 1L;
        Long id2 = 2L;
        Collection<Long> idCol = DaoUtils.formatIdsForInClause(id1, id2);
        Assert.assertEquals(idCol.size(), 2L);
    }

    @Test
    private void testGetLowerCase()
    {
        Assert.assertEquals(DaoUtils.getLowerCase(TEST_UPPERCASE_STRING), TEST_EXPECTED_LOWERCASE_STRING);
    }

    @Test
    private void testGetLikeLowerCase()
    {
        Assert.assertEquals(DaoUtils.getLikeLowerCase(TEST_UPPERCASE_STRING), TEST_EXPECTED_LOWERCASE_LIKE_STRING);
    }
}
