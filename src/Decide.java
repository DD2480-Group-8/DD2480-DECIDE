public class Decide {

    enum CONNECTORS {NOTUSED, ORR, ANDD};
    int NUMPOINTS;
    Coordinate[] POINTS;
    CONNECTORS[][] LCM;
    boolean[][] PUM;
    boolean[] CMV;
    boolean[] PUV;
    boolean[] FUV;
    Parameters PARAMETERS;
        
    public class Coordinate{
        double XPOS;
        double YPOS;
    };
    class Parameters{
        public double LENGTH1;
        public double RADIUS1;
        public double EPSILON;
        public double AREA1;
        public int Q_PTS;
        public int QUADS;
        public double DIST;
        public int N_PTS;
        public int K_PTS;
        public int A_PTS;
        public int B_PTS;
        public int C_PTS;
        public int D_PTS;
        public int E_PTS;
        public int F_PTS;
        public int G_PTS;
        public double LENGTH2;
        public double RADUIS2;
        public double AREA2;
    };

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

    public int simpleAddition(int x, int y) {
        return x + y;
    }

}   
