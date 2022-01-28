import org.junit.Assert;
import org.junit.Test;

public class DecideTest {

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
    public void LEC3PositiveTest() {
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

        Coordinate[] pts = new Coordinate[]{new Coordinate(0,0), new Coordinate(2,0), new Coordinate(0,2)};
        
        Decide decide = new Decide(
                pts.length,
                pts,
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );

        Assert.assertTrue(decide.LIC3());
    }

    @Test
    public void LEC3NegativeTest() {
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

        Coordinate[] pts = new Coordinate[]{new Coordinate(0,0), new Coordinate(1,0), new Coordinate(0,1)};

        Decide decide = new Decide(
                pts.length,
                pts,
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );

        Assert.assertFalse(decide.LIC3());

    }

    @Test
    public void LEC3InvalidTest() {
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

        Coordinate[] pts = new Coordinate[]{new Coordinate(0,0), new Coordinate(2,0)};

        Decide decide = new Decide(
                pts.length,
                pts,
                params,
                new Decide.CONNECTORS[15][15],
                new boolean[15]
        );

        Assert.assertFalse(decide.LIC3());

    }

}
