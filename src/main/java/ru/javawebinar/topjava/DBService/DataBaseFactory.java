package ru.javawebinar.topjava.DBService;

/**
 * Created by MSI on 15.07.2017.
 */
public class DataBaseFactory {

    private static DBService dBService;
    public static synchronized DBService getDataBase(){
        if (dBService==null){
            dBService=new InMemoryDataBase();
        }
        return dBService;
    }
}
