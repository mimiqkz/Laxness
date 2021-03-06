package teymi17.laxness.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by meatyminx on 14/02/2018.
 */
@Entity
public class Quote {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name= "text")
    private String text;
    // novel could be an object type
    @ColumnInfo(name="novel")
    private String novel;
    //Temporary
    @ColumnInfo(name="year")
    private String year;

    public Quote(String text, String novel, String year) {
        this.text = text;
        this.novel = novel;
        this.year = year;
    }
    public Quote() {
    }

    public String getNovel() {
        return novel;
    }

    public void setNovel(String novel) {
        this.novel = novel;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}