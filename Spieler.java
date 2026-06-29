public class Spieler extends FIGUR {
    private int Leben = 30;
    public Spieler(double x, double y) {
        super("shoot/player.png");
        setzeMittelpunkt(x, y);
        macheDynamisch();
        setzeEbene(1);
        System.out.println("Spieler erstellt");
    }

    public int getHearts() {
        return Leben;
    }

    public void minusHeart() {
        Leben--;
    }

    public void plusHeart() {
        Leben++;
    }
}