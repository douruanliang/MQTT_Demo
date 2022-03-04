package io.dourl.mqtt.storage.db;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.dourl.mqtt.bean.MessageModelDao;
import io.dourl.mqtt.bean.SessionModelDao;
import io.dourl.mqtt.bean.UserModelDao;

/**
 * 数据库迁移帮助类
 * Created by zhangpeng on 15/12/31.
 */
class MigrationHelper {

    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
    private static MigrationHelper instance;

    public static MigrationHelper getInstance() {
        if(instance == null) {
            instance = new MigrationHelper();
        }
        return instance;
    }

    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        generateTempTables(db, daoClasses);
        dropTable(db, daoClasses);
        createTable(db, daoClasses);
        restoreData(db, daoClasses);
    }

    @SafeVarargs
    private final void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for(int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();

            StringBuilder createTableStringBuilder = new StringBuilder();

            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

            for(int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if(getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);

                    String type = null;

                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

                    if(daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }

                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");

            db.execSQL(createTableStringBuilder.toString());

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");

            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    private void dropTable(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            if (daoClass == MessageModelDao.class) {
                MessageModelDao.dropTable(db, true);
            } else if (daoClass == UserModelDao.class) {
                UserModelDao.dropTable(db, true);
            } else if (daoClass == SessionModelDao.class) {
                SessionModelDao.dropTable(db, true);
            }
        }
    }

    private void createTable(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            if (daoClass == MessageModelDao.class) {
                MessageModelDao.createTable(db, false);
            } else if (daoClass == UserModelDao.class) {
                UserModelDao.createTable(db, false);
            } else if (daoClass == SessionModelDao.class) {
                SessionModelDao.createTable(db, false);
            }
        }
    }

    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for(int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if(getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

            StringBuilder dropTableStringBuilder = new StringBuilder();

            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);

            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if(type.equals(String.class)) {
            return "TEXT";
        }
        if(type.equals(long.class) || type.equals(Long.class) || type.equals(int.class)
                || type.equals(Integer.class) || type.equals(short.class) || type.equals(Short.class)
                || type.equals(byte.class) || type.equals(Byte.class)) {
            return "INTEGER";
        }
        if(type.equals(Boolean.class) || type.equals(boolean.class)) {
            return "BOOLEAN";
        }

        throw new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.d(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}
