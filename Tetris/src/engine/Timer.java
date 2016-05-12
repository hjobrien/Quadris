package engine;

public class Timer {
  
  public static final double MIN_TIME_PER_TURN = 100;

  public Timer() {

  }

  public void start() {

  }

  public void stop() {

  }

  public double updateTime(double time) {
    if (time > MIN_TIME_PER_TURN) {
      return time * 0.9936; // makes the time reach the minimum time after 60 seconds of 60 updates
                            // per second (360 ticks)
    }
    else {
      return MIN_TIME_PER_TURN;
    }
  }
}
