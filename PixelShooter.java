public class PixelShooter extends SPIEL {

    private String[] tilemap = {
            "#################################",
            "#  Z #            BB            #",
            "#    ###S#####              #####",
            "#                BBBBB          #",
            "#SSS########     BBBBBB   #######",
            "#    #                  #       #",
            "##SS#####            # ####  ####",
            "#  # #       S S      # #  Z    #",
            "##        BB         ## # ## ####",
            "#  #                            #",
            "####SSS###       ##   # # #######",
            "#             3       # #       #",
            "####     #           ## # ##  ###",
            "#        #                      #",
            "# ##     #    BB     ## ###   # #",
            "#  #                            #",
            "#########                 #######",
            "# Z  B       SS        BBBBBBBB #",
            "# ##BBB                #### # # #",
            "#           S     BBB           #",
            "#####B                       ####",
            "# ## BBB        BBBBB         #Z#",
            "#             BBBB              #",
            "#########                 #######",
            "#    S   S   #                  #",
            "##S#######          B     #######",
            "#                 ##            #",
            "####BB          ###   #####   ###",
            "#         L             ### # # #",
            "# Z            S  ##  S         #",
            "## # #         S BBB#   #B#######",
            "#    #       #    ##     BBBB K #",
            "#################################",
    };

    // Variables
    private boolean bush = false;
    private boolean gameOver = false;

    private char mode = '1';
    private int score = 0;

    // Player
    private Spieler player;

    // Tile Felder + Count
    private Tile[] walls;
    private int wallCount = 0;
    private Tile[] spawner;
    private int spawnerCount = 0;
    private Tile[] bushes;
    private int bushCount = 0;
    private Tile[] loading;

    // Felder
    private Projektil[] Projektile;
    private int ProjektileCount = 0;

    private Zombie[] enemy;
    private int enemyCount = 0;

    // Power Tile
    private Tile power1;
    private Tile power2;
    private Tile power3;

    // TIMER
    private double cooldown = 15.0;
    private double SpawnTimer = 0.0;
    private double[] powerTimer;

    private double hitTime = 0.0;
    private double hitCooldown = 1.0;

    private double shootCooldown = 0.25;
    private double shootTimer = 0.0;

    // TEXT
    private TEXT GO;
    private TEXT Leben;
    private TEXT ScoreLabel;

    public PixelShooter() {
        super(700, 700, true);
        walls = new Tile[600];
        spawner = new Tile[50];
        bushes = new Tile[200];
        loading = new Tile[3];
        powerTimer = new double[3];
        loadMap();
        player = new Spieler(0, 0);
        setzeSchwerkraft(0);
        setzeKamerafokus(player);
        setzeKamerazoom(60);
        Projektile = new Projektil[300];
        enemy = new Zombie[100];

        ScoreLabel = new TEXT(player.nenneMx() + 8.5, player.nenneMy() + 8.5, 1, player.getHearts());
        ScoreLabel.setzeFarbe("blau");
        ScoreLabel.setzeSichtbar(true);
        ScoreLabel.setzeEbenenposition(6);

        Leben = new TEXT(player.nenneMx() + 8.5, player.nenneMy() + 8.5, 1, player.getHearts());
        Leben.setzeFarbe("rot");
        Leben.setzeSichtbar(true);
        Leben.setzeEbenenposition(6);
    }

    private void loadMap() {
        double tileSize = 0.53;
        int rowCount = tilemap.length;
        int columnCount = tilemap[0].length();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tilemap[r];
                char tileMapChar = row.charAt(c);

                double x = c * tileSize;
                double y = r * tileSize;

                Tile tile = new Tile(tileMapChar, x, y);
                if (tileMapChar == '3') {
                    power1 = tile;
                    Tile lTile = new Tile('N', x, y);
                    loading[0] = lTile;
                    loading[0].setzeSichtbar(false);
                }
                if (tileMapChar == 'L') {
                    power2 = tile;
                    Tile lTile = new Tile('N', x, y);
                    loading[1] = lTile;
                    loading[1].setzeSichtbar(false);
                }
                if (tileMapChar == 'K') {
                    power3 = tile;
                    Tile lTile = new Tile('N', x, y);
                    loading[2] = lTile;
                    loading[2].setzeSichtbar(false);
                }
                if (tileMapChar == 'Z' && spawnerCount < 50 && spawner != null) {
                    spawner[spawnerCount] = tile;
                    spawnerCount++;
                }
                if (tileMapChar == '#' && wallCount < 600 && walls != null) {
                    walls[wallCount] = tile;
                    wallCount++;
                }
                if (tileMapChar == 'B' && bushCount < 600 && bushes != null) {
                    bushes[bushCount] = tile;
                    bushCount++;
                }
            }
        }
    }

    @Override
    public void bildAktualisierungReagieren(double sekunden) {
        if (gameOver != true) {
            if (player != null && spawner != null && Leben != null) {
                if (shootTimer > 0) {
                    shootTimer -= sekunden;
                    if (shootTimer < 0) {
                        shootTimer = 0;
                    }
                }
                if (SpawnTimer > 0) {
                    SpawnTimer -= sekunden;
                    if (SpawnTimer < 0) {
                        SpawnTimer = 0;
                    }
                }

                if (hitTime > 0) {
                    hitTime -= sekunden;
                    if (hitTime < 0) {
                        hitTime = 0;
                    }
                }

                for (int i = 0; i < powerTimer.length; i++) {
                    if (powerTimer[i] > 0) {
                        powerTimer[i] -= sekunden;
                        if (powerTimer[i] < 0) {
                            powerTimer[i] = 0;
                            loading[i].setzeSichtbar(false);
                        }
                    }
                }

                if (istTasteGedrueckt(87) || istTasteGedrueckt(38)) { // W or UP
                    player.verschiebenUm(0, 0.05);
                }
                if (istTasteGedrueckt(83) || istTasteGedrueckt(40)) { // S or DOWN
                    player.verschiebenUm(0, -0.05);
                }
                if (istTasteGedrueckt(65) || istTasteGedrueckt(37)) { // A or LEFT
                    player.verschiebenUm(-0.05, 0);
                }
                if (istTasteGedrueckt(68) || istTasteGedrueckt(39)) { // D or RIGHT
                    player.verschiebenUm(0.05, 0);
                }

                if (Projektile != null) {
                    for (int i = 0; i < ProjektileCount; i++) {
                        if (Projektile[i] != null) {
                            Projektile[i].move();
                        }
                    }
                }

                // long start = System.nanoTime();

                if (player.beruehrt(power1) && powerTimer[0] == 0) {
                    mode = '3';
                    loading[0].setzeSichtbar(true);
                    powerTimer[0] = cooldown;
                }
                if (player.beruehrt(power2) && player.getHearts() < 3 && powerTimer[1] == 0) {
                    player.plusHeart();
                    loading[1].setzeSichtbar(true);
                    powerTimer[1] = cooldown;
                }
                if (player.beruehrt(power3) && powerTimer[2] == 0) {
                    mode = 'B';
                    loading[2].setzeSichtbar(true);
                    powerTimer[2] = cooldown;
                }

                for (int i = 0; i < bushCount; i++) {
                    if (bushes[i] != null) {
                        if (player.beruehrt(bushes[i])) {
                            bush = true;
                        }
                    }
                }

                for (int z = 0; z < enemyCount; z++) {
                    if (enemy[z] != null) {
                        enemy[z].move(player.nenneMx(), player.nenneMy(), !bush);
                        if (player.beruehrt(enemy[z]) && hitTime == 0) {
                            player.minusHeart();
                            hitTime = hitCooldown;
                            break;
                        }
                    }
                }

                bush = false;

                for (int i = 0; i < ProjektileCount; i++) {
                    if (Projektile[i] != null) {

                        Projektile[i].move();

                        // Wand prüfen
                        for (int w = 0; w < walls.length; w++) {
                            if (walls[w] != null && Projektile[i].beruehrt(walls[w])) {
                                if (Projektile[i].getBomb()) {
                                    for (int k = 0; k < enemyCount; k++) {
                                        if (enemy[k] != null && Projektile[i].getKillRadius(enemy[k])) {
                                            enemy[k].setzeSichtbar(false);
                                            enemy[k] = null;

                                        }
                                    }
                                }
                                Projektile[i].setzeSichtbar(false);
                                Projektile[i] = null;
                                break;
                            }
                        }

                        // Zombies prüfen
                        for (int z = 0; z < enemyCount; z++) {
                            if (enemy[z] != null && Projektile[i] != null) {
                                if (Projektile[i].beruehrt(enemy[z])) {
                                    enemy[z].setzeSichtbar(false);
                                    enemy[z] = null;
                                    Projektile[i].setzeSichtbar(false);
                                    Projektile[i] = null;
                                    score++;
                                    break;
                                }
                            }
                        }
                    }
                }

                // System.out.println((System.nanoTime() - start) / 1_000_000.0 + " ms");

                if (player.getHearts() == 0) {
                    GO = new TEXT(player.nenneMx(), player.nenneMy(), 3, "GameOver");
                    GO.setzeSchriftart("test.ttf");
                    GO.setzeFarbe("rot");
                    GO.setzeSichtbar(true);
                    GO.setzeEbenenposition(3);
                    gameOver = true;
                }

                Leben.setzeMittelpunkt(player.nenneMx() + 5.3, player.nenneMy() + 5.3);
                Leben.setzeInhalt(IntHeart());

                ScoreLabel.setzeMittelpunkt(player.nenneMx() - 5.3, player.nenneMy() + 5.3);
                ScoreLabel.setzeInhalt(String.valueOf(score));

                if (SpawnTimer == 0) {
                    if (spawner != null) {
                        for (int i = 0; i < spawner.length; i++) {
                            if (spawner[i] != null) {
                                spawnZombie(i + enemyCount, spawner[i].nenneMx(), spawner[i].nenneMy());
                            }
                        }
                        SpawnTimer = cooldown;
                    }
                }
            }
        }
    }

    @Override
    public void klickReagieren(double x, double y) {
        double off = 0.25;
        double px = player.nenneMx();
        double py = player.nenneMy();
        if (this.Projektile != null) {
            if (shootTimer == 0) {
                if (ProjektileCount + 2 < Projektile.length) {
                    if (mode == '3') {
                        for (int i = 0; i < 3; i++) {
                            Projektile[ProjektileCount + i] = new Projektil(
                                    px + ((i - 1) * off) * -Math.sin(Math.toRadians(calcDIR(px, py, x, y))),
                                    py + ((i - 1) * off) * Math.cos(Math.toRadians(calcDIR(px, py, x, y))), mode,
                                    calcDIR(px, py, x, y));
                        }

                        /// BOMB

                        ProjektileCount += 3;
                    } else {
                        Projektile[ProjektileCount] = new Projektil(px, py, mode, calcDIR(px, py, x, y));
                        ProjektileCount++;
                    }
                }
                shootTimer = shootCooldown;
                mode = '1';
            }
        }
    }

    public void spawnZombie(int i, double xx, double yy) {
        enemy[i] = new Zombie(xx, yy);
        enemyCount++;
    }

    public String IntHeart() {
        StringBuilder Hearts = new StringBuilder("");
        for (int i = 0; i < player.getHearts(); i++) {
            Hearts.append("❤︎");
        }
        return Hearts.toString();
    }

    private double calcDIR(double px, double py, double mx, double my) {
        return Math.toDegrees(Math.atan2(my - py, mx - px));
    }

    public static void main(String[] args) {
        new PixelShooter();
    }
}