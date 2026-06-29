import ea.edu.EduActor;

public class Projektil extends FIGUR {
    private double speed = 0.2;
    private double dx;
    private double dy;
    private char mo;
    private final int subSteps = 4;

    public Projektil(double px, double py, char mode, double angle) {
        super(getImg(mode));
        setzeMittelpunkt(px, py);
        macheSensor();
        setzeEbene(1);
        skaliere(0.7);
        setzeDrehwinkel(angle);
        mo = mode;
        dx = speed * Math.cos(Math.toRadians(angle));
        dy = speed * Math.sin(Math.toRadians(angle));
    }

    public void move() {
        double stepDx = dx / subSteps;
        double stepDy = dy / subSteps;
        for (int i = 0; i < subSteps; i++) {
            verschiebenUm(stepDx, stepDy);
        }
    }

    public boolean getBomb() {
        return mo == 'B' ? true : false;
    }

    public boolean getKillRadius(EduActor ea) {
        if (Math.hypot(berechneAbstandX(ea), berechneAbstandY(ea)) < 10) {
            return true;
        } else {
            return false;
        }
    }

    private static String getImg(char mode) {
        if (mode == 'B') {
            return "shoot/bomb.png";
        } else {
            return "shoot/projektil.png";
        }
    }
}