import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ex2_1 {
    static String filePath = System.getProperty("user.dir") + "\\txtfiles";
    static final int MAX_THREADS = 5;

    ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

    public Ex2_1() {

    }

    /**
     * Generate files in dirc
     * 
     * @param n     number of files
     * @param seed  random seed for number of lines
     * @param bound max number of lines
     * @return paths to generated files
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        Random rnd = new Random(seed);
        String[] paths = new String[n];

        for (int i = 0; i < n; i++) {
            File tmp = new File(filePath, "file_" + i);
            paths[i] = filePath + "\\file_" + i;

            try {
                tmp.createNewFile();
                FileWriter file = new FileWriter(tmp);

                int lines = rnd.nextInt(bound);

                for (int j = 0; j < lines; j++) {
                    if (j != 0 && j < lines)
                        file.write("\n");

                    file.write("line number : " + j);
                }

                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return paths;
    }

    /**
     * Read number of lines in all file paths - one thread
     * 
     * @param filePaths files to read
     * @return number of lines in all files combined
     */
    public static int getNumOfLines(String[] filePaths) {
        int lines = 0;

        for (int i = 0; i < filePaths.length; i++) {
            Scanner filereader;
            try {
                filereader = new Scanner(new File(filePaths[i]));

                while (filereader.hasNextLine()) {
                    filereader.nextLine();
                    lines++;
                }

                filereader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }

    public int getNumOfLinesThreads(String[] filePaths) {
        int lines = 0;
        int fileCount = filePaths.length;
        int threadCount = fileCount >= MAX_THREADS ? MAX_THREADS : fileCount;
        MyThreadReader[] threads = new MyThreadReader[threadCount];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThreadReader();
        }

        int read = 0;
        boolean threadsAlive = true;
        while (read < fileCount || threadsAlive) {
            threadsAlive = false;
            for (int i = 0; i < threadCount; i++) {
                if (!threads[i].isAlive()) {
                    lines += threads[i].getLines();
                    if (read < fileCount) {
                        threads[i] = new MyThreadReader(filePaths[read++]);
                        threads[i].run();
                        threadsAlive = true;
                    }
                } else {
                    threadsAlive = true;
                }
            }
        }

        return lines;
    }

    public int getNumOfLinesThreadPool(String[] filePaths) {
        int lines = 0;
        int fileCount = filePaths.length;
        MyCallableReader[] fileReaders = new MyCallableReader[fileCount];
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < fileCount; i++) {
            fileReaders[i] = new MyCallableReader(filePaths[i]);
            futures.add(pool.submit(fileReaders[i]));
        }

        for (int i = 0; i < fileCount; i++) {
            try {
                lines += futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }

    public boolean deleteTextFiles(String[] filePaths) {
        boolean success = true;
        for (String path : filePaths) {
            File file = new File(path);
            if (!file.delete()) {
                success = false;
            }
        }
        return success;
    }

    public void Close() {
        pool.shutdown();
    }
}
