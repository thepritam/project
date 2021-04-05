package com.example.ServicePortal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class model {

    private static HashMap<Integer,Double> value= new HashMap<>();
    private static ArrayList<String> product = new ArrayList<>();
    private static ArrayList<String> quant = new ArrayList<>();

    public static void setPair(int id,double price)
    {
        value.put(id,price);
    }

    public static void clearmap()
    {
        value.clear();
    }

    public static void setprod(String prod)
    {
        product.add(prod);
    }

    public static void setqty(String qty)
    {
        quant.add(qty);
    }

    public static HashMap<Integer,Double> getMap()
    {
        return value;
    }

    public static ArrayList<String> getprod() { return product; }

    public static ArrayList<String> getquant() { return quant; }
}
