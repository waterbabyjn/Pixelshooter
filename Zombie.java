public class Zombie extends FIGUR {
    private double speed;
    private double ozx = 0;
    private double ozy = 0;

    private boolean[] dir = {true, true, true, true};
                            /* U     D     L     R */

    public Zombie(double x, double y) {
        super("shoot/zombie.png");
        setzeMittelpunkt(x, y);
        macheDynamisch();
        setzeEbene(0);
        skaliere(0.8);
    }

    public void move(double px, double py, boolean quickfast) {
        if (quickfast == true) {
            speed = 0.1;
        } else {
            speed = 0.05;
        }

        double zx = nenneMx();
        double zy = nenneMy();
        double angle = Math.toDegrees(Math.atan2(py - zy, px - zx));
        // this.setzeDrehwinkel(angle);

        double dx = speed * Math.cos(Math.toRadians(angle));
        double dy = speed * Math.sin(Math.toRadians(angle));

        double mdx = dx;
        double mdy = dy;

        if (dir[3] == false && dir[2] == false) {
            mdx = 0;
        } else if ((dx > 0 && dir[3] == false) || (dx < 0 && dir[2] == false)) { mdx  = dx * -1;};
        if (dir[1] == false && dir[0] == false) {
            mdy = 0;
        } else if ((dy > 0 && dir[0] == false) || (dy < 0 && dir[1] == false)) { mdy  = dy * -1;};

        verschiebenUm(mdx, mdy);

        if (zx != ozx && zy != ozy) {
            ozx = zx;
            ozy = zy;
        } else {
            if (zx == ozx) {
                if (dx > 0) { dir[3] = false;}; // rmove R
                if (dx < 0) { dir[2] = false;}; // rmove L
            } else if (zx != ozx) {
                dir[3] = true; 
                dir[2] = true;
            }
            if (zy == ozy) {
                if (dy > 0) { dir[0] = false;}; // rmove U
                if (dy < 0) { dir[1] = false;}; // rmove D
            } else if (zy != ozy) {
                dir[1] = true; 
                dir[0] = true;
            }
        }

    }

    public boolean stuck(double a, double b) {
        return Math.abs(a - b) < 0.1;
    }
}
