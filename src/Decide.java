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
//        System.out.println("Hello World");
    }

    public void LIC0() {
        for (int i = 1; i < NUMPOINTS; i++) {
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i-1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i-1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }

    public int simpleAddition(int x, int y) {
        return x + y;
    }

        /*
    *   LIC 3 is:
    *   There exists at least one set of three consecutive data points that are the vertices of a triangle
    *   with area greater than AREA1. (0 ≤ AREA1)
    *
    */
    public void LIC3(){
        if (NUMPOINTS < 3) {
            CMV[3] = false;
            return;
        }

        for (int n = 0 ; n < NUMPOINTS-2 ; n++) {
            // Extract the coordinates of the three consecutive data points
            double x1 = POINTS[n].XPOS;
            double y1 = POINTS[n].YPOS;
            double x2 = POINTS[n+1].XPOS;
            double y2 = POINTS[n+1].YPOS;
            double x3 = POINTS[n+2].XPOS;
            double y3 = POINTS[n+2].YPOS;

            // Calculate the area of the three consecutive data points
            double area = Math.abs((x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2))/2);

            if (area > PARAMETERS.AREA1) {
                CMV[3] = true;
                return;
            }
        }
        CMV[3] = false;
    }

}   
