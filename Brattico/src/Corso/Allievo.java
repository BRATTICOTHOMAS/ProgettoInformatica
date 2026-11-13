package Corso;

import java.time.LocalDateTime;

public class Allievo {
    private String nome;
    private int eta;
    private boolean iscritto;
    public TipoCorso tc;


    Allievo(String nome, int eta, String tipoCorso){
        controlli(nome,eta,tipoCorso);
        iscritto = false;
    }

    private void controlli(String nome, int eta, String tpc){

        if (nome.isEmpty()){
            throw new IllegalArgumentException("NOME VUOTO");
        } else if (nome.length() < 3) {
            throw new IllegalArgumentException("NOME TROPPO CORTO");
        }else{
            if(nome.matches("[a-zA-Z]+"))
            this.nome = nome;
        }

        if (eta < 18 || eta > 130){
            throw new IllegalArgumentException("ETÀ INSERITA NON VALIDA");
        }else{
            this.eta = eta;
        }

        this.tc = TipoCorso.valueOf(tpc.toLowerCase());
    }


    public String getNome(){
        return this.nome;
    }

    public int getEta(){
        return this.eta;
    }

    public TipoCorso getTc(){
        return this.tc;
    }

    public boolean isIscritto(){
        return this.iscritto;
    }


    public void setIscritto(boolean iscritto, String t) {

        if (iscritto) {

            if (t == null) {

                throw new IllegalArgumentException("Il tipo corso non può essere null");

            }

            this.tc = TipoCorso.valueOf(t.trim().toLowerCase());

        } else {

            this.tc = null;

        }

    }


    public void ControlloIscritti(LocalDateTime dataInizio1,LocalDateTime dataInizio2 ){
        if(dataInizio1 == dataInizio2){
            throw new IllegalArgumentException("SEI ISCRITTO A DUE CORSI IN CONTEMPORANEA");
        }

    }



    @Override
    public String toString(){
        return "\n Nome: " + this.nome + " Età: " + this.eta + " TipoCorso: " + this.tc;
    }
}
