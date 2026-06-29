public class Tile extends FIGUR {

    public Tile(char kind, double x, double y) {
        super(getTileImage(kind));
        if (kind == '#') {
            macheStatisch();

            setzeEbene(-1);
        } else if (kind == 'B') {
            macheSensor();
            setzeEbene(2);
            Tile sub = new Tile(' ', x, y);
            sub.setzeEbene(-1);
            sub.macheSensor();
            sub.setzeMittelpunkt(x - 8.5, y -8.5);
        } else {
            macheSensor();
            setzeEbene(-1);
        }
        this.setzeMittelpunkt(x - 8.5, y - 8.5);
    }

    private static String getTileImage(char kind) {
        if (kind == ' ') {
            return "shoot/gras.png";
        } else if (kind == '#') {
            return "shoot/wall.png";
        } else if (kind == '3') {
            return "shoot/power1.png";
        } else if (kind == 'L') {
            return "shoot/power2.png";
        } else if (kind == 'K') {
            return "shoot/power3.png";
        } else if (kind == 'S') {
            return "shoot/stone.png";
        } else if (kind == 'B') {
            return "shoot/bush1.png";
        } else if (kind == 'Z') {
            return "shoot/spawner.png";
        } else if (kind == 'N') {
            return "shoot/loading-power.png";
        }
        // default fallback
        return "shoot/gras.png";
    }
}
// Spawner TExture: "shoot/spawner.png"
