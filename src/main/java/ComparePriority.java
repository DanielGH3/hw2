import java.util.Comparator;

public class ComparePriority implements Comparator<RunnableTask> {

    @Override
    public int compare(RunnableTask o1, RunnableTask o2) {
        return o1.compareTo(o2);
    }
}
