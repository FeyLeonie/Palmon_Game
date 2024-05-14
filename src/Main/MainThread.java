package Main;

import csv_handling.CSV_Reader;
import csv_handling.CSV_Searching;

public class MainThread
{
     public static void main (String[] args)
     {
         CSV_Reader data = new CSV_Reader();
         CSV_Searching searching = new CSV_Searching(data);

         data.start();
         searching.start();
     }
}
