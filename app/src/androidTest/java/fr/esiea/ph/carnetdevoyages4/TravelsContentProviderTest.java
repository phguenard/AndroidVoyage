package fr.esiea.ph.carnetdevoyages4;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

import fr.esiea.ph.carnetdevoyages4.db.TravelsContentProvider;

public class TravelsContentProviderTest extends ProviderTestCase2<TravelsContentProvider>
{
    private ContentResolver contentResolver;

    public TravelsContentProviderTest() {
        super (TravelsContentProvider. class , "fr.esiea.ph.carnetdevoyages4" );
    }

    @Override
    protected void setUp() throws Exception {
        super .setUp();
        contentResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super .tearDown();
        cleanDatabase();
    }

    private void cleanDatabase() {
        contentResolver.delete(TravelsContentProvider.CONTENT_URI, null, null);
    }

    public void testQuery() {
        Cursor cursor =
                contentResolver.query(TravelsContentProvider.CONTENT_URI, null, null,
                        null, null);
        assertNotNull(cursor);
    }

    public void testInsert() {
        Uri myRowUri = contentResolver.insert(Uri.parse ( "content://fr.esiea.ph.carnetdevoyages" )/*TravelsContentProvider.CONTENT_URI*/, getDaValue());
        Log. i("Test", myRowUri.toString());
        assertNotNull (myRowUri);
        Cursor cursor =
                contentResolver.query(TravelsContentProvider.CONTENT_URI , null , null , null , null );
        assertNotNull (cursor);
        assertEquals ( 1 , cursor.getCount());
        Cursor cursor2 = contentResolver.query(myRowUri, null , null , null , null );
        assertNotNull (cursor2);
        assertEquals ( 1 , cursor2.getCount());
    }
    private ContentValues getDaValue(){
        ContentValues newValues = new ContentValues();
        newValues.put(TravelsContentProvider. VOY_TITRE , "Road Trip America" );
        newValues.put(TravelsContentProvider. VOY_DESTINATION , "Etats-Unis" );
        newValues.put(TravelsContentProvider. VOY_DATE , "2016/08/01" );
        newValues.put(TravelsContentProvider. VOY_USERID , "1" );
        return newValues;
    }
}