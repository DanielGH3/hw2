import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunnableTask implements Runnable , Comparable<RunnableTask>{
    ExecutorService pool = Executors.newSingleThreadExecutor();
    Task task;

    public RunnableTask(Task t){
        task = t;
    }

    @Override
    public void run() {
        Future future = pool.submit(task);
        try {
            future.get();
            pool.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            pool.shutdown();
        }
    }

    public Task getTask(){
        return task;
    }

    @Override
    public int compareTo(RunnableTask o) {
        return task.compareTo(o.getTask());
    }
}
