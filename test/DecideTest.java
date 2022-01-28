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
}
