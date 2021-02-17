import Controller.GameEngineController;
import Model.ContinentModel;
import Model.CountryModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        HashMap<String, CountryModel> l_countries = new HashMap<>();
        ArrayList<ContinentModel> l_continents = new ArrayList<>();

        CountryModel a = new CountryModel(1,"India");
        CountryModel b = new CountryModel(2,"Egypt");
        CountryModel c = new CountryModel(3,"Canada");
        CountryModel a1 = new CountryModel(4,"Canada1");
        CountryModel a2 = new CountryModel(5,"Canada2");
        CountryModel a3 = new CountryModel(6,"Canada3");
        CountryModel a4 = new CountryModel(7,"Canada4");
        CountryModel a5 = new CountryModel(8,"Canada5");
        CountryModel a6 = new CountryModel(9,"Canada6");
        CountryModel a7 = new CountryModel(10,"Canada7");
        CountryModel a8 = new CountryModel(11,"Canada8");
        CountryModel a9 = new CountryModel(12,"Canada9");

        l_countries.put(a.getName(), a);
        l_countries.put(b.getName(), b);
        l_countries.put(c.getName(), c);
        l_countries.put(a1.getName(), a1);
        l_countries.put(a2.getName(), a2);
        l_countries.put(a3.getName(), a3);
        l_countries.put(a4.getName(), a4);
        l_countries.put(a5.getName(), a5);
        l_countries.put(a6.getName(), a6);
        l_countries.put(a7.getName(), a7);
        l_countries.put(a8.getName(), a8);
        l_countries.put(a9.getName(), a9);

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
