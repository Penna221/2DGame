package questions;

public class Question {
    public String question;
    public String[] wrongs;
    public String correct;
    public Question(String question,String[] wrongs, String correct){
        this.question = question;
        this.wrongs = wrongs;
        this.correct = correct;
    }
}
