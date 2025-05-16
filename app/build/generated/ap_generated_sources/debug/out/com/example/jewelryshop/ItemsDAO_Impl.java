package com.example.jewelryshop;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ItemsDAO_Impl implements ItemsDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Item> __insertionAdapterOfItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ItemsDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItem = new EntityInsertionAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `termekek` (`nev`,`leiras`,`tipus`,`ar`,`kep`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        if (value.getNev() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getNev());
        }
        if (value.getTermekleiras() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTermekleiras());
        }
        final String _tmp;
        _tmp = TipusConverter.toString(value.getTipus());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        if (value.getAr() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAr());
        }
        stmt.bindLong(5, value.getKep());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM termekek";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Item item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Item>> getItems() {
    final String _sql = "SELECT * FROM termekek ORDER BY nev ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"termekek"}, false, new Callable<List<Item>>() {
      @Override
      public List<Item> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfNev = CursorUtil.getColumnIndexOrThrow(_cursor, "nev");
          final int _cursorIndexOfTermekleiras = CursorUtil.getColumnIndexOrThrow(_cursor, "leiras");
          final int _cursorIndexOfTipus = CursorUtil.getColumnIndexOrThrow(_cursor, "tipus");
          final int _cursorIndexOfAr = CursorUtil.getColumnIndexOrThrow(_cursor, "ar");
          final int _cursorIndexOfKep = CursorUtil.getColumnIndexOrThrow(_cursor, "kep");
          final List<Item> _result = new ArrayList<Item>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Item _item;
            final String _tmpNev;
            _tmpNev = _cursor.getString(_cursorIndexOfNev);
            final String _tmpTermekleiras;
            _tmpTermekleiras = _cursor.getString(_cursorIndexOfTermekleiras);
            final Tipus _tmpTipus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTipus);
            _tmpTipus = TipusConverter.toTipus(_tmp);
            final String _tmpAr;
            _tmpAr = _cursor.getString(_cursorIndexOfAr);
            final int _tmpKep;
            _tmpKep = _cursor.getInt(_cursorIndexOfKep);
            _item = new Item(_tmpNev,_tmpTermekleiras,_tmpTipus,_tmpAr,_tmpKep);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
