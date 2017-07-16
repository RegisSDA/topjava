package ru.javawebinar.topjava.DAO;

/**
 * Created by MSI on 15.07.2017.
 */
public class MealDAOFactory {

    private static MealDAO dBService;
    public static synchronized MealDAO getMealDAO(){
        if (dBService==null){
            dBService=new MealDAOImplInMemoryDB();
        }
        return dBService;
    }
}
