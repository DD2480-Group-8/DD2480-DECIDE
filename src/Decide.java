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
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i - 1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i - 1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }
    public void LIC5() {
        for (int i = 1; i < NUMPOINTS; i++) {
            double diff = POINTS[i].XPOS - POINTS[i-1].XPOS;
            if (diff < 0) {
                CMV[5] = true;
            }
        }
    }

    /**
     * Implementation of LIC1: There exists at least one set of three consecutive data points that cannot all be contained
     * within or on a circle of radius RADIUS1.
     * Finds the center between three points, and checks if there is any point that is a distance greater than RADIUS1
     * from the center.
     */
    public void LIC1() {
        for (int i = 2; i < NUMPOINTS; i++) {
            // Find the centroid.
            Coordinate centroid = new Coordinate(
                    (POINTS[i].XPOS + POINTS[i-1].XPOS + POINTS[i-2].XPOS) / 3,
                    (POINTS[i].YPOS + POINTS[i-1].YPOS + POINTS[i-2].YPOS) / 3
            );
            // Check if any of the points have a distance to the centroid larger than the radius.
            if (
                    (Math.sqrt(Math.pow(POINTS[i].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                    || (Math.sqrt(Math.pow(POINTS[i-1].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i-1].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
                    || (Math.sqrt(Math.pow(POINTS[i-2].XPOS - centroid.XPOS, 2) + Math.pow(POINTS[i-2].YPOS - centroid.YPOS, 2)) > PARAMETERS.RADIUS1)
            ) {
                CMV[1] = true;
                break; // only need one set of points to fulfill this, no need to continue the loop.
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
