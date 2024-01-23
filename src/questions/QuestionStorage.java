package questions;

import java.io.File;
import java.util.ArrayList;

import json.DataType;
import json.JSON;
import json.KeyValuePair;

public class QuestionStorage {
    public static ArrayList<Question> math_easy, math_medium;
    public static ArrayList<Question> geo_easy, geo_medium;
    public static void load(){
        JSON math_json = new JSON(new File("res\\json\\questions_math.json"));
        KeyValuePair m1 = math_json.parse("JSON");
        KeyValuePair m2 = m1.findChild("easy");
        
        KeyValuePair m3 = m1.findChild("medium");

        math_easy = new ArrayList<Question>();
        math_medium = new ArrayList<Question>();

        loadQuestions(m2,math_easy);
        loadQuestions(m3,math_medium);
        

        JSON geo_json = new JSON(new File("res\\json\\questions_geography.json"));
        KeyValuePair g1 = geo_json.parse("JSON");
        KeyValuePair g2 = g1.findChild("easy");
        KeyValuePair g3 = g1.findChild("medium");

        geo_easy = new ArrayList<Question>();
        geo_medium = new ArrayList<Question>();

        loadQuestions(g2,geo_easy);
        loadQuestions(g3,geo_medium);
        printAllFrom(math_easy);
        printAllFrom(math_medium);
        printAllFrom(geo_easy);
        printAllFrom(geo_medium);
    }
    public static void printAllFrom(ArrayList<Question> questions){
        for(Question q : questions){
            System.out.println(q.question);
            System.out.println("Answer: " + q.correct);
        }
    }
    private static void loadQuestions(KeyValuePair keypair, ArrayList<Question> output){
        DataType[] children = keypair.getArray();
        for(DataType d : children){
            ArrayList<KeyValuePair> data = d.getObject();
            String question = data.get(0).getString();
            DataType[] wrongs = data.get(1).getArray();
            int len = wrongs.length;
            String[] wrongArray = new String[len];
            for(int i = 0; i < len; i++){
                wrongArray[i] = wrongs[i].getString();
            }
            String correct = data.get(2).getString();
            output.add(new Question(question,wrongArray,correct));
            
            
        }
    }
}
