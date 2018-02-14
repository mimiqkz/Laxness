package teymi17.laxness;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by harry on 14/02/2018.
 */

@Dao
public interface QuoteDao {
    @Query("SELECT * FROM user")
    List<Quote> getAll();

    @Query("SELECT * FROM quote WHERE uid IN (:quoteIds)")
    List<Quote> loadAllByIds(int[] quoteIds);

    @Insert
    void insertAll(Quote... quotes);

    @Delete
    void delete(Quote quote);
}
