import org.junit.Assert;
import org.junit.Test;

public class DecideTest {

    @Test
    public void test() {
        Decide decide = new Decide();
        Assert.assertEquals(5, decide.simpleAddition(3, 2));
    }
}
