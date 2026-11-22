package accademiaMusicale;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Random;

public class AccademiaMusicale{
    private ArrayList<Corso> corsi;
    private Registro registro;/*registra gli eventi*/
    private LocalDateTime dataCorrente;

    public AccademiaMusicale(){
    this.corsi=new ArrayList<>();
    this.registro=new Registro();
    this.dataCorrente=LocalDateTime.now();

    }
    private void controllaNonNullo(Corso corso) {
        if (corso == null) {
            throw new IllegalArgumentException("Il corso non può essere nullo");
        }
    }

    private void controllaCorsoEsistente(Corso corso) {
        controllaNonNullo(corso);
        if (!corsi.contains(corso)) {
            throw new IllegalArgumentException("Corso inesistente");
        }
    }
    public LocalDateTime generaData(){
        Random rand=new Random();
        LocalDateTime data_iniziocorso = this.dataCorrente.plusDays(rand.nextInt(14, 45));;/*aggiunge i giorni dallla data di oggi (45=44-14-->30 giorni data limite di inizio corso)*/
        return data_iniziocorso;


    }

    public void aggiungiCorso(Corso corso){

        controllaNonNullo(corso);
        if (corso.isCancellato()) {
            throw new IllegalArgumentException("Impossibile aggiungere un corso già cancellato");
        }
        if (corsi.contains(corso)) {
            throw new IllegalArgumentException("Corso già presente");
        }
        corsi.add(corso);
        registro.scriviRegistro("Aggiunto nuovo corso: " + corso.toString(), this.dataCorrente);


    }
    public boolean cancellaCorso(Corso corso){
            controllaCorsoEsistente(corso);
            ArrayList<Allievo> allieviIscritti = corso.getAllieviIscritti();
            for (int i = 0; i < allieviIscritti.size(); i++) {
                Allievo a = allieviIscritti.get(i);
                a.setIscritto(false);/*cosi un allievo si può iscrivere ad un altro corso*/
            }
            corso.setDisponibilita(false);
            corso.setCancellato(true);
            corsi.remove(corso);
        registro.scriviRegistro("Cancellato corso e liberati gli allievi iscritti.", this.dataCorrente);
            return true;
    }
    public void apriCorso(Corso corso){
        controllaCorsoEsistente(corso);
        if(corso.isCancellato()){
            throw new IllegalArgumentException("il corso era presente nella lista ma è stato cancellato");
        }else{

            corso.setDisponibilita(true);
            registro.scriviRegistro("Aperte le iscrizioni per un corso.", this.dataCorrente);
        }
    }
    public void chiudiCorso(Corso corso){
        controllaCorsoEsistente(corso);
        if(corso.isCancellato()){
            throw new IllegalArgumentException("il corso era presente nella lista ma è stato cancellato");
        }else{
            corso.setDisponibilita(false);
            registro.scriviRegistro("Chiuse le iscrizioni per un corso.", this.dataCorrente);
        }
    }
    public ArrayList getCorsi(){
        return this.corsi;
    }
    public void passaAlGiornoSuccessivo() {
        this.dataCorrente = this.dataCorrente.plusDays(1);
        controllaScadenzeCorsi();
        registro.scriviRegistro("Iniziato nuovo giorno di simulazione", this.dataCorrente);
    }
    public void controllaScadenzeCorsi() {
        for (int i = 0; i < corsi.size(); i++) {
            Corso c = corsi.get(i);
            if (c.getDisponibilita() && c.getInizio() != null) {
                if (dataCorrente.isAfter(c.getInizio().plusDays(10))) {
                    chiudiCorso(c);
                }
            }
        }
    }
    public Registro getRegistro() {
        return this.registro;
    }
    public LocalDateTime getDataCorrente() { return this.dataCorrente; }
    @Override
    public String toString() {
        if (corsi.isEmpty()) {
            return "Nessun corso presente nell'accademia.";
        }
        String listaCorsi = "Elenco corsi dell'accademia:\n";
        for (int i = 0; i < corsi.size(); i++) {
            listaCorsi += (i + 1) + ") " + corsi.get(i).toString() + "\n";
        }
        return listaCorsi;
    }
}
