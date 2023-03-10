package coin.market.cap.api;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Andre1483
 * @version 10032023.2248
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        final String[] args2 = args;
        final Timer timer = new Timer();

        SQLConnection.connect();

        TimerTask task = new TimerTask() {
            
            int mainCounter = 30;    // number of repetitions in seconds
            int secCounter = 10;    // time till first json-pull and upload
            int setSecCounter = 2 * 60; // time between first json-pull and upload in seconds

            @Override
            public void run() {
                if (secCounter > 0) {
                    System.out.println(mainCounter * setSecCounter + secCounter + " seconds till end");
                    System.out.println(secCounter-- + " seconds till next execution");
                }
                else if (secCounter == 0 && mainCounter > 0) {
                    try {
                        SQLConnection.main(args2);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    secCounter = setSecCounter;
                    System.out.println(mainCounter-- + "executions left");
                }
                else {
                    try {
                        SQLConnection.main(args2);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
     
                    SQLConnection.disconnect();

                    timer.cancel();

                    System.out.println("end");
                }
            }
        };
        timer.scheduleAtFixedRate(task, 100, 1000);
    }
}