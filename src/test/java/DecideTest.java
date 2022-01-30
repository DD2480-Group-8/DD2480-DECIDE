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

    // Write tests below

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
    }

    /*
    * Tests a negative case for the LIC2 function.
    */
    @Test
    public void LIC2TestFalse() {
        setup1();
        decide.NUMPOINTS = 3;
        decide.PARAMETERS.EPSILON = Math.PI*7/8;
        decide.POINTS = new Coordinate[] {
            new Coordinate(1, 1),
            new Coordinate(0, 0),
            new Coordinate(1, 0)
        };
        decide.LIC2();
        Assert.assertFalse(decide.CMV[2]);
    }

    /*
    * Tests a positive case for the LIC2 function.
    */
    @Test
    public void LIC2TestTrue() {
        setup1();
        decide.POINTS = new Coordinate[] {
            new Coordinate(0, 2),
            new Coordinate(0, 3),
            new Coordinate(1, 3)
        };
        decide.NUMPOINTS = 3;
        decide.PARAMETERS.EPSILON = Math.PI/2 -0.1; 

        decide.LIC2();
        Assert.assertTrue(decide.CMV[2]);
    }

    /*
    * Tests the Coordinate angle checking helper function.
    */
    @Test
    public void checkAngleTest() {
        setup1();
        Coordinate c1 = new Coordinate(1,0);
        Coordinate c2 = new Coordinate(0,0);
        Coordinate c3 = new Coordinate(1,1);
        
        Assert.assertEquals(decide.checkAngle(c1, c2, c3), Math.PI/4, 0.001);
    }

    /*
    * Tests the Coordinate subtraction helper function.
    */
    @Test
    public void coordSubtractTest() {
        setup1();
        Coordinate c1 = new Coordinate(1,3);
        Coordinate c2 = new Coordinate(5,-4);

        Coordinate c3 = new Coordinate(-4,7);
        Coordinate res = decide.coordSubtract(c1,c2);
        
        Assert.assertEquals(c3.XPOS, res.XPOS, 0.001);
        Assert.assertEquals(c3.YPOS, res.YPOS, 0.001);
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

    /*
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
  
    @Test
    public void LIC5TestFalse() {
        setup1();
        decide.LIC5();
        Assert.assertFalse(decide.CMV[5]);
    }

    @Test
    public void LIC5TestTrue() {
        setup2();
        decide.LIC5();
        Assert.assertTrue(decide.CMV[5]);

    }


    /**
     * Tests the LIC7 method that it executes correctly and sets CMV[7] to true.
     */
    @Test
    public void LIC7PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(1,1),
                new Coordinate(0,0),
                new Coordinate(2,2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.K_PTS = 1;
        params.LENGTH1 = 1;

        // Executes method
        decide.LIC7();

        // Asserts that it sets CMV[7] to true
        Assert.assertTrue(decide.CMV[7]);
    }

    /**
     * Tests the LIC7 method that it executes correctly and sets CMV[7] to false,
     * since the distance is smaller than PARAMETER.LENGTH1
     */
    @Test
    public void LIC7NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(1,1),
                new Coordinate(0,0),
                new Coordinate(2,2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.K_PTS = 1;
        params.LENGTH1 = 2;

        // Executes method
        decide.LIC7();

        // Asserts that it sets CMV[7] to false
        Assert.assertFalse(decide.CMV[7]);
    }

    /**
     * Tests the LIC7 method that it executes correctly and sets CMV[7] to false.
     */
    @Test
    public void LIC7InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(1,1),
                new Coordinate(0,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.K_PTS = 1;
        params.LENGTH1 = 1;

        // Executes method
        decide.LIC7();

        // Asserts that it sets CMV[7] to false
        Assert.assertFalse(decide.CMV[7]);
    }

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

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to true
     */
    @Test
    public void LIC11PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(-1,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.G_PTS = 1;

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to true
        Assert.assertTrue(decide.CMV[11]);
    }

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to true
     */
    @Test
    public void LIC11PositiveTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(0,1),
                new Coordinate(0,2),
                new Coordinate(0,3),
                new Coordinate(1,0),
                new Coordinate(1,1),
                new Coordinate(1,2),
                new Coordinate(1,3),
                new Coordinate(2,0),
                new Coordinate(2,1),
                new Coordinate(2,2),
                new Coordinate(2,3),
                new Coordinate(-1,0),
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.G_PTS = 8;

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to true
        Assert.assertTrue(decide.CMV[11]);
    }

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to false
     */
    @Test
    public void LIC11NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test negative case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(1,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.G_PTS = 1;

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to false
        Assert.assertFalse(decide.CMV[11]);
    }

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to false,
     * due to PARAMETER.G_PTS being less than 1.
     */
    @Test
    public void LIC11InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(-1,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.G_PTS = 0; // Must be > 0

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to false
        Assert.assertFalse(decide.CMV[11]);
    }

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to false,
     * due to PARAMETER.G_PTS being larger than NUMPOINTS-2.
     */
    @Test
    public void LIC11InvalidTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(-1,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.G_PTS = 3; // Must be ≤ NUMPOINTS-2

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to false
        Assert.assertFalse(decide.CMV[11]);
    }

    /**
     * Tests the LIC11 method that it executes correctly and sets CMV[11] to false,
     * due to NUMPOINTS being < 3.
     */
    @Test
    public void LIC11InvalidTest3() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test invalid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1)
        };
        decide.NUMPOINTS = decide.POINTS.length; // Can't be < 3
        params.G_PTS = 1; // This one fails as well, but it is not tested

        // Executes method
        decide.LIC11();

        // Asserts that it sets CMV[11] to false
        Assert.assertFalse(decide.CMV[11]);
    }
}
