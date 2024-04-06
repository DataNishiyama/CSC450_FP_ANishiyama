import java.util.concurrent.Semaphore;

//Main
public class Main {

    //Semaphore Class (Java Sync Primitive) / x1 Permits
    private static Semaphore semaphore = new Semaphore(1);

    //Total Ales
    private static int totalAles = 0;


    //Thread Implementation
    public static void main(String[] args) {
        Thread fillThread = new Thread(Main::fillAles);
        Thread drinkThread = new Thread(Main::drinkAles);

        fillThread.start();
        drinkThread.start();

        try {
            fillThread.join();
            drinkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Fill Ales
    private static void fillAles() {
        for (int i = 1; i <= 20; ++i) {
            try {
                semaphore.acquire(); //Acquire Permit
                totalAles = i;
                System.out.println("Filled " + i + " ale" + (i > 1 ? "s" : ""));
                if (totalAles == 20) {
                    semaphore.release(); //Release Permit
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); //Release Permit
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Drink Ales
    private static void drinkAles() {
        try {

            semaphore.acquire(); //Acquire Permit
            while (totalAles != 20) {
                semaphore.release();
                Thread.sleep(100);
                semaphore.acquire(); //Release Permit
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); //Release Permit
        }

        for (int i = 19; i >= 0; --i) {
            try {
                semaphore.acquire(); //Acquire Permit
                totalAles = i;
                System.out.println("Drank ale " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); //Release Permit
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
