
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    public String title;
    public List<String> keywords = new ArrayList<String>();
    public String summary;
    public List<String> objectives = new ArrayList<String>();
    public List<String> intended_audience = new ArrayList<String>();
    public String level;
    public List<String> badges = new ArrayList<String>();
    public List<Activity> activies = new ArrayList<Activity>();

    public static class Activity {
        public String title;
        public String type;
        public String show_answer;
        public String pool_count;

        public Boolean redoable;
        public String body;
        public String level;
        public List<String> badges=new ArrayList<String>();
        public List<Problem> problems = new ArrayList<Problem>();
    }

    public static class Problem {
        public String title;
        public String type;
        public String body;
        public String level;
        public List<Choice> choices=new ArrayList<Choice>();
    }


    public class Choice {
        public String body;
        public String is_correct;

    }



public static String filePath = "src/lessons/小平的大冒险（范例）";
static Lesson.Activity a;
    public static File fileList(String strPath) throws FileNotFoundException {
        System.out.println(strPath);
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                String strFilePath = files[i].getAbsolutePath().toLowerCase();
                File file = new File(strFilePath);
                return file;
            }
        }
        return null;
    }
    public static JsonReader getContentByLocalFile(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        return reader;
    }
    public static void main(String[] args) throws IOException {
        FolderToJson();
    }

    public static void FolderToJson() throws IOException {
        Lesson lesson = null;
        File baseFolder = new File(filePath);
        lesson = new Gson().fromJson(
                getContentByLocalFile(fileList(filePath)), Lesson.class);

        File[] activities = new File(baseFolder, "activities").listFiles();


        for (File activityFolder : activities) {
            System.out.println("begin read:" + activityFolder.getName());

            a = new Gson().fromJson(
                    getContentByLocalFile(new File(filePath + "/activities/" + activityFolder.getName(), "activity.txt")),
                    Lesson.Activity.class);
            System.out.println("complete reading:" + activityFolder.getName());
            if ("quiz".equals(a.type)) {
                // read all problems
                File[] problems = new File(activityFolder, "problems").listFiles();
                getContentByLocalFile(fileList(filePath + "/activities/" + activityFolder.getName() + "/problems/"));
                for (File problemFile : problems) {
                    Lesson.Problem p = new Gson().fromJson(
                            getContentByLocalFile(problemFile),
                            Lesson.Problem.class);
                    a.problems.add(p);
                }
            }
            lesson.activies.add(a);
        }
        System.out.println(new Gson().toJson(lesson));
    }
}