package Main;

import csv_handling.CSV_Reader;

public class Testing
{
    static CSV_Reader data =  new CSV_Reader();

    public static void main(String[]args)
    {
        data.MoveDataReader();
        data.PalmonDataReader();
        data.PalmonMoveDataReader();
    }
}
