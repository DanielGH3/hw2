import java.lang.invoke.CallSite;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RunnableTask implements Runnable , Comparable<RunnableTask>{
    Callable operation;
    int priority;

    public RunnableTask(Task t){
        operation = t;
        priority = t.getPriority();
    }

    public RunnableTask(Callable c){
        operation = c;
        priority = TaskType.OTHER.getPriorityValue();
    }

    public RunnableTask(Callable c, TaskType type){
        operation = c;
        priority = type.getPriorityValue();
    }

    @Override
    public void run() {
        FutureTask future = new FutureTask<>(operation);
        Thread thread = new Thread(future);
        thread.start();  
          
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public int getPriority(){
        return priority;
    }

    @Override
    public int compareTo(RunnableTask o) {
        return Integer.compare(priority, o.getPriority());
    }
}
