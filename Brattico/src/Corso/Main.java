package Corso;

import java.time.LocalDateTime;

public class Main{
    public static void main(String[] args) {



        AccademiaMusicale accademiaMusicale=new AccademiaMusicale();
        accademiaMusicale.aggiungiCorso(new Corso("pianista"));
        IO.println(accademiaMusicale.toString());

    }
}