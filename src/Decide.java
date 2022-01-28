public class Decide {

    enum CONNECTORS {
        NOTUSED,
        ORR,
        ANDD
    };

    int NUMPOINTS;
    Coordinate[] POINTS;
    CONNECTORS[][] LCM;
    boolean[][] PUM;
    boolean[] CMV = new boolean[15];
    boolean[] PUV;
    boolean[] FUV;
    Parameters PARAMETERS;


    Decide(int numpts, Coordinate[] pts, Parameters params, CONNECTORS[][] LCM, boolean[] PUV){
        this.NUMPOINTS = numpts;
        this.POINTS = pts;
        this.PARAMETERS = params;
        this.LCM = LCM;
        this.PUV = PUV;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
    }

    public void LIC0() {
        for (int i = 1; i < POINTS.length; i++) {
            double dist = Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i-1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i-1].XPOS, 2));
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i-1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i-1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }

    public int simpleAddition(int x, int y) {
        return x + y;
    }

}   
