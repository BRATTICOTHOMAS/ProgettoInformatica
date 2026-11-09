public class Allievo {
    private String nome;
    private int eta;
    private boolean iscritto;
    public TipoCorso tc;


    Allievo(String n, int e, String t){
        controlli(n,e,t);
        iscritto = false;
    }

    private void controlli(String nome, int eta, String tpc){

        if (nome.isEmpty()){
            throw new IllegalArgumentException("NOME VUOTO");
        } else if (nome.length() < 3) {
            throw new IllegalArgumentException("NOME TROPPO CORTO");
        }else{
            this.nome = nome;
        }

        if (eta < 18 || eta > 130){
            throw new IllegalArgumentException("ETÀ INSERITA NON VALIDA");
        }else{
            this.eta = eta;
        }

        this.tc = TipoCorso.valueOf(tpc.toLowerCase());
    }


    public void iscriviti(){

    }


    @Override
    public String toString(){
        return "Nome: " + this.nome + " Età: " + this.eta + " Tipo Corso: " + this.tc;
    }
}
