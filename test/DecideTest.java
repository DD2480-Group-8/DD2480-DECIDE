import org.junit.Assert;
import org.junit.Test;

public class DecideTest {
    Parameters params;
    Decide decide;

    public void setup1() {
         params = new Parameters(
                1,
                1,
                Math.PI/2 + 0.1,
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
                3,
                new Coordinate[]{new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(1, 3)},
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );
    }

    public void setup2() {
        params = new Parameters(
                1,
                1,
                Math.PI/2,
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
                3,
                new Coordinate[]{new Coordinate(10, 2), new Coordinate(0, 2), new Coordinate(1, 1)},
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
    public void LIC2TestFalse() {
        setup2();
        decide.LIC2();
        Assert.assertFalse(decide.CMV[2]);
    }

    @Test
    public void LIC2TestTrue() {
        setup1();
        decide.LIC2();
        Assert.assertTrue(decide.CMV[2]);
    }

    @Test
    public void checkAngleTest() {
        Coordinate c1 = new Coordinate(1,0);
        Coordinate c2 = new Coordinate(0,0);
        Coordinate c3 = new Coordinate(1,1);
        
        Assert.assertEquals(decide.checkAngle(c1, c2, c3), Math.PI/4);
    }

    @Test
    public void coordSubtractTest() {
        Coordinate c1 = new Coordinate(1,3);
        Coordinate c2 = new Coordinate(5,-4);
        Coordinate res = new Coordinate(-4,7);
        
        Assert.assertEquals(decide.coordSubtract(c1,c2), res);
    }

    
}
