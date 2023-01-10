import java.util.concurrent.Callable;

interface TaskAction<T>{
    public T execute();
}

public class Task<T> implements Comparable<Task<T>> , Callable<T> {
    private TaskType taskType = TaskType.OTHER;
    private TaskAction<T> action;

    public Task(TaskAction<T> operation, TaskType taskType) {
        this.taskType = taskType;
        action = operation;
    }

    @Override
    public T call() {   
        return action.execute();
    }

    public static <T> Task<T> createTask(TaskAction<T> operation) {
        return new Task<>(operation, TaskType.OTHER);
    }

    public static <T> Task<T> createTask(TaskAction<T> operation, TaskType taskType) {
        return new Task<>(operation, taskType);
    }

    public int getPriority() {
        return taskType.getPriorityValue();
    }

    public void setType(TaskType type){
        this.taskType = type;
    }

    @Override
    public int compareTo(Task<T> other) {
        return Integer.compare(this.getPriority(), other.getPriority());
    }
}
