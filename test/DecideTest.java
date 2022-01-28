import org.junit.Assert;
import org.junit.Test;

public class DecideTest {
    Parameters params;
    Decide decide;

    public void setup1() {
         params = new Parameters(
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1
        );
         decide = new Decide(
                2,
                new Coordinate[]{new Coordinate(0, 2), new Coordinate(0, 3)},
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );
    }

    public void setup2() {
        params = new Parameters(
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1
        );
        decide = new Decide(
                2,
                new Coordinate[]{new Coordinate(10, 2), new Coordinate(0, 2)},
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );
    }

    // Tests starts below

    @Test
    public void LIC0TestFalse() {
        setup1();
        decide.LIC0();
        Assert.assertFalse(decide.CMV[0]);
    }

    @Test
    public void LIC0TestTrue() {
        setup2();
        decide.LIC0();
        Assert.assertTrue(decide.CMV[0]);
    }

    /**
     * Tests the LIC3 method that it executes correctly and sets CMV[3] to true.
     */
    @Test
    public void LEC3PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{new Coordinate(0,0), new Coordinate(2,0), new Coordinate(0,2)};
        decide.NUMPOINTS = decide.POINTS.length;
        // Executes method
        decide.LIC3();

        // Asserts that it sets CMV[3] to true
        Assert.assertTrue(decide.CMV[3]);
    }

    /**
     * Tests the LIC3 method that it executes correctly and sets CMV[3] to false,
     * due to the area being too small.
     */
    @Test
    public void LEC3NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{new Coordinate(0,0), new Coordinate(1,0), new Coordinate(0,1)};
        decide.NUMPOINTS = decide.POINTS.length;
        // Executes method
        decide.LIC3();

        // Asserts that it sets CMV[3] to false
        Assert.assertFalse(decide.CMV[3]);
    }

    /**
     * Tests the LIC3 method that it executes correctly and sets CMV[3] to true,
     * due to too few coordinates.
     */
    @Test
    public void LEC3InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{new Coordinate(0,0), new Coordinate(2,0)};
        decide.NUMPOINTS = decide.POINTS.length;
        // Executes method
        decide.LIC3();

        // Asserts that it sets CMV[3] to false
        Assert.assertFalse(decide.CMV[3]);
    }
}
