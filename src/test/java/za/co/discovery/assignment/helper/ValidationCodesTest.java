package za.co.discovery.assignment.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kapeshi.Kongolo on 2016/04/18.
 */
public class ValidationCodesTest {
    @Test
    public void verifyThatValidationCodesHandlingIsCorrect() throws Exception {
        String code = ValidationCodes.ROUTE_EXISTS.toString();
        int codeId = ValidationCodes.TRAFFIC_EXISTS.getId();
        ValidationCodes mode = ValidationCodes.fromString(code);
        assertEquals(code, mode.toString());
        assertEquals(3, codeId);
    }
}