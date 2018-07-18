package tr.com.novasta.novasta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class cdb extends SQLiteOpenHelper {
    public cdb(Context context) {
        super(context, "Novasta Mobile APP", null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    boolean exec(String QUERY) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            sqLiteDatabase.execSQL(QUERY);
            return true;
        } catch (Exception e) {
            clib.err(205, e, QUERY);
            return false;
        }
    }

    private Cursor cursor(String QUERY) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery(QUERY, null);
        } catch (Exception e) {
            clib.err(200, e);
            return null;
        }
    }

    private String string(String QUERY, String DEFAULT) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor template;


            while ((template = db.rawQuery(QUERY, null)).moveToNext()) {
                return clib.decode(template.getString(0));
            }

            return DEFAULT;
        } catch (Exception e) {
            clib.err(201, e);
            return DEFAULT;
        }
    }

    private int integer(String QUERY, int DEFAULT) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor template = db.rawQuery(QUERY, null);

            while (template.moveToNext()) {
                return template.getInt(0);
            }

            return DEFAULT;
        } catch (Exception e) {
            clib.err(202, e);
            return DEFAULT;
        }
    }

    private long longwong(String QUERY, long DEFAULT) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor template = db.rawQuery(QUERY, null);

            while (template.moveToNext()) {
                return template.getLong(0);
            }

            return DEFAULT;
        } catch (Exception e) {
            clib.err(202, e);
            return DEFAULT;
        }
    }

    public Cursor categorises() {
        return cursor("SELECT id,image, title FROM categorises WHERE ACTIVE = 1 AND father = 0 ORDER BY ID");
    }

    public Cursor categorises(boolean onlySub) {
        return cursor("SELECT ID, IMAGE, title FROM categorises WHERE ACTIVE = 1 AND father != 0 ORDER BY ID DESC");
    }

    public Cursor subCategorises(int CAT_ID) {
        return cursor("SELECT ID, IMAGE, title FROM categorises WHERE ACTIVE = 1 AND father = " + CAT_ID + " ORDER BY ID DESC");
    }

    public Cursor reference(int ID) {
        if (ID == -1) {
            return cursor("SELECT ID, IMAGE, TITLE, DATE, DESCRIPTION FROM 'references' WHERE ACTIVE = 1 ORDER BY ID desc");
        } else {
            return cursor("SELECT ID, IMAGE, TITLE, DATE, DESCRIPTION FROM 'references' WHERE ID = " + ID + " AND ACTIVE = 1 ORDER BY ID desc");
        }
    }

    int counterReference(int ID) {
        return 0;//integer("SELECT counter FROM 'references' WHERE ID = " + ID, 0);
    }

    int counterNews(int ID) {
        return 0;//integer("SELECT counter FROM news WHERE id =" + ID, 0);
    }

    String url(int pid, int ptype) {
        if (ptype == TYPE_CATEGORISES) {
            return categorises(pid, "url", "");
        } else if (ptype == TYPE_NEWS) {
            return news(pid, "url", "");
        } else if (ptype == TYPE_REFERENCES) {
            return reference(pid, "url", "");
        } else {
            return "";
        }
    }

    int TYPE_CATEGORISES = 1, TYPE_NEWS = 2, TYPE_REFERENCES = 3;

    boolean cache(int pid, int ptype) {
        if (ptype == TYPE_CATEGORISES) {
            return cacheCategorises(pid);
        } else if (ptype == TYPE_NEWS) {
            return cacheNews(pid);
        } else if (ptype == TYPE_REFERENCES) {
            return cacheReferences(pid);
        }

        return false;
    }


    void cache(int pid, int ptype, boolean pedit) {
        if (ptype == TYPE_CATEGORISES) {
            Log.e("B", cache(pid, ptype) + "");
            cacheCategorises(pid, pedit);
            Log.e("A", cache(pid, ptype) + "");
        } else if (ptype == TYPE_NEWS) {
            Log.e("B", cache(pid, ptype) + "");
            cacheNews(pid, pedit);
            Log.e("a", cache(pid, ptype) + "");
        } else if (ptype == TYPE_REFERENCES) {
            Log.e("B", cache(pid, ptype) + "");
            cacheReferences(pid, pedit);
            Log.e("a", cache(pid, ptype) + "");
        }
    }

    boolean cacheNews(int ID) {
        int cached = integer("SELECT cache FROM news WHERE id =" + ID, 0);

        if (cached == 1) {
            return true;
        } else {
            return false;
        }
    }

    boolean cacheCategorises(int ID) {
        int cached = integer("SELECT cache FROM categorises WHERE id =" + ID, 0);

        if (cached == 1) {
            return true;
        } else {
            return false;
        }
    }

    void delReferences(int ID) {
        exec("UPDATE 'references' SET cache = 0 WHERE ID = " + ID);
    }

    void delCategorises(int ID) {
        exec("UPDATE categorises SET cache = 0 WHERE id = " + ID);
    }

    void delNews(int ID) {
        exec("UPDATE news SET cache = 0 WHERE id = " + ID);
    }

    boolean description(int pid, int ptype, String pdata) {
        try {
            if (ptype == TYPE_CATEGORISES) {
                return exec("UPDATE categorises SET description = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            } else if (ptype == TYPE_REFERENCES) {
                return exec("UPDATE 'references' SET description = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            } else if (ptype == TYPE_NEWS) {
                return exec("UPDATE news SET description = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            }

            Log.e("asda", pid + "--" + ptype + "--)" + pdata);

            return false;
        } catch (Exception e) {
            clib.err(6000, e);
            return false;
        }
    }

    boolean content(int pid, int ptype, String pdata) {
        try {
            if (ptype == TYPE_CATEGORISES) {
                return exec("UPDATE categorises SET content = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            } else if (ptype == TYPE_REFERENCES) {
                return exec("UPDATE 'references' SET content = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            } else if (ptype == TYPE_NEWS) {
                return exec("UPDATE news SET content = '" + clib.encode(pdata) + "' WHERE id = " + pid);
            }

            return false;
        } catch (Exception e) {
            clib.err(6100, e);
            return false;
        }
    }

    boolean cacheReferences(int ID) {
        int cache = integer("SELECT cache FROM 'references' WHERE id =" + ID, 0);

        if (cache == 1) {
            return true;
        } else {
            return false;
        }
    }

    void cacheCategorises(int ID, boolean edit) {
        exec("UPDATE categorises SET cache = 1 WHERE id = " + ID);
    }

    void cacheReferences(int ID, boolean edit) {
        exec("UPDATE 'references' SET cache =1 WHERE id = " + ID);
    }

    void cacheNews(int ID, boolean edit) {
        exec("UPDATE news SET cache = 1 WHERE id =" + ID);
    }

    int counterCategorises(int ID) {
        return 0;//integer("SELECT counter FROM categorises WHERE id =" + ID, 0);
    }

    void categorises(int ID) {
      //  exec("UPDATE categorises SET counter = counter + 1 WHERE id =" + ID);
    }

    void references(int ID) {
        //exec("UPDATE 'references' SET counter = counter + 1 WHERE id =" + ID);
    }

    void news(int ID) {
       // exec("UPDATE news SET counter = counter + 1 WHERE id =" + ID);
    }

    boolean checkCategorises(int ID) {
        int result = integer("SELECT ID FROM categorises WHERE ID = " + ID, -1);

        if (ID == result) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkReferences(int ID) {
        int result = integer("SELECT ID FROM 'references' WHERE ID = " + ID, -1);

        if (ID == result) {
            return true;
        } else {
            return false;
        }
    }

    boolean checkNews(int ID) {
        int result = integer("SELECT ID FROM 'news' WHERE ID = " + ID, -1);

        if (ID == result) {
            return true;
        } else {
            return false;
        }
    }

    public int reference(int ID, String COLUMN, int DEFAULT) {
        return integer("SELECT " + COLUMN + " FROM 'references' WHERE ID = " + ID, DEFAULT);
    }

    public String reference(int ID, String COLUMN, String DEFAULT) {
        return string("SELECT " + COLUMN + " FROM 'references' WHERE ID = " + ID, DEFAULT);
    }

    public String categorises(int ID, String TAG, String DEFAULT) {
        return string("SELECT " + TAG + " FROM categorises WHERE id =" + ID, DEFAULT);
    }

    int categorises(int ID, String TAG, int DEFAULT) {
        return integer("SELECT " + TAG + " FROM categorises WHERE id = " + ID, DEFAULT);
    }

    String news(int ID, String TAG, String DEFAULT) {
        return string("SELECT " + TAG + " FROM news WHERE ACTIVE = 1 AND ID =" + ID, DEFAULT);
    }

    int news(int ID, String TAG, int DEFAULT) {
        return integer("SELECT " + TAG + " FROM news WHERE ID =" + ID, DEFAULT);
    }

    //ID, TITLE, DESCRIPĞTIPON, IMAGE,DATE
    Cursor news() {
        return cursor("SELECT id, title, description, image, 'date' FROM news WHERE active = 1 ORDER BY id DESC");
    }

    public long last(String TAG) {
        try {
            Log.e("asda","SELECT value FROM info WHERE tag LIKE '%" + TAG + "%'");
            return longwong("SELECT value FROM info WHERE tag LIKE '%" + TAG + "%'", 0); // Long.parseLong(company(TAG, ""));
        } catch (Exception e) {
            clib.err(25449, e);
            return 0;
        }
        //return longwong("SELECT value FROM sync WHERE tag LIKE '%" + TAG + "%'", 0);
    }

    public boolean last(String TAG, long WONG) {
        if (last(TAG) < WONG) {
            return exec("UPDATE info SET value = " + WONG + " WHERE TAG Like '%" + TAG + "%'");
        } else {
            Log.e("DAHA ESKI BIR ZMAAN VAR", "JUMP JUMP");
            return true;
        }
    }

    public String company(String TAG, String DEFAULT) {
        return string("SELECT VALUE FROM info WHERE tag LIKE '" + TAG + "'", DEFAULT);
    }

    public Cursor searchInCategories(String LIKE) {
        // return cursor("SELECT ID, TITLE, IMAGE, TITLE FROM categorises WHERE (title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%' OR faqs LIKE '%" + LIKE + "%') AND ACTIVE = 1 AND father != 0 ORDER BY ID DESC");
        return cursor("SELECT ID, TITLE, IMAGE, TITLE FROM categorises WHERE (title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%') AND ACTIVE = 1 AND father != 0 ORDER BY ID DESC");
    }

    public Cursor searchInReferences(String LIKE) {
        //return cursor("SELECT ID, TITLE, IMAGE, DESCRIPTION, DATE FROM 'references' WHERE ACTIVE = 1 AND title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%' OR faqs LIKE '%" + LIKE + "%' OR date LIKE '%" + LIKE + "%'  ORDER BY ID DESC");
        return cursor("SELECT ID, TITLE, IMAGE, DESCRIPTION, DATE FROM 'references' WHERE ACTIVE = 1 AND title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%' OR date LIKE '%" + LIKE + "%'  ORDER BY ID DESC");
    }

    public boolean categorises(int ID, String HTML) {
        return exec("UPDATE categorises SET CONTENT = '" + clib.encode(HTML) + "' WHERE ID = " + ID);
    }

    public Cursor searchInNews(String LIKE) {
        //  return cursor("SELECT ID, TITLE, IMAGE, DESCRIPTION, DATE FROM news WHERE ACTIVE = 1 AND title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%' OR date LIKE '%" + LIKE + "%' ORDER BY ID DESC");
        return cursor("SELECT ID, TITLE, IMAGE, DESCRIPTION, DATE FROM news WHERE ACTIVE = 1 AND title LIKE '%" + LIKE + "%' OR description LIKE '%" + LIKE + "%' OR content LIKE '%" + LIKE + "%' OR date LIKE '%" + LIKE + "%' ORDER BY ID DESC");
    }

    public boolean company(String TAG, String VALUE, boolean isEdit) {
        return exec("UPDATE info SET value = '" + VALUE + "' WHERE tag LIKE '%" + TAG + "%'");
    }
}
