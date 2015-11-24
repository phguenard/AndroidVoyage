package fr.esiea.ph.carnetdevoyages4.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by PH on 17/11/2015.
 */
public class TravelsContentProvider extends ContentProvider {

    //:::::::::::::::::::::::::://
    //:: URI d'exposition
    //:::::::::::::::::::::::::://
    public static final Uri CONTENT_URI = Uri.parse ( "content://fr.esiea.ph.carnetdevoyages" );
    // Constantes pour identifier les requetes
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    // Uri matcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher. NO_MATCH );
        uriMatcher .addURI( "fr.esiea.ph.carnetdevoyages" , "elements" , ALLROWS);
        uriMatcher .addURI( "fr.esiea.ph.carnetdevoyages" , "elements/#" , SINGLE_ROW);
    }

    //:::::::::::::::::::::::::://
    //:: Champs de la table Voyage
    //:::::::::::::::::::::::::://
    public static final String VOY_ID = "voy_id";
    public static final String VOY_TITRE = "voy_titre";
    public static final String VOY_DESTINATION = "voy_destination";
    public static final String VOY_DATE = "voy_date";
    public static final String VOY_USERID = "voy_userid";

    //:::::::::::::::::::::::::://
    //:: Champs de la table Etape
    //:::::::::::::::::::::::::://
    public static final String ETP_ID = "etp_id";
    public static final String ETP_VOYID = "etp_voyid";
    public static final String ETP_ETAPEID = "etp_etapeid";
    public static final String ETP_TITRE = "etp_titre";
    public static final String ETP_DESCRIPTION = "etp_description";
    public static final String ETP_LOCALISATION = "etp_localisation";
    public static final String ETP_PHOTOSID = "etp_photosid";
    public static final String ETP_DATE = "etp_date";

    //:::::::::::::::::::::::::://
    //:: Champs de la table User
    //:::::::::::::::::::::::::://
    public static final String USR_ID = "usr_id";
    public static final String USR_LOGIN = "usr_login";
    public static final String USR_PASSWORD = "usr_password";

    //:::::::::::::::::::::::::://
    //:: Champs de la table Photos
    //:::::::::::::::::::::::::://
    public static final String PHT_ID = "pht_id";
    public static final String PHT_1 = "pht_1";
    public static final String PHT_2 = "pht_2";
    public static final String PHT_3 = "pht_3";
    public static final String PHT_4 = "pht_4";
    public static final String PHT_5 = "pht_5";
    public static final String PHT_6 = "pht_6";

    private TravelsDBHelper myTravelsHelper;

    @Override
    public boolean onCreate() {
// construction/ouverture de la base de donnée
        myTravelsHelper = new TravelsDBHelper(
                getContext(),
                TravelsDBHelper. DATABASE_NAME ,
                null ,
                TravelsDBHelper. DATABASE_VERSION );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //ouverture de la base de donnée
        SQLiteDatabase db = myTravelsHelper.getWritableDatabase();
        //parametres de la requete SQL
        String groupBy = null ;
        String having = null ;
        //construction de la requete
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //ajout de la table
        queryBuilder.setTables(myTravelsHelper.DATABASE_TABLE1);
        queryBuilder.setTables(myTravelsHelper.DATABASE_TABLE2);
        queryBuilder.setTables(myTravelsHelper.DATABASE_TABLE3);
        queryBuilder.setTables(myTravelsHelper.DATABASE_TABLE4);
        // si requete de ligne on limite les retours à la premiere ligne
        switch ( uriMatcher .match(uri)){
            case SINGLE_ROW : String rowId = uri.getPathSegments().get(1);
                //ajout de la clause where, si on demande un element précis
                queryBuilder.appendWhere( VOY_ID + "=" + rowId);
                queryBuilder.appendWhere( ETP_ID + "=" + rowId);
                queryBuilder.appendWhere( USR_ID + "=" + rowId);
                queryBuilder.appendWhere( PHT_ID + "=" + rowId);
            default : break ;
        }
        //execution de la requete
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch ( uriMatcher .match(uri)){
            case ALLROWS : return
                    "vnd.android.cursor. dir /vnd.paad.elemental" ;
            case SINGLE_ROW : return
                    "vnd.android.cursor. item /vnd.paad.elemental" ;
            default : throw new IllegalArgumentException( "URI non reconnue" );
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //ouverture de la base de donnée
        SQLiteDatabase db = myTravelsHelper .getWritableDatabase();
        //hack column vide
        String nullColumnHack = null;
        //Insere les valeurs
        long id = db.insert(TravelsDBHelper.DATABASE_TABLE1 , nullColumnHack, contentValues);
        //si valeurs inseree
        if (id > 1){
            // construit l'uri de la ligne crée
            Uri instertedId = ContentUris. withAppendedId(CONTENT_URI, id);
            //notifie le changement des données
            getContext().getContentResolver().notifyChange(instertedId, null );
            return instertedId;
        }
        //sinon
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[]
            selectionArgs) {
        //ouverture de la base de donnée
        SQLiteDatabase db = myTravelsHelper .getWritableDatabase();
        // si requete de ligne on limite les retours à la premiere ligne
        switch ( uriMatcher .match(uri)){
            case SINGLE_ROW : String rowId =
                    uri.getPathSegments().get( 1 );
                selection = VOY_ID + "=" + rowId
                        + (!TextUtils. isEmpty(selection) ? " AND (" +
                        selection + ')' : "" );
            default : break;
        }
        if (selection == null ) {
            selection = "1";
        }
        // on effectue la suppression
        int deleteCount = db.delete(TravelsDBHelper. DATABASE_TABLE1,
                selection, selectionArgs);
        //notifie le changement des données
        getContext().getContentResolver().notifyChange(uri, null );
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String
            selection, String[] selectionArgs) {
        //ouverture de la base de donnée
        SQLiteDatabase db = myTravelsHelper .getWritableDatabase();
        // si requete de ligne on limite les retours à la premiere ligne
        switch ( uriMatcher .match(uri)){
            case SINGLE_ROW : String rowId =
                    uri.getPathSegments().get( 1 );
                selection = VOY_ID + "=" + rowId
                        + (!TextUtils. isEmpty (selection) ? " AND (" +
                        selection + ')' : "" );
            default : break;
        }
        if (selection == null ) {
            selection = "1";
        }
        // on effectue l'update
        int updateCount = db.update(TravelsDBHelper. DATABASE_TABLE1 ,
                contentValues, selection, selectionArgs);
        //notifie le changement des données
        getContext().getContentResolver().notifyChange(uri, null );
        return updateCount;
    }


    private static class TravelsDBHelper extends SQLiteOpenHelper {
        /*Cette fonction est appelée si aucune base n'existe et que le Helper
        doit la créer
        */

        // nom de la base de données
        private static final String DATABASE_NAME = "Carnetdevoyages";
        // nom des tables
        private static final String DATABASE_TABLE1 = "Voyage";
        private static final String DATABASE_TABLE2 = "Etape";
        private static final String DATABASE_TABLE3 = "User";
        private static final String DATABASE_TABLE4 = "Photos";
        // version de la base de données
        private static final int DATABASE_VERSION = 1;

        // script de création de la base de donnée
        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE1 + " (" + VOY_ID + " int primary key autoincrement, " +
                VOY_TITRE + " varchar(150) not null, " +
                VOY_DESTINATION + " varchar(200) not null, " +
                VOY_DATE + " date not null, " +
                VOY_USERID + " int foreign key not null); " +
                "create table " +
                DATABASE_TABLE2 + " (" + ETP_ID + " int primary key autoincrement, " +
                ETP_VOYID + " int foreign key not null, " +
                ETP_ETAPEID + " int not null, " +
                ETP_TITRE + " varchar(150) not null, " +
                ETP_DESCRIPTION + " varchar(1000) not null, " +
                ETP_LOCALISATION + " varchar(300) not null, " +
                ETP_PHOTOSID + " int, " +
                ETP_DATE + " date not null); " +
                "create table " +
                DATABASE_TABLE3 + " (" + USR_ID + " int primary key autoincrement, " +
                USR_LOGIN + " varchar(250) not null, " +
                USR_PASSWORD + " varchar(250) not null); " +
                "create table " +
                DATABASE_TABLE4 + " (" + PHT_ID + " int primary key autoincrement, " +
                PHT_1 + " varchar(250), " +
                PHT_2 + " varchar(250), " +
                PHT_3 + " varchar(250), " +
                PHT_4 + " varchar(250), " +
                PHT_5 + " varchar(250), " +
                PHT_6 + " varchar(250)); ";

        public TravelsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /*
        Cette fonction est appelée si aucune base n'existe et que le Helper
        doit la créer
        */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE );
        }

        /*
        Cette fonction est appelée si la base existe déjà et que la verison
        courante ne
        correspond pas à la version demandée
        */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
                              int newVersion) {
            Log. w("DATABASE", "Mise à jour de la version " +
                    oldVersion + " vers la verison " + newVersion +
                    ": toutes les données seront perdues.");
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + DATABASE_TABLE1);
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + DATABASE_TABLE2);
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + DATABASE_TABLE3);
            sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + DATABASE_TABLE4);
            onCreate(sqLiteDatabase);
        }
    }
}
