package cmput301_17.includebucket;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class OfflineRequestQueue {

    private static LinkedBlockingQueue<Command> requestQueue = null;

    public static LinkedBlockingQueue<Command> getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = new LinkedBlockingQueue<>();
        }
        return requestQueue;
    }

    /**
     * Taken from: https://www.javacodegeeks.com/2015/09/command-design-pattern.html
     * Accessed: November 26, 2016
     * Author: Rohit Joshi
     * @param command
     */
    public static void addCommand(Command command) {
        try {
            getRequestQueue().put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void execute() {
        while (!getRequestQueue().isEmpty()) {
            try {
                Command c = OfflineRequestQueue.getRequestQueue().take();
                c.execute();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

