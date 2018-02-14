package teymi17.laxness;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by harry on 14/02/2018.
 */

@Database(entities = {Quote.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuoteDao quoteDao();
}