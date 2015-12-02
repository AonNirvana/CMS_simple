package com.egco428.cms_simple;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 1/12/2558.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "articles.db";
    public static final int DB_V = 1;

    private static final String CTFN = "CREATE TABLE IF NOT EXISTS ";
    private static final String DTFE = "DROP TABLE IF EXISTS ";
    public static final String TB_NAME = "articles";
    private static final String TM_NAME = "temp";
    private static final String KTYPE = "TEXT PRIMARY KEY";
    private static final String CTYPE = "TEXT";
    private static final String TB_CRATE = CTFN + TB_NAME + "(" + ViewFragment.ARG_KEY + " " + KTYPE + "," + ViewFragment.ARG_COL + " " + CTYPE + ")";
    private static final String TB_DROP = DTFE + TB_NAME;
    private static final String TM_CRATE = CTFN + TM_NAME + "(" + ViewFragment.ARG_KEY + " " + KTYPE + "," + ViewFragment.ARG_COL + " " + CTYPE + ")";
    private static final String TM_DROP = DTFE + TM_NAME;
    private static final String TM_DD1 = "INSERT INTO " + TM_NAME + "(" + ViewFragment.ARG_KEY + "," + ViewFragment.ARG_COL + ") ";
    private static final String TM_DD2 = "SELECT " + ViewFragment.ARG_KEY + "," + ViewFragment.ARG_COL + " FROM " + TB_NAME;
    private static final String TM_NTR = "ALTER TABLE " + TM_NAME + " RENAME TO " + TB_NAME;

    public MySQLiteHelper(Context context) {super(context,DB_NAME,null,DB_V);}
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TB_CRATE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TM_DROP);
        db.execSQL(TM_CRATE);
        db.execSQL(TM_DD1 + TM_DD2);
        db.execSQL(TB_DROP);
        db.execSQL(TM_NTR);
    }
}
