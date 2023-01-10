import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class MyCallableReader implements Callable<Integer>{
    String filepath;
    int lines;

    public MyCallableReader(String path){
        filepath = path;
    }

    @Override
    public Integer call() throws Exception {
        return getNumOfLinesInFile(filepath);
    }

    public int getNumOfLinesInFile(String path){
        int lines = 0;
        Scanner filereader;
        try {
            filereader = new Scanner(new File(path));

            while (filereader.hasNextLine()) {
                filereader.nextLine();
                lines++;
            }

            filereader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
    } 
}
