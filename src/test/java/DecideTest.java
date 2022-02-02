import java.beans.Transient;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;


/**
 * DecideTest Class
 * Contains Unit tests and prefix values for all methods inside to class Decide.
 * All methods is tested with positive/negative test cases, as well as some tests for invalid parameters.
 */
public class DecideTest {
    Parameters params;
    Decide decide;

    /**
     * Prefix function setting up necessary models to run tests on.
     */
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

    /**
     * Prefix function setting up necessary models to run tests on.
     */
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

    // Tests start below

    /**
     * Test for FUV function. Will test both positive and negative cases.
     */
    @Test
    public void FUVTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test Positive case
        Arrays.fill(decide.PUM[0], true);
        Arrays.fill(decide.PUV, true);

        // Calculates the FUV
        decide.calculateFUV();
        // Test that FUV[0] is true as it should be
        Assert.assertTrue(decide.FUV[0]);

        // Change certain parameters to test Negative case
        decide.PUM[0][1] = false;

        // Calculates the FUV
        decide.calculateFUV();
        // Test that FUV[0] is false as it should be
        Assert.assertFalse(decide.FUV[0]);
    }
    
    /**
     * A simple test for the PUM function. Creates a Decide instance where the first LIC is true,
     * the second is false, and where only the first two LIC are used. 
     */
    @Test
    public void PUMTest(){
        //Setup where LIC0 is satisfied but LIC1 is not. 
        setup1();
        decide.PARAMETERS.RADIUS1 = 10;
        decide.PARAMETERS.LENGTH1 = 3;
        decide.NUMPOINTS = 3;
        decide.POINTS = new Coordinate[] {
            new Coordinate(1, 0),
            new Coordinate(10, 10),
            new Coordinate(-1, -2)
    }   ;
        
        //Sets all LCM values to NOTUSED.
        for(int i = 0; i < decide.LCM.length; i++){
            for(int j = 0; j < decide.LCM[0].length; j++){
                    decide.LCM[i][j] = Decide.CONNECTORS.NOTUSED;
            }
        }
        //Sets four cases where the LICs are used. 
        decide.LCM[0][0] = Decide.CONNECTORS.ANDD;
        decide.LCM[0][1] = Decide.CONNECTORS.ANDD;
        decide.LCM[1][0] = Decide.CONNECTORS.ORR;
        decide.LCM[1][1] = Decide.CONNECTORS.ORR;

        // Calculate all LICs
        decide.runLIC();

        //Calculates the PUM matrix
        decide.PUM();

        //Checks that the PUM matrix contains the expected values. 
        Assert.assertTrue(decide.PUM[0][0]);
        Assert.assertFalse(decide.PUM[0][1]);
        Assert.assertTrue(decide.PUM[1][0]);
        Assert.assertFalse(decide.PUM[1][1]);
    }

    /**
     * Asserts that a false case for LIC0 is indeed false.
     */
    @Test
    public void LIC0TestFalse() {
        setup1();
        decide.LIC0();
        Assert.assertFalse(decide.CMV[0]);
    }

    /**
     * Asserts that a true case for LIC0 is indeed true.
     */
    @Test
    public void LIC0TestTrue() {
        setup2();
        decide.LIC0();
        Assert.assertTrue(decide.CMV[0]);
    }


    /**
     * Asserts that a false case for LIC1 is false.
     * LIC1 is true if there exists no three points that can be contained within a circle of radius RADIUS1.
     */
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

    /**
     * Asserts that a true case for LIC1 is true.
     * LIC1 is true if there exists no three points that can be contained within a circle of radius RADIUS1.
     */
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

    /**
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

    /**
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

    /**
     * Tests the LIC3 method that it executes correctly and sets CMV[3] to true.
     */
    @Test
    public void LIC3PositiveTest() {
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
    public void LIC3NegativeTest() {
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
    public void LIC3InvalidTest() {
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

    /**
     * Asserts that a true case for LIC4 is true.
     * LIC4 is true if Q_PTS consecutive points are in more than quads different quadrants.
     */
    @Test
    public void LIC4TestTrue() {
        setup1();
        // set up one point in each quadrant, check for more than 3 quadrants, should return true.
        decide.POINTS = new Coordinate[] {
                new Coordinate(1, 1), // 1st
                new Coordinate(-1, 1), // 2nd
                new Coordinate(-1, -1), // 3rd
                new Coordinate(1, -1) // 4th
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.QUADS = 3; // should be between 1 and 3 (inclusive).
        params.Q_PTS = 3; // should be between 2 and numpoints (inclusive).
        decide.LIC4();
        Assert.assertTrue(decide.CMV[4]);
    }

    /**
     * Asserts that a false case for LIC4 is false.
     * LIC4 is true if Q_PTS consecutive points are in more than quads different quadrants.
     */
    @Test
    public void LIC4TestFalse() {
        setup1();
        // set up all points in 1st and 2nd quadrant, check for more than 2 quadrants, should return false.
        decide.POINTS = new Coordinate[] {
                new Coordinate(1, 1), // 1st
                new Coordinate(2, 2), // 1st
                new Coordinate(-1, 1), // 2nd
                new Coordinate(-2, 2) // 2nd
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.QUADS = 2; // should be between 1 and 3 (inclusive).
        params.Q_PTS = 3; // should be between 2 and numpoints (inclusive).
        decide.LIC4();
        Assert.assertFalse(decide.CMV[4]);
    }

    /**
     * Asserts that an invalid case for LIC4 sets CMV[4] to false.
     * LIC4 is true if Q_PTS consecutive points are in more than quads different quadrants.
     * (2 ≤ Q PTS ≤ NUMPOINTS), (1 ≤ QUADS ≤ 3)
     */
    @Test
    public void LIC4TestInvalidInputs() {
        setup1();
        // set up all points in 1st and 2nd quadrant, set quandrants to 0, should return false.
        decide.POINTS = new Coordinate[] {
                new Coordinate(1, 1), // 1st
                new Coordinate(2, 2), // 1st
                new Coordinate(-1, 1), // 2nd
                new Coordinate(-2, 2) // 2nd
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.QUADS = 0; // should be between 1 and 3 (inclusive).
        params.Q_PTS = 3; // should be between 2 and numpoints (inclusive).
        decide.LIC4();
        Assert.assertFalse(decide.CMV[4]);

        // with quads > 3
        params.QUADS = 4; // should be between 1 and 3 (inclusive).
        decide.LIC4();
        Assert.assertFalse(decide.CMV[4]);

        // with Q_PTS < 2
        params.Q_PTS = 1;
        decide.LIC4();
        Assert.assertFalse(decide.CMV[4]);

        // with Q_PTS > numpoints
        params.Q_PTS = decide.NUMPOINTS + 1;
        decide.LIC4();
        Assert.assertFalse(decide.CMV[4]);
    }

    /**
     * Asserts that a false case for LIC5 is false.
     * LIC5 is true if there exists at least one set of two consecutive data points, (X[i],Y[i]) and (X[j],Y[j]), such
     * that X[j] - X[i] < 0. (where i = j-1)
     */
    @Test
    public void LIC5TestFalse() {
        setup1();
        decide.LIC5();
        Assert.assertFalse(decide.CMV[5]);
    }

    /**
     * Asserts that a true case for LIC5 is true.
     * LIC5 is true if there exists at least one set of two consecutive data points, (X[i],Y[i]) and (X[j],Y[j]), such
     * that X[j] - X[i] < 0. (where i = j-1)
     */
    @Test
    public void LIC5TestTrue() {
        setup2();
        decide.LIC5();
        Assert.assertTrue(decide.CMV[5]);

    }

        /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to true.
     */
    @Test
    public void LIC6PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
        new Coordinate(0,1),
        new Coordinate(1,3),
        new Coordinate(2,1),
        new Coordinate(3,1),
        new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 3;
        params.DIST = 1;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertTrue(decide.CMV[6]);
    }

    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to true when first and last points of these N_PTS are identical.
     */
    @Test
    public void LIC6PositiveTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
            new Coordinate(0,1),
            new Coordinate(1,3),
            new Coordinate(0,1),
            new Coordinate(3,1),
            new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 3;
        params.DIST = 2;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertTrue(decide.CMV[6]);
    }

    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to false.
     */
    @Test
    public void LIC6NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(2,1),
                new Coordinate(3,1),
                new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 3;
        params.DIST = 2;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertFalse(decide.CMV[6]);
    }

    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to false due to NUMPOINTS<3.
     */
    @Test
    public void LIC6NegativeTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
            new Coordinate(0,1),
            new Coordinate(1,1),
            new Coordinate(2,1),
            new Coordinate(3,1),
            new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 2;
        params.DIST = 2;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertFalse(decide.CMV[6]);
    }
    
    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to false due to PARAMETERS.N_PTS<3.
     */
    @Test
    public void LIC6NegativeTest3() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(2,1),
                new Coordinate(3,1),
                new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 2;
        params.DIST = 2;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertFalse(decide.CMV[6]);
    }

    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to false due to NUMPOINTS<3.
     */
    @Test
    public void LIC6NegativeTest4() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(2,3),
                new Coordinate(3,1),
                new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 2;
        params.DIST = 2;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertFalse(decide.CMV[6]);
    }

    /**
     * Tests the LIC6 method that it executes correctly and sets CMV[6] to false due to DIST<0.
     */
    @Test
    public void LIC6NegativeTest5() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,1),
                new Coordinate(2,1),
                new Coordinate(3,1),
                new Coordinate(4,1)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.N_PTS = 3;
        params.DIST = -1;

        // Executes method
        decide.LIC6();

        // Asserts that it sets CMV[6] to true
        Assert.assertFalse(decide.CMV[6]);
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
     * Tests the LIC8 method that it executes correctly and sets CMV[8] to false.
     */
    @Test
    public void LIC8InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(1,1),
                new Coordinate(1,1),
                new Coordinate(2,2),
                new Coordinate(3,1),
                new Coordinate(1,1),
                new Coordinate(1,2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.A_PTS = 1;
        params.B_PTS = 3;
        params.RADIUS1 = 3;

        // Executes method
        decide.LIC8();

        // Asserts that it sets CMV[8] to false
        Assert.assertFalse(decide.CMV[8]);
    }

    /**
     * Tests the LIC8 method that it executes correctly and sets CMV[8] to true.
     */
    @Test
    public void LIC8ValidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(1,5),
                new Coordinate(10,20),
                new Coordinate(15,25),
                new Coordinate(25,30),
                new Coordinate(40,50),
                new Coordinate(50,75),
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.A_PTS = 1;
        params.B_PTS = 2;
        params.RADIUS1 = 3;

        // Executes method
        decide.LIC8();

        // Asserts that it sets CMV[8] to true
        Assert.assertTrue(decide.CMV[8]);
    }


    /**
     * Tests a negative case for the LIC 9 function.
     */
    @Test
    public void LIC9NegativeTest1() {
        setup1();
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.EPSILON = Math.PI*7/8;
        decide.POINTS = new Coordinate[] {
            new Coordinate(1, 1),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(1, 0)
        };
        decide.LIC9();
        Assert.assertFalse(decide.CMV[9]);
    }
    
    /**
     * Tests a negative case for the LIC 9 function where one point is equal to a corresponding angle vertex.
     */
    @Test
    public void LIC9NegativeTest2() {
        setup1();
        decide.NUMPOINTS = 6;
        decide.PARAMETERS.EPSILON = Math.PI*7/8;
        decide.POINTS = new Coordinate[] {
            new Coordinate(1, 1),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(1, 0),
            new Coordinate(-1, 0),
        };
        decide.LIC9();
        Assert.assertFalse(decide.CMV[9]);
    }

    /**
     * Tests a positive case for the LIC 9 function.
     */
    @Test
    public void LIC9PositiveTest1() {
        setup1();
        decide.POINTS = new Coordinate[] {
            new Coordinate(0, 2),
            new Coordinate(0, 0),
            new Coordinate(0, 3),
            new Coordinate(0, 0),
            new Coordinate(1, 3),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 0),
            new Coordinate(0, 0)
        };
        decide.NUMPOINTS = 10;
        decide.PARAMETERS.EPSILON = Math.PI/2 -0.1; 

        decide.LIC9();
        Assert.assertTrue(decide.CMV[9]);
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
        params.AREA1 = 1.01;

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

    /**
     * Asserts that a true case for LIC12 sets CMV[12] to true.
     * LIC12 is true if there exists at least one set of two data points separated by K_PTS, which are a distance greater than LENGTH1 apart
     * and one set of two data points K_PTS apart that is a distance less than LENGTH2 apart.
     */
    @Test
    public void LIC12TestTrue() {
        // Setup of parameters to use
        setup1();

        // Set the points to be on a straight line. Necessary parameters are length1, length2, and K_PTS.
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(1,0),
                new Coordinate(2,0),
                new Coordinate(3,0),
                new Coordinate(4,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.LENGTH1 = 2;
        params.LENGTH2 = 5;
        params.K_PTS = 2;

        // Executes method
        decide.LIC12();
        Assert.assertTrue(decide.CMV[12]);
    }

    /**
     * Asserts that a false case for LIC12 sets CMV[12] to false.
     * LIC12 is true if there exists at least one set of two data points separated by K_PTS, which are a distance greater than LENGTH1 apart
     * and one set of two data points K_PTS apart that is a distance less than LENGTH2 apart.
     */
    @Test
    public void LIC12TestFalse() {
        // Setup of parameters to use
        setup1();

        // Set the points to be on a straight line. Necessary parameters are length1, length2, and K_PTS.
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(1,0),
                new Coordinate(2,0),
                new Coordinate(3,0),
                new Coordinate(4,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.LENGTH1 = 7; // set to 7 since we don't have any distance that long.
        params.LENGTH2 = 5; // set to 5 since we have distances shorter than that.
        params.K_PTS = 1; // 1 pt between them.

        // Executes method
        decide.LIC12();
        Assert.assertFalse(decide.CMV[12]);
    }

    /**
     * Asserts that a case with invalid inputs for LIC12 sets CMV[12] to false.
     * LIC12 is true if there exists at least one set of two data points separated by K_PTS, which are a distance greater than LENGTH1 apart
     * and one set of two data points K_PTS apart that is a distance less than LENGTH2 apart.
     */
    @Test
    public void LIC12InvalidInputs() {
        // Setup of parameters to use
        setup1();

        // Set the points to be on a straight line. Necessary parameters are length1, length2, and K_PTS.
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(1,0),
                new Coordinate(2,0),
                new Coordinate(3,0),
                new Coordinate(4,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.LENGTH1 = 2; // set to 7 since we don't have any distance that long.
        params.LENGTH2 = 5; // set to 5 since we have distances shorter than that.

        params.K_PTS = -1; // Test negative k_pts.
        decide.LIC12();
        Assert.assertFalse(decide.CMV[12]);

        params.K_PTS = 2; // Reset k_pts.
        params.LENGTH1 = -2; // Test negative Length1.
        decide.LIC12();
        Assert.assertFalse(decide.CMV[12]);

        params.LENGTH1 = 2; // reset Length1.
        params.LENGTH2 = -2; // Test negative Length1.
        decide.LIC12();
        Assert.assertFalse(decide.CMV[12]);

        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(1,0),
        }; // Test less than 3 points.
        decide.LIC12();
        Assert.assertFalse(decide.CMV[12]);

    }

    /**
     * Tests the LIC13 method that it executes correctly and sets CMV[13] to true
     */
    @Test
    public void LIC13PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,0),
                new Coordinate(0,-1),
                new Coordinate(-1,0),
                new Coordinate(0,2),
                new Coordinate(2,0),
                new Coordinate(0,-2),
                new Coordinate(-2,0),
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.RADIUS1 = 1;
        params.RADIUS2 = 2;

        // Executes method
        decide.LIC13();

        // Asserts that it sets CMV[13] to true
        Assert.assertTrue(decide.CMV[13]);
    }

    /**
     * Tests the LIC13 method that it executes correctly and sets CMV[13] to false
     * because all sets of points can be contained within RADIUS1
     */
    @Test
    public void LIC13NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,0),
                new Coordinate(0,-1),
                new Coordinate(-1,0),
                new Coordinate(0,2),
                new Coordinate(2,0),
                new Coordinate(0,-2),
                new Coordinate(-2,0),
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.RADIUS1 = 3;
        params.RADIUS2 = 2;

        // Executes method
        decide.LIC13();

        // Asserts that it sets CMV[13] to true
        Assert.assertFalse(decide.CMV[13]);
    }

    /**
     * Tests the LIC13 method that it executes correctly and sets CMV[13] to false
     * because all sets of points cannot be contained within RADIUS2
     */
    @Test
    public void LIC13NegativeTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,0),
                new Coordinate(0,-1),
                new Coordinate(-1,0),
                new Coordinate(0,2),
                new Coordinate(2,0),
                new Coordinate(0,-2),
                new Coordinate(-2,0),
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.RADIUS1 = 1;
        params.RADIUS2 = 0.9;

        // Executes method
        decide.LIC13();

        // Asserts that it sets CMV[13] to true
        Assert.assertFalse(decide.CMV[13]);
    }

    /**
     * Tests the LIC13 method that it executes correctly and sets CMV[13] to false
     * because NUMPOINTS < 5
     */
    @Test
    public void LIC13InvalidTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test positive case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,1),
                new Coordinate(1,0),
                new Coordinate(0,-1),
                new Coordinate(-1,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.RADIUS1 = 1;
        params.RADIUS2 = 2;

        // Executes method
        decide.LIC13();

        // Asserts that it sets CMV[13] to true
        Assert.assertFalse(decide.CMV[13]);
    }

     /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to true.
     */
    @Test
    public void LIC14PositiveTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(0,2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;
        params.AREA2 = 4;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to true
        Assert.assertTrue(decide.CMV[14]);
    }

    /**

     * Tests the LIC14 method that it executes correctly and sets CMV[14] to true.
     */
    @Test
    public void LIC14PositiveTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
            new Coordinate(0,0),
            new Coordinate(-1,-1),
            new Coordinate(2,0),
            new Coordinate(3,3),
            new Coordinate(0,2),
            new Coordinate(1,-1),
            new Coordinate(4,-2),
            new Coordinate(2,1),
            new Coordinate(4,2),
            new Coordinate(0,-2)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 2;
        params.F_PTS = 2;
        params.AREA1 = 1;
        params.AREA2 = 4;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to true
        Assert.assertTrue(decide.CMV[14]);
    }

    /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to false.
     * Only one possible triangle and it has area = 2.
     */
    @Test
    public void LIC14NegativeTest() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
            new Coordinate(0,0),
            new Coordinate(-1,-1),
            new Coordinate(2,0),
            new Coordinate(3,3),
            new Coordinate(0,2)
    };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;
        params.AREA2 = 1.99;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to false
        Assert.assertFalse(decide.CMV[14]);
    }

    /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to false.
     * Two possible triangles, but both with an area greater than AREA2.
     */
    @Test
    public void LIC14NegativeTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
            new Coordinate(0,0),
            new Coordinate(-1,-1),
            new Coordinate(2,0),
            new Coordinate(3,3),
            new Coordinate(0,2),
            new Coordinate(-2,4)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;
        params.AREA2 = 1.99;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to false
        Assert.assertFalse(decide.CMV[14]);
    }


    /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to false,
     * due to too few points.
     */
    @Test
    public void LIC14InvalidTest() {// Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0)
        };
        decide.NUMPOINTS = decide.POINTS.length; // too few points
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;
        params.AREA2 = 2;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to false
        Assert.assertFalse(decide.CMV[14]);
    }

    /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to false,
     * due to E_PTS + F_PTS > NUMPOINTS-3.
     */
    @Test
    public void LIC14InvalidTest2() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(0,2),
                new Coordinate(-2,4)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 4; // Too large
        params.F_PTS = 2; // Too large
        params.AREA1 = 1;
        params.AREA2 = 2;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to false
        Assert.assertFalse(decide.CMV[14]);
    }

    /**
     * Tests the LIC14 method that it executes correctly and sets CMV[14] to false,
     * due to AREA2 < 0.
     */
    @Test
    public void LIC14InvalidTest3() {
        // Setup of parameters to use
        setup1();
        // Change certain parameters to test valid case
        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(-1,-1),
                new Coordinate(2,0),
                new Coordinate(3,3),
                new Coordinate(0,2),
                new Coordinate(-2,4)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        params.E_PTS = 1;
        params.F_PTS = 1;
        params.AREA1 = 1;
        params.AREA2 = -1;

        // Executes method
        decide.LIC14();

        // Asserts that it sets CMV[14] to false
        Assert.assertFalse(decide.CMV[14]);
    }

    /**
      * Tests that the function calsulateDistance retruns the correct values
      */
    @Test
    public void calculateDistanceTest(){
        setup1();

        //Sets up three Coordinates used in testing
        Coordinate i = new Coordinate(0,0);
        Coordinate j = new Coordinate(0,2);
        Coordinate k = new Coordinate(1,1);

        //checks that the distances between i and the other points are correct 
        Assert.assertEquals(2.0, decide.calculateDistance(i,j), 0.01);
        Assert.assertEquals(Math.sqrt(2.0), decide.calculateDistance(i,k), 0.01);
    }

       /**
      * Tests that the function checkcircle is correctly executed and returns correct.
      */
    @Test
    public void checkCircleTest(){
        setup1();
        Coordinate i = new Coordinate(0,0);
        Coordinate j = new Coordinate(2,0);
        Coordinate k = new Coordinate(1,1.1);
            
        Assert.assertTrue(decide.checkCircle(i,j,k,1));  
    }

    /**
     * Tests that help function calculateArea is correctly executed and returns correct.
     */
    @Test
    public void calculateAreaTest() {
        setup1();

        Coordinate i = new Coordinate(0,0);
        Coordinate j = new Coordinate(0,2);
        Coordinate k = new Coordinate(2,0);
        // Area of these three is 2

        Assert.assertEquals(2.0, decide.calculateArea(i,j,k), 0.01);
    }

    /**
     * Tests that help function threePointsAreaComparison executes correctly and returns true
     */
    @Test
    public void threePointsAreaComparisonTest() {
        setup1();

        decide.POINTS = new Coordinate[]{
                new Coordinate(0,0),
                new Coordinate(0,2),
                new Coordinate(2,0)
        };
        decide.NUMPOINTS = decide.POINTS.length;
        // Area of these three is 2

        // Should return true as 2 >= 1 and 2 <= 2
        Assert.assertTrue(decide.threePointsAreaComparison(1, true, 0,0));
        Assert.assertTrue(decide.threePointsAreaComparison(2, false, 0,0));

        // Should return false as 2 >= 3 and 2 <= 1 is not true
        Assert.assertFalse(decide.threePointsAreaComparison(3, true, 0,0));
        Assert.assertFalse(decide.threePointsAreaComparison(1, false, 0,0));
    }

    /**
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

    /**
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
}
