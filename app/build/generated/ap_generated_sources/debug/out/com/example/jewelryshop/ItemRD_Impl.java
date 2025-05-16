package com.example.jewelryshop;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ItemRD_Impl extends ItemRD {
  private volatile ItemsDAO _itemsDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `termekek` (`nev` TEXT NOT NULL, `leiras` TEXT NOT NULL, `tipus` TEXT, `ar` TEXT NOT NULL, `kep` INTEGER NOT NULL, PRIMARY KEY(`nev`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e3cce210b70b5c2504404f1e490fc148')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `termekek`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTermekek = new HashMap<String, TableInfo.Column>(5);
        _columnsTermekek.put("nev", new TableInfo.Column("nev", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermekek.put("leiras", new TableInfo.Column("leiras", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermekek.put("tipus", new TableInfo.Column("tipus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermekek.put("ar", new TableInfo.Column("ar", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTermekek.put("kep", new TableInfo.Column("kep", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTermekek = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTermekek = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTermekek = new TableInfo("termekek", _columnsTermekek, _foreignKeysTermekek, _indicesTermekek);
        final TableInfo _existingTermekek = TableInfo.read(_db, "termekek");
        if (! _infoTermekek.equals(_existingTermekek)) {
          return new RoomOpenHelper.ValidationResult(false, "termekek(com.example.jewelryshop.Item).\n"
                  + " Expected:\n" + _infoTermekek + "\n"
                  + " Found:\n" + _existingTermekek);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e3cce210b70b5c2504404f1e490fc148", "b4c9dff92ef3f20c2be2b5862ea771ae");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "termekek");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `termekek`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public ItemsDAO itemdao() {
    if (_itemsDAO != null) {
      return _itemsDAO;
    } else {
      synchronized(this) {
        if(_itemsDAO == null) {
          _itemsDAO = new ItemsDAO_Impl(this);
        }
        return _itemsDAO;
      }
    }
  }
}
