package Main;

/**
 * Diese Klasse ueberprueft die verbleibende Zeit eines Zuges
 */
public class Timer extends Thread{
    private long time,start, ende;
    private boolean istFertig = false;
    public Timer(long time) {
        start = System.currentTimeMillis();
        this.time = time;
        start();
    }

    public void run() {
        try {
            if(time < 100) {
                istFertig = true;
            } else {
                sleep(time-200);
                ende = System.currentTimeMillis();
                istFertig = true;
            }
        } catch (java.lang.InterruptedException e) {
            System.out.println("Fehler");
        }
    }

    public boolean getStatus() {
        return istFertig;
    }

    public void setIstFertig(boolean fertig) {
        istFertig = fertig;
    }

    public void warten() {
        setIstFertig(false);
        try {
            wait();
        } catch (java.lang.InterruptedException e) {
            System.out.println("Fehler");
        }
    }

}
