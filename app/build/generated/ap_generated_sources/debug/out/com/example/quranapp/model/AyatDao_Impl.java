package com.example.quranapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AyatDao_Impl implements AyatDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<AyatEntity> __insertAdapterOfAyatEntity;

  public AyatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfAyatEntity = new EntityInsertAdapter<AyatEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ayat` (`id`,`surahNumber`,`ayatNumber`,`text`,`latin`,`translation`,`audio`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final AyatEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSurahNumber());
        statement.bindLong(3, entity.getAyatNumber());
        if (entity.getText() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getText());
        }
        if (entity.getLatin() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getLatin());
        }
        if (entity.getTranslation() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getTranslation());
        }
        if (entity.getAudio() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getAudio());
        }
      }
    };
  }

  @Override
  public void insertAll(final List<AyatEntity> ayats) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfAyatEntity.insert(_connection, ayats);
      return null;
    });
  }

  @Override
  public LiveData<List<AyatEntity>> getAyatsBySurah(final int surahNumber) {
    final String _sql = "SELECT * FROM ayat WHERE surahNumber = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"ayat"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, surahNumber);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSurahNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "surahNumber");
        final int _columnIndexOfAyatNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ayatNumber");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfLatin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latin");
        final int _columnIndexOfTranslation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translation");
        final int _columnIndexOfAudio = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audio");
        final List<AyatEntity> _result = new ArrayList<AyatEntity>();
        while (_stmt.step()) {
          final AyatEntity _item;
          final int _tmpSurahNumber;
          _tmpSurahNumber = (int) (_stmt.getLong(_columnIndexOfSurahNumber));
          final int _tmpAyatNumber;
          _tmpAyatNumber = (int) (_stmt.getLong(_columnIndexOfAyatNumber));
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpLatin;
          if (_stmt.isNull(_columnIndexOfLatin)) {
            _tmpLatin = null;
          } else {
            _tmpLatin = _stmt.getText(_columnIndexOfLatin);
          }
          final String _tmpTranslation;
          if (_stmt.isNull(_columnIndexOfTranslation)) {
            _tmpTranslation = null;
          } else {
            _tmpTranslation = _stmt.getText(_columnIndexOfTranslation);
          }
          final String _tmpAudio;
          if (_stmt.isNull(_columnIndexOfAudio)) {
            _tmpAudio = null;
          } else {
            _tmpAudio = _stmt.getText(_columnIndexOfAudio);
          }
          _item = new AyatEntity(_tmpSurahNumber,_tmpAyatNumber,_tmpText,_tmpLatin,_tmpTranslation,_tmpAudio);
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<AyatEntity> getAyatsBySurahSync(final int surahNumber) {
    final String _sql = "SELECT * FROM ayat WHERE surahNumber = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, surahNumber);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfSurahNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "surahNumber");
        final int _columnIndexOfAyatNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ayatNumber");
        final int _columnIndexOfText = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "text");
        final int _columnIndexOfLatin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latin");
        final int _columnIndexOfTranslation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translation");
        final int _columnIndexOfAudio = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "audio");
        final List<AyatEntity> _result = new ArrayList<AyatEntity>();
        while (_stmt.step()) {
          final AyatEntity _item;
          final int _tmpSurahNumber;
          _tmpSurahNumber = (int) (_stmt.getLong(_columnIndexOfSurahNumber));
          final int _tmpAyatNumber;
          _tmpAyatNumber = (int) (_stmt.getLong(_columnIndexOfAyatNumber));
          final String _tmpText;
          if (_stmt.isNull(_columnIndexOfText)) {
            _tmpText = null;
          } else {
            _tmpText = _stmt.getText(_columnIndexOfText);
          }
          final String _tmpLatin;
          if (_stmt.isNull(_columnIndexOfLatin)) {
            _tmpLatin = null;
          } else {
            _tmpLatin = _stmt.getText(_columnIndexOfLatin);
          }
          final String _tmpTranslation;
          if (_stmt.isNull(_columnIndexOfTranslation)) {
            _tmpTranslation = null;
          } else {
            _tmpTranslation = _stmt.getText(_columnIndexOfTranslation);
          }
          final String _tmpAudio;
          if (_stmt.isNull(_columnIndexOfAudio)) {
            _tmpAudio = null;
          } else {
            _tmpAudio = _stmt.getText(_columnIndexOfAudio);
          }
          _item = new AyatEntity(_tmpSurahNumber,_tmpAyatNumber,_tmpText,_tmpLatin,_tmpTranslation,_tmpAudio);
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          _item.setId(_tmpId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
