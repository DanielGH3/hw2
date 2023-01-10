import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyThreadReader extends Thread {
    String filepath;
    int lines;

    public MyThreadReader(){
        lines = 0;
    }

    public MyThreadReader(String path){
        filepath = path;
        lines = 0;
    }
    
    @Override 
    public void run(){
        lines = getNumOfLinesInFile(filepath);
    }

    public int getLines(){
        return lines;
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
