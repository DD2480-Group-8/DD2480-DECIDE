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

    @Test
    public void test() {
        Parameters params = new Parameters(
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
        Decide decide = new Decide(
                1,
                new Coordinate[15],
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );

        Assert.assertEquals(5, decide.simpleAddition(3, 2));
    }

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
  
      @Test
    public void LIC1TestFalse() {
        setup1();
        decide.POINTS = new Coordinate[] {
                new Coordinate(1, 1),
                new Coordinate(1, 1),
                new Coordinate(1, 0)
        };
        params.RADIUS1 = 1;
        decide.LIC1();
        Assert.assertFalse(decide.CMV[1]);
    }

    @Test
    public void LIC1TestTrue() {
        setup1();
        decide.POINTS = new Coordinate[] {
                new Coordinate(0, 0),
                new Coordinate(0, 10),
                new Coordinate(0, 0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.RADIUS1 = 1;
        decide.LIC1();
        Assert.assertTrue(decide.CMV[1]);


    /**
     * Tests the LIC10 method that it executes correctly and sets CMV[10] to true.
     */
    @Test
    public void LIC10PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(1,1),
                new Coordinate(0,2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;

        // Executes method
        decide.LIC10();

        // Asserts that it sets CMV[10] to true
        Assert.assertTrue(decide.CMV[10]);
    }

    /**
     * Tests the LIC10 method that it executes correctly and sets CMV[10] to false.
     */
    @Test
    public void LIC10NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(1,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;

        // Executes method
        decide.LIC10();

        // Asserts that it sets CMV[10] to false
        Assert.assertFalse(decide.CMV[10]);
    }

    /**
     * Tests the LIC10 method that it executes correctly and sets CMV[10] to false,
     * due to too few coordinates.
     */
    @Test
    public void LIC10InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(1,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;

        // Executes method
        decide.LIC10();

        // Asserts that it sets CMV[10] to false
        Assert.assertFalse(decide.CMV[10]);
    }

    /**
     * Tests the LIC10 method that it executes correctly and sets CMV[10] to false,
     * due to PARAMETERS.E_PTS being < 1
     */
    @Test
    public void LIC10InvalidTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(1,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 0;
        params.F_PTS = 1;
        params.AREA1 = 1;

        // Executes method
        decide.LIC10();

        // Asserts that it sets CMV[10] to false
        Assert.assertFalse(decide.CMV[10]);
    }

    /**
     * Tests the LIC10 method that it executes correctly and sets CMV[10] to false,
     * due to PARAMETERS.E_PTS + PARAMETERS.F_PTS > NUMPOINTS−3
     */
    @Test
    public void LIC10InvalidTest3() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(1,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 3;
        params.F_PTS = 3;
        params.AREA1 = 1;

        // Executes method
        decide.LIC10();

        // Asserts that it sets CMV[10] to false
        Assert.assertFalse(decide.CMV[10]);
    }
}
