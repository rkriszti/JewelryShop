package com.example.jewelryshop;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
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
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProfilDAO_Impl implements ProfilDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Profil> __insertionAdapterOfProfil;

  private final EntityDeletionOrUpdateAdapter<Profil> __updateAdapterOfProfil;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProfilByEmail;

  public ProfilDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfil = new EntityInsertionAdapter<Profil>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `profil` (`email`,`nev`,`profil_kep_uri`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Profil value) {
        if (value.getEmail() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEmail());
        }
        if (value.getNev() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNev());
        }
        if (value.getProfilKepUri() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getProfilKepUri());
        }
      }
    };
    this.__updateAdapterOfProfil = new EntityDeletionOrUpdateAdapter<Profil>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `profil` SET `email` = ?,`nev` = ?,`profil_kep_uri` = ? WHERE `email` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Profil value) {
        if (value.getEmail() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEmail());
        }
        if (value.getNev() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNev());
        }
        if (value.getProfilKepUri() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getProfilKepUri());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getEmail());
        }
      }
    };
    this.__preparedStmtOfDeleteProfilByEmail = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM profil WHERE email = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Profil profil) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfProfil.insert(profil);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Profil profil) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfProfil.handle(profil);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteProfilByEmail(final String email) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProfilByEmail.acquire();
    int _argIndex = 1;
    if (email == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, email);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteProfilByEmail.release(_stmt);
    }
  }

  @Override
  public LiveData<Profil> getProfil() {
    final String _sql = "SELECT * FROM profil LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"profil"}, false, new Callable<Profil>() {
      @Override
      public Profil call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfNev = CursorUtil.getColumnIndexOrThrow(_cursor, "nev");
          final int _cursorIndexOfProfilKepUri = CursorUtil.getColumnIndexOrThrow(_cursor, "profil_kep_uri");
          final Profil _result;
          if(_cursor.moveToFirst()) {
            _result = new Profil();
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            _result.setEmail(_tmpEmail);
            final String _tmpNev;
            _tmpNev = _cursor.getString(_cursorIndexOfNev);
            _result.setNev(_tmpNev);
            final String _tmpProfilKepUri;
            _tmpProfilKepUri = _cursor.getString(_cursorIndexOfProfilKepUri);
            _result.setProfilKepUri(_tmpProfilKepUri);
          } else {
            _result = null;
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

  @Override
  public Profil getProfilSync() {
    final String _sql = "SELECT * FROM profil LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfNev = CursorUtil.getColumnIndexOrThrow(_cursor, "nev");
      final int _cursorIndexOfProfilKepUri = CursorUtil.getColumnIndexOrThrow(_cursor, "profil_kep_uri");
      final Profil _result;
      if(_cursor.moveToFirst()) {
        _result = new Profil();
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        _result.setEmail(_tmpEmail);
        final String _tmpNev;
        _tmpNev = _cursor.getString(_cursorIndexOfNev);
        _result.setNev(_tmpNev);
        final String _tmpProfilKepUri;
        _tmpProfilKepUri = _cursor.getString(_cursorIndexOfProfilKepUri);
        _result.setProfilKepUri(_tmpProfilKepUri);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Profil getProfilByEmail(final String email) {
    final String _sql = "SELECT * FROM profil WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfNev = CursorUtil.getColumnIndexOrThrow(_cursor, "nev");
      final int _cursorIndexOfProfilKepUri = CursorUtil.getColumnIndexOrThrow(_cursor, "profil_kep_uri");
      final Profil _result;
      if(_cursor.moveToFirst()) {
        _result = new Profil();
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        _result.setEmail(_tmpEmail);
        final String _tmpNev;
        _tmpNev = _cursor.getString(_cursorIndexOfNev);
        _result.setNev(_tmpNev);
        final String _tmpProfilKepUri;
        _tmpProfilKepUri = _cursor.getString(_cursorIndexOfProfilKepUri);
        _result.setProfilKepUri(_tmpProfilKepUri);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
