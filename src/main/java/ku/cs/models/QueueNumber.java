package ku.cs.models;

import java.time.LocalDate;
import java.util.Date;

public class QueueNumber {
    private static int lastQueueNumber;
    private static LocalDate latestDateInQueue = LocalDate.now();

    public static int generateQueueNumber() {
        System.out.println("latest queue: "+lastQueueNumber);
        checkDate();
        //System.out.println("latest queue: "+lastQueueNumber);
        lastQueueNumber++;
        System.out.println("current queue" + lastQueueNumber);
        return lastQueueNumber;
    }
    public static void checkDate(){
        LocalDate currentDate = LocalDate.now();
        System.out.println("latest date: "+ latestDateInQueue);
        System.out.println("current date: "+ currentDate);
        if(currentDate.isAfter(latestDateInQueue)){
            lastQueueNumber = 0;
        }
        latestDateInQueue = currentDate;
    }

    public static int getLastQueueNumber() {
        return lastQueueNumber;
    }

    public static void setLastQueueNumber(int lastQueueNumber) {
        QueueNumber.lastQueueNumber = lastQueueNumber;
    }

    public static LocalDate getLatestDateInQueue() {
        return latestDateInQueue;
    }

    public static void setLatestDateInQueue(LocalDate latestDateInQueue) {
        QueueNumber.latestDateInQueue = latestDateInQueue;
    }
}
