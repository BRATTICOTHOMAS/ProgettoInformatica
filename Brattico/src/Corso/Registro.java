package Corso;
import java.util.ArrayList;
import java.time.LocalDateTime;
public class Registro {
    private ArrayList<String> registrazioni;

    public Registro() {
        this.registrazioni = new ArrayList<>();
    }
    public void scriviRegistro(String messaggio, LocalDateTime dataOra) {
        if (messaggio == null || messaggio.trim().isEmpty()) {
            throw new IllegalArgumentException("Il messaggio del registro non può essere vuoto");
        }

    }public ArrayList<String> getRegistrazioni() {
        return this.registrazioni;
    }@Override
    public String toString() {
        if (registrazioni.isEmpty()) {
            return "Nessuna operazione registrata nel log.";
        }
        String risultato = "---  REGISTRO ATTIVITÀ ACCADEMIA ---\n";
        for (int i = 0; i < registrazioni.size(); i++) {
            risultato += registrazioni.get(i) + "\n";
        }
        return risultato;
    }


}
