package edu.cedarville.adld.common.translator;

import org.junit.Before;
import org.junit.Test;

import edu.cedarville.adld.common.model.DataPoint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataPointTranslatorTest {

    private DataPointTranslator translator;

    @Before
    public void setUp() throws Exception {
        this.translator = new DataPointTranslator();

    }

    @Test
    public void testThatDataPointTranslatorTranslatesInputsToIntegerPoints() throws Exception {
        String inputA = "14,A0,79,FF";
        String inputB = "00,FF,0F,F0";

        DataPoint pointA = this.translator.translateInputToDataPoint(inputA);
        DataPoint pointB = this.translator.translateInputToDataPoint(inputB);

        assertThat(pointA.leftSensor, is(20));
        assertThat(pointA.frontSensor, is(160));
        assertThat(pointA.rightSensor, is(121));
        assertThat(pointA.sonarSensor, is(255));

        assertThat(pointB.leftSensor, is(0));
        assertThat(pointB.frontSensor, is(255));
        assertThat(pointB.rightSensor, is(15));
        assertThat(pointB.sonarSensor, is(240));
    }
}
