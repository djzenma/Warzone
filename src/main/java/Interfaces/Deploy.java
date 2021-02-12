package Interfaces;

import Model.CountryModel;

import java.util.HashMap;

public interface Deploy {
    public void execute(HashMap<Integer, CountryModel> p_countries);
}
