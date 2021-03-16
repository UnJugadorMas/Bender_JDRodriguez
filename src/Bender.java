public class Bender {
    private char[][] mapa;
    private boolean finalCami;
    private String cami;
    private int posXInicial;
    private int posYInicial;
    private int posXFinal;
    private int posYFinal;
    private boolean isNullS = false;
    private boolean isNullN = false;

    public Bender(String mapa) {
        this.mapa = createMap(mapa);
    }

    public String run() {
        char[][] mapa = this.getMapa();
        this.cami = "";
        this.finalCami = true;
        this.posXInicial = 0;
        this.posYInicial = 0;
        this.posXFinal = 0;
        this.posYFinal = 0;

        /* Posicio inicial. Cerca el caràcter X */
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                if (mapa[i][j] == 'X') {
                    this.posXInicial = i;
                    this.posYInicial = j;
                }
            }
        }

        /* Posicio final. Cerca el caràcter $ */
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                if (mapa[i][j] == '$') {
                    this.posXFinal = i;
                    this.posYFinal = j;
                }
            }
        }
        return camiNormal();
    }

    public char[][] getMapa() {
        return this.mapa;
    }

    public char[][] createMap(String mapa) {

        /* Files */
        int files = 0;
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) == '\n') files++;
        }
        files++; /* Es suma una darrera */

        /* Columnes */
        int[] columnes = new int[files];
        int columnesFinal = 0;

        /* Es compten les columnes d'una fila */
        for (int i = 0; i < mapa.length(); i++) {
            for (int j = 0; j < columnes.length; j++) {
                while (mapa.charAt(i) != '\n') {
                    columnes[j]++;
                    i++;
                    if (i == mapa.length()) break;
                }
                i++;

                /* Es guarda el nombre de columnes major */
                if (columnes[j] > columnesFinal) {
                    columnesFinal = columnes[j];
                }
            }
        }

        /* Es guarda el mapa a un String sense el caràcter \n */
        int multiplicador = 0;
        String mapaComplet = "";
        for (int i = 0; i < mapa.length(); i++) {
            while (mapa.charAt(i) != '\n') {
                mapaComplet += mapa.charAt(i);
                i++;
                if (i == mapa.length()) break;
            }
            multiplicador++;

            /* Si és un mapa irregular s'afegeixen espais en blanc al final de cada fila menor */
            if (mapaComplet.length() < columnesFinal * multiplicador) {
                while (mapaComplet.length() < columnesFinal * multiplicador) {
                    mapaComplet += " ";
                }
            }
        }

        /* Aquest array bidimensional s'omple amb l'String anterior */
        char[][] chars = new char[files][columnesFinal];
        int posicio = 0;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnesFinal; j++) {
                chars[i][j] = mapaComplet.charAt(posicio);
                posicio++;
            }
        }

        return chars;
    }

    public String camiNormal() {

        while (this.finalCami) {
            if (this.mapa[this.posXInicial][this.posYInicial] == this.mapa[this.posXFinal][this.posYFinal])
                return this.cami;

            /* Comença provant si es pot moure al sud */
            if (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                if (this.mapa[this.posXInicial][this.posYInicial] == mapa[this.posXFinal][this.posYFinal])
                    return this.cami;

                /* Mentre no trobi un obstacle sguirà amb la seva direcció */
                while (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                    this.cami += "S";
                    this.posXInicial++;

                    /* Quan troba un inversor ha de canviar les seves prioritats */
                    if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                        while (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "S";
                            this.posXInicial++;
                        }
                        this.cami = camiInvertit();
                    }
                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;

                    /* Si troba un teletransportador s'ha de teletransportar al correcte */
                    if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();

                    /* Si es troba a la cantonada inferior dreta pot donar camí null */
                    if (this.mapa[this.posXInicial + 1][this.posYInicial] == '#' && this.mapa[this.posXInicial][this.posYInicial + 1] == '#')
                        this.isNullS = true;
                }

            } else { /* Si no pot provarà a l'est */
                if (this.mapa[this.posXInicial][this.posYInicial + 1] != '#') {
                    while (this.mapa[this.posXInicial][this.posYInicial + 1] != '#') {
                        if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                        this.cami += "E";
                        this.posYInicial++;

                        if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                            while (this.mapa[this.posXInicial][this.posYInicial + 1] != '#') {
                                if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                this.cami += "E";
                                this.posYInicial++;
                            }
                            this.cami = camiInvertit();
                        }
                        if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                        if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();
                    }
                } else { /* Si no pot provarà al nord */
                    if (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                        while (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "N";
                            this.posXInicial--;

                            if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                                while (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                    this.cami += "N";
                                    this.posXInicial--;
                                }
                                this.cami = camiInvertit();
                            }
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();

                            /* Si es troba a la cantonada superior dreta i abans a la inferior dreta serà camí null, ja que és infinit */
                            if (this.mapa[this.posXInicial - 1][this.posYInicial] == '#' && this.mapa[this.posXInicial][this.posYInicial + 1] == '#')
                                this.isNullN = true;
                            if (this.isNullN && this.isNullS) return null;
                        }

                    } else { /* Si no pot provarà a l'oest */
                        while (this.mapa[this.posXInicial][this.posYInicial - 1] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "W";
                            this.posYInicial--;

                            if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                                while (this.mapa[this.posXInicial][this.posYInicial - 1] != '#') {
                                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                    this.cami += "W";
                                    this.posYInicial--;
                                }
                                this.cami = camiInvertit();
                            }
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();
                        }
                    }
                }
            }
        }

        return this.cami;
    }

    public String camiInvertit() {
        this.isNullN = false;
        this.isNullS = false;
        while (this.finalCami) {

            /* Ara les seves prioritats seran nord, oest, sud i est, i farà el mateix que al cami normal */
            if (this.mapa[this.posXInicial][this.posYInicial] == this.mapa[this.posXFinal][this.posYFinal])
                return this.cami;
            if (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                if (this.mapa[this.posXInicial][this.posYInicial] == this.mapa[this.posXFinal][this.posYFinal])
                    return this.cami;
                while (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                    this.cami += "N";
                    this.posXInicial--;

                    if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                        while (this.mapa[this.posXInicial - 1][this.posYInicial] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "N";
                            this.posXInicial--;
                        }
                        this.cami = camiNormal();
                    }
                    if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();

                    /* Si es troba a la cantonada superior esquerra pot donar null */
                    if (this.mapa[this.posXInicial - 1][this.posYInicial] == '#' && this.mapa[this.posXInicial][this.posYInicial - 1] == '#')
                        this.isNullN = true;
                }
            } else {
                if (this.mapa[this.posXInicial][this.posYInicial - 1] != '#') {
                    while (this.mapa[this.posXInicial][this.posYInicial - 1] != '#') {
                        if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                        this.cami += "W";
                        this.posYInicial--;

                        if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                            while (this.mapa[this.posXInicial][this.posYInicial - 1] != '#') {
                                if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                this.cami += "W";
                                this.posYInicial--;
                            }
                            this.cami = camiNormal();
                        }
                        if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();
                    }
                } else {
                    if (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                        while (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "S";
                            this.posXInicial++;

                            if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                                while (this.mapa[this.posXInicial + 1][this.posYInicial] != '#') {
                                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                    this.cami += "S";
                                    this.posXInicial++;
                                }
                                this.cami = camiNormal();
                            }
                            if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();

                            /* Si es troba a la cantonada inferior esquerra i abans a la superior esquerra serà camí null */
                            if (this.mapa[this.posXInicial + 1][this.posYInicial] == '#' && this.mapa[this.posXInicial][this.posYInicial - 1] == '#')
                                this.isNullS = true;
                            if (this.isNullS && this.isNullN) return null;
                        }
                    } else {
                        while (this.mapa[this.posXInicial][this.posYInicial + 1] != '#') {
                            if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                            this.cami += "E";
                            this.posYInicial++;

                            if (this.mapa[this.posXInicial][this.posYInicial] == 'I') {
                                while (this.mapa[this.posXInicial][this.posYInicial + 1] != '#') {
                                    if (this.mapa[this.posXInicial][this.posYInicial] == '$') return this.cami;
                                    this.cami += "E";
                                    this.posYInicial++;
                                }
                                this.cami = camiNormal();
                            }
                            if (this.mapa[this.posXInicial][this.posYInicial] == 'T') teletransport();
                        }
                    }
                }
            }
        }

        return this.cami;
    }

    public void teletransport() {
        int teleportX = 0;
        int teleportY = 0;
        int temporalX = 0;
        int temporalY = 0;

        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                /* Cerca els caràcters T i que sigui diferent al que ja es troba */
                if (this.mapa[i][j] == 'T' && (i != this.posXInicial || j != this.posYInicial)) {
                    temporalX = i;
                    temporalY = j;

                    /* Si és el primer que es troba es guarda directament */
                    if (teleportX == 0 && teleportY == 0) {
                        teleportX = temporalX;
                        teleportY = temporalY;
                    }

                    /* Quan es troba un a menor distància es canvia el teletransportador final */
                    if (producteEscalar(temporalX, temporalY) < producteEscalar(teleportX, teleportY)) {
                        teleportX = temporalX;
                        teleportY = temporalY;
                    }

                    /* Si es troben dos a la mateixa distància */
                    if (producteEscalar(temporalX, temporalY) == producteEscalar(teleportX, teleportY)) {
                        if (temporalX <= teleportX && (temporalY > teleportY && temporalY > mapa[0].length / 2)) {
                            teleportX = temporalX;
                            teleportY = temporalY;
                        } else {
                            if (teleportY < mapa[0].length / 2 && temporalX >= teleportX) {
                                teleportX = temporalX;
                                teleportY = temporalY;
                            }
                        }
                    }
                }
            }
        }
        this.posXInicial = teleportX;
        this.posYInicial = teleportY;
    }

    public double producteEscalar(int x1, int y1) {
        /* Aquesta fórmula s'utilitza per calcular la distància entre dos vectors */
        return Math.sqrt(((x1 - this.posXInicial) * (x1 - this.posXInicial)) + ((y1 - this.posYInicial) * (y1 - this.posYInicial)));
    }
}
