/**
 * Parameters class
 * Custom type used to model the inputs of the Decide problem.
 * The properties of the parameters class are used to calculate the LICs in the Decide class.
 */
public class Parameters {
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
    public double RADIUS2;
    public double AREA2;

    /**
     * @param length1 Length in LIC 0, 7 and 12
     * @param radius1 Radius in LICs 1, 8, 13
     * @param epsilon Deviation from PI in LICs 2, 9
     * @param area1 Area in LICs 3, 10, 14
     * @param q_pts No. of consecutive points in LIC 4
     * @param quads No. of quadrants in LIC 4
     * @param dist Distance in LIC 6
     * @param n_pts No. of consecutive pts . in LIC 6
     * @param k_pts No. of int. pts. in LICs 7, 12
     * @param a_pts No. of int. pts. in LICs 8, 13
     * @param b_pts No. of int. pts. in LICs 8, 13
     * @param c_pts No. of int. pts. in LIC 9
     * @param d_pts No. of int. pts. in LIC 9
     * @param e_pts No. of int. pts. in LICs 10, 14
     * @param f_pts No. of int. pts. in LICs 10, 14
     * @param g_pts No. of int. pts. in LIC 11
     * @param length2 Maximum length in LIC 12
     * @param radius2 Maximum radius in LIC 13
     * @param area2 Maximum area in LIC 14
     */
    public Parameters(
            double length1, double radius1, double epsilon, double area1, int q_pts, int quads,
            double dist, int n_pts, int k_pts, int a_pts, int b_pts, int c_pts, int d_pts, int e_pts,
            int f_pts, int g_pts, double length2, double radius2, double area2
    ) {
        this.LENGTH1 = length1;
        this.RADIUS1 = radius1;
        this.EPSILON = epsilon;
        this.AREA1 = area1;
        this.Q_PTS = q_pts;
        this.QUADS = quads;
        this.DIST = dist;
        this.N_PTS = n_pts;
        this.K_PTS = k_pts;
        this.A_PTS = a_pts;
        this.B_PTS = b_pts;
        this.C_PTS = c_pts;
        this.D_PTS = d_pts;
        this.E_PTS = e_pts;
        this.F_PTS = f_pts;
        this.G_PTS = g_pts;
        this.LENGTH2 = length2;
        this.RADIUS2 = radius2;
        this.AREA2 = area2;
    }
};
