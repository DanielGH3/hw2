import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomExecutor {
    private PriorityBlockingQueue<RunnableTask> queue;
    ExecutorService pool;

    int[] prioraties = new int[10];

    public CustomExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();

        queue = new PriorityBlockingQueue<RunnableTask>(40, new ComparePriority());
        pool  = new ThreadPoolExecutor(cores / 2, cores - 1, 300, TimeUnit.MILLISECONDS,  (PriorityBlockingQueue)queue){
            @Override
            public void execute(Runnable command) {
                int p = ((RunnableTask)command).getPriority();
                prioraties[p - 1]++;
                super.execute(command);
            }       

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                int p = ((RunnableTask)r).getPriority();
                prioraties[p - 1]--;
                super.afterExecute(r, t);
            }
        };
    }

    public CustomExecutor(int size){
        queue = new PriorityBlockingQueue<RunnableTask>(40, new ComparePriority());
        pool  = new ThreadPoolExecutor(size, size, 300, TimeUnit.MILLISECONDS,  (PriorityBlockingQueue)queue);
    }

    public CustomExecutor(int coreSize, int maxSize){
        queue = new PriorityBlockingQueue<RunnableTask>(40, new ComparePriority());
        pool  = new ThreadPoolExecutor(coreSize, maxSize, 300, TimeUnit.MILLISECONDS,  (PriorityBlockingQueue)queue);
    }

    private void submit(RunnableTask task){
        if(!pool.isShutdown())
            pool.execute(task);
    }

    public void submit(Task t){
        RunnableTask rt = new RunnableTask(t);
        submit(rt);
    }

    public void submit(Callable c){   
        RunnableTask rt = new RunnableTask(c);
        submit(rt);
    }

    public void submit(Callable c, TaskType type){
        RunnableTask rt = new RunnableTask(c, type);
        submit(rt);
    }

    public int getCurrentMax(){ //O(10) == O(1)
        for(int i = 0; i < 10; i++){
            if(prioraties[i] != 0)
                return i + 1;
        }
        return 10;
    }

    public void Close(){
        pool.shutdown(); // Disable new tasks from being submitted
    }
}