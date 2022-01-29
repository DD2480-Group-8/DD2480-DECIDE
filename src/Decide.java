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
        for (int i = 1; i < NUMPOINTS; i++) {
            if (Math.sqrt(Math.pow(POINTS[i].XPOS - POINTS[i-1].XPOS, 2) + Math.pow(POINTS[i].YPOS - POINTS[i-1].YPOS, 2)) > PARAMETERS.LENGTH1) {
                CMV[0] = true;
            }
        }
    }
    
    public void LIC2(){
        for(int i = 0; i < NUMPOINTS-2; i++){
            if(checkAngle(POINTS[i], POINTS[i+1], POINTS[i+2])){
                CMV[2] = true;
                return;
            }
        }
        CMV[2] = false;
    }

    private boolean checkAngle(Coordinate i, Coordinate j, Coordinate k){
        i = coordSubtract(i,j);
        k = coordSubtract(k,j);
        if((i.XPOS == 0 && i.YPOS == 0) || (k.XPOS == 0 && k.YPOS == 0)){
            return false;
        }
        double dotProduct = i.XPOS * k.XPOS + i.YPOS * k.YPOS;
        double lenProduct = Math.sqrt(Math.pow(i.XPOS,2) + Math.pow(i.YPOS,2)) * Math.sqrt(Math.pow(k.XPOS,2) + Math.pow(k.YPOS,2));
        double angle = Math.acos(dotProduct/lenProduct);
        if( angle > Math.PI + PARAMETERS.EPSILON || angle < Math.PI - PARAMETERS.EPSILON){
            return true;
        }
        return false;
    }

    private Coordinate coordSubtract(Coordinate i, Coordinate j){
        Coordinate res = new Coordinate();
        res.XPOS = i.XPOS-j.XPOS;
        res.YPOS = i.YPOS - j.YPOS;
        return(res);
    }

    public int simpleAddition(int x, int y) {
        return x + y;
    }

}   
