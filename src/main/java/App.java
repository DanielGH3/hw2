public class App {
    public static void main(String[] args) {
        Ex2_1 ex = new Ex2_1();

        String[] paths = Ex2_1.createTextFiles(10, 10, 1000);

        long start = System.currentTimeMillis();
        System.out.println(Ex2_1.getNumOfLines(paths));
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("time1 = " + elapsedTime);

        start = System.currentTimeMillis();
        System.out.println(ex.getNumOfLinesThreads(paths));
        end = System.currentTimeMillis();
        elapsedTime = end - start;
        System.out.println("time2 = " + elapsedTime);


        start = System.currentTimeMillis();
        System.out.println(ex.getNumOfLinesThreadPool(paths));
        end = System.currentTimeMillis();
        elapsedTime = end - start;
        System.out.println("time3 = " + elapsedTime);

        CustomExecutor exx = new CustomExecutor();

        Task t1 =  Task.createTask(
            ()->{try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } System.out.println("done"); return 1;},
            TaskType.OTHER
        );

        Task t2 =  Task.createTask(
            ()->{System.out.println("hello2"); return 1;},
            TaskType.OTHER
        );

        Task t3 =  Task.createTask(
            ()->{System.out.println("hello1"); return 1;},
            TaskType.COMPUTATIONAL
        );    

        exx.submit(t1);
        exx.submit(t2);
        exx.submit(t3);
        exx.Close();
        ex.Close();
    }
}
