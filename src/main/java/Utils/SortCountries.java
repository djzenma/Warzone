package Utils;

import Model.CountryModel;

import java.util.Comparator;

public class SortCountries {

    public static class SortCountriesDescending implements Comparator<CountryModel> {
        @Override
        public int compare(CountryModel o1, CountryModel o2) {
            return o2.getArmies() - o1.getArmies();
        }
    }

    public static class SortCountriesAscending implements Comparator<CountryModel> {
        @Override
        public int compare(CountryModel o1, CountryModel o2) {
            return o1.getArmies() - o2.getArmies();
        }
    }
}
