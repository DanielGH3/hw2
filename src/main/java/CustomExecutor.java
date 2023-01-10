import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomExecutor {
    private PriorityBlockingQueue<RunnableTask> queue = new PriorityBlockingQueue<RunnableTask>(40, new ComparePriority());;
    ExecutorService pool  = new ThreadPoolExecutor(1, 1, 100, TimeUnit.MILLISECONDS,  (PriorityBlockingQueue)queue);;

    public CustomExecutor() {

    }

    public void submit(Task t){
        Runnable r = new RunnableTask(t);
        pool.execute(r);
    }

    public void Close(){
        pool.shutdown(); // Disable new tasks from being submitted
    }
}