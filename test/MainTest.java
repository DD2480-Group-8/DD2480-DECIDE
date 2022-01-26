import org.junit.Assert;
import org.junit.Test;

public class MainTest {

    @Test
    public void test() {
        Main main = new Main();
        Assert.assertEquals(5, main.simpleAddition(3, 2));
    }
}
