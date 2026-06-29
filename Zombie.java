public class Zombie extends FIGUR {
    private double speed;
    private static final double SIGHT_RANGE = 4.0;
    private static final double MOVE_TOLERANCE = 0.001;

    private double wanderX = 0.05;
    private double wanderY = 0.0;
    private int stuckFrames = 0;
    private int pauseFrames = 0;

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
        double dxToPlayer = px - zx;
        double dyToPlayer = py - zy;
        double distanceSquared = dxToPlayer * dxToPlayer + dyToPlayer * dyToPlayer;
        double sightRangeSquared = SIGHT_RANGE * SIGHT_RANGE;

        double moveX = 0;
        double moveY = 0;

        if (pauseFrames > 0) {
            pauseFrames--;
            verschiebenUm(0, 0);
            return;
        }

        if (distanceSquared <= sightRangeSquared) {
            double followSpeed = speed * 0.6;

            if (dxToPlayer > 0.05) {
                moveX = followSpeed;
            } else if (dxToPlayer < -0.05) {
                moveX = -followSpeed;
            }

            if (dyToPlayer > 0.05) {
                moveY = followSpeed;
            } else if (dyToPlayer < -0.05) {
                moveY = -followSpeed;
            }
        } else {
            moveX = wanderX * speed;
            moveY = wanderY * speed;
        }

        double oldX = zx;
        double oldY = zy;
        verschiebenUm(moveX, moveY);

        if (stuck(oldX, nenneMx()) && stuck(oldY, nenneMy())) {
            stuckFrames++;
        } else {
            stuckFrames = 0;
        }

        if (stuckFrames >= 6) {
            stuckFrames = 0;
            pauseFrames = 2;
            berechneNeueWanderRichtung();
        }
    }

    private void berechneNeueWanderRichtung() {
        if (wanderX != 0) {
            wanderY = wanderX;
            wanderX = 0;
        } else if (wanderY != 0) {
            wanderX = -wanderY;
            wanderY = 0;
        } else {
            wanderX = speed;
            wanderY = 0;
        }
    }

    public boolean stuck(double a, double b) {
        double diff = a - b;
        if (diff < 0) {
            diff = -diff;
        }
        return diff < MOVE_TOLERANCE;
    }
}
