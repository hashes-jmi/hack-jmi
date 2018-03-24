package com.example.tanishqsaluja.beep.DB;

/**
 * Created by tanishqsaluja on 24/3/18.
 */

public class TableContract {
    public static final String TABLE_NAME="notes";
    public static String ID="id";
    public static final String TITLE="title";
    public static final String DESCRIPTION="description";
    public static final String ISDONE="isdone";
    public static final String HOUR=" hour ";
    public static final String MINUTE=" minute ";
    public static final String COMMA=" , ";
    public static final String LBR=" ( ";
    public static final String RBR=" ) ";
    public static final String TYPE_INT=" INTEGER ";
    public static final String TYPE_TEXT=" TEXT ";
    public static final String IPK=" INTEGER PRIMARY KEY ";
    public static final String TERMINATE=" ; ";
    public static final String SELECT=" SELECT ";
    public static final String ALL=" * ";
    public static final String FROM = " FROM ";
    public static final String WHERE = " WHERE ";

    public static final String CREATE_TABLE_ALTER=" CREATE TABLE "+TABLE_NAME+LBR
            +ID+IPK+COMMA+TITLE+TYPE_TEXT+COMMA+DESCRIPTION+TYPE_TEXT+COMMA+ISDONE
            +TYPE_INT+COMMA+HOUR+TYPE_INT+COMMA+MINUTE+TYPE_INT+RBR+TERMINATE;

    public static final String GET_ALL_NOTES=SELECT+ALL+FROM+TABLE_NAME+TERMINATE;
}
