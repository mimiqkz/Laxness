package teymi17.laxness;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by meatyminx on 14/02/2018.
 */

public class Quote {

    private String text;
    // novel could be an object type
    private String novel;
    //Temporary
    private int year;

    public Quote(String text, String novel, int year) {
        this.text = text;
        this.novel = novel;
        this.year = year;
    }

    public String getNovel() {
        return novel;
    }

    public void setNovel(String novel) {
        this.novel = novel;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}