package Main;

public class ABKnoten {
    private int alpha, beta, wert;

    public ABKnoten(int a, int b, int w) {
        alpha = a;
        beta = b;
        wert = w;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }

    public int getWert() {
        return wert;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public void setWert(int wert) {
        this.wert = wert;
    }

}
