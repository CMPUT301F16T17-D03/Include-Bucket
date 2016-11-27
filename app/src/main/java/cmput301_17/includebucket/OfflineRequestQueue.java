package cmput301_17.includebucket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class OfflineRequestQueue {

    private static LinkedBlockingQueue<Request> requestQueue = null;
    private volatile boolean shutdown;

    public static LinkedBlockingQueue<Request> getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = new LinkedBlockingQueue<>();
        }
        return requestQueue;
    }

    /**
     * Taken from: https://www.javacodegeeks.com/2015/09/command-design-pattern.html
     * Accessed: November 26, 2016
     * Author: Rohit Joshi
     * @param request
     */
    public void addCommand(Request request) {
        try {
            requestQueue.put(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void queueManager() {
        while (!requestQueue.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        shutdown = true;
    }
}
