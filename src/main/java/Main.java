import Controller.GameEngineController;
import Model.ContinentModel;
import Model.CountryModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        HashMap<Integer, CountryModel> l_countries = new HashMap<>();
        ArrayList<ContinentModel> l_continents = new ArrayList<>();

        CountryModel a = new CountryModel(1,"India");
        CountryModel b = new CountryModel(2,"Egypt");
        CountryModel c = new CountryModel(3,"Canada");
        CountryModel a1 = new CountryModel(4,"Canada1");
        CountryModel a2 = new CountryModel(5,"Canada2");
        CountryModel a3 = new CountryModel(6,"Canada2");
        CountryModel a4 = new CountryModel(7,"Canada2");
        CountryModel a5 = new CountryModel(8,"Canada2");
        CountryModel a6 = new CountryModel(9,"Canada2");
        CountryModel a7 = new CountryModel(10,"Canada2");
        CountryModel a8 = new CountryModel(11,"Canada2");
        CountryModel a9 = new CountryModel(12,"Canada2");

        l_countries.put(1, a);
        l_countries.put(2, b);
        l_countries.put(3, c);
        l_countries.put(4, a1);
        l_countries.put(5, a2);
        l_countries.put(6, a3);
        l_countries.put(7, a4);
        l_countries.put(8, a5);
        l_countries.put(9, a6);
        l_countries.put(10, a7);
        l_countries.put(11, a8);
        l_countries.put(12, a9);

        ContinentModel c1 = new ContinentModel("Asia",7);
        ContinentModel c2 = new ContinentModel("Australia",8);

        c1.addCountry(a);
        c1.addCountry(b);
        c1.addCountry(c);
        c1.addCountry(a1);
        c1.addCountry(a2);
        c1.addCountry(a3);
        c1.addCountry(a4);
        c1.addCountry(a5);
        c2.addCountry(a6);
        c2.addCountry(a7);
        c2.addCountry(a8);
        c1.addCountry(a9);

        l_continents.add(c1);
        l_continents.add(c2);

        GameEngineController l_gameEngine = new GameEngineController(l_countries, l_continents);

        l_gameEngine.addPlayer("Aman");
        l_gameEngine.addPlayer("Mazen");
        l_gameEngine.addPlayer("Akshat");

        l_gameEngine.assignCountries();
        /*l_gameEngine.assignReinforcements();
        l_gameEngine.issueOrders();*/
        l_gameEngine.run();
    }
}
