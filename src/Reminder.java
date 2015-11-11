import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    Timer timer;

    public Reminder(int seconds) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds * 1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            //System.out.format("Time's up!%n");
            timer.cancel(); //Terminate the timer thread
            System.exit(0);
        }
    }
}