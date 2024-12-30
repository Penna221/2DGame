package tools;

import ui.Task;

public class Timer {
    private double elapsedTime, currentTime, lastTime;
    private double time;
    private Task t;
    public Timer(int time, Task t){
        this.time = time;
        this.t = t;
        lastTime = System.currentTimeMillis();
        elapsedTime = 0;
    }
    public void update(){
        currentTime = System.currentTimeMillis();
        elapsedTime += currentTime - lastTime;
        // System.out.println(elapsedTime);
        if(elapsedTime>=time){
            t.perform();
            elapsedTime-=time;
        }
        lastTime = currentTime;
    }
    public void backToStart(){
        lastTime = System.currentTimeMillis();
        elapsedTime = 0;
    }
    public void setTime(int millis){
        time = millis;
    }
}
