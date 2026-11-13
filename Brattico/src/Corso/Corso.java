package Corso;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Corso {
    private TipoCorso tipoCorso;
    private ArrayList<Allievo> allieviIscritti;
    private boolean disponibilita;
    private int nIscritti;
    private int nIscrittiMax;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private boolean cancellato;

    public Corso(String tipo) {
        controlli(tipo);
        this.nIscrittiMax = 15;
        this.nIscritti = 0;
        this.disponibilita = true;
        this.allieviIscritti = new ArrayList<>();
        this.cancellato = false;
    }
    private void controlli(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Il tipo corso non può essere null");
        }
        try {
            this.tipoCorso = TipoCorso.valueOf(tipo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo corso non esistente!!");
        }
    }
    public LocalDateTime getInizio() {
        return inizio;
    }
    public boolean iscriviAllievo(String nome, int età, String tipoCorso) {
        if (cancellato) {
            throw new IllegalArgumentException("IMPOSSIBILE AGGIUNGERE L'ALLIEVO: IL CORSO È STATO CANCELLATO!!");
        }
        if (nIscritti >= nIscrittiMax) {
            disponibilita = false;
            return false;
        }
        Allievo nuovoAllievo = new Allievo(nome, età, tipoCorso);
        allieviIscritti.add(nuovoAllievo);
        nIscritti += 1;
        disponibilita = (nIscritti < nIscrittiMax);
        return true;
    }
    public boolean rimuoviAllievo(String nome) {
        if (cancellato) {
            throw new IllegalArgumentException("IMPOSSIBILE RIMUOVERE L'ALLIEVO: IL CORSO È STATO CANCELLATO!!");
        }
        if (nome == null) {
            return false;
        }
        for (int i = 0; i < allieviIscritti.size(); i++) {
            if (allieviIscritti.get(i).getNome().equals(nome.trim())) {
                allieviIscritti.remove(i);
                nIscritti -= 1;
                disponibilita = true;
                return true;
            }
        }
        return false;
    }
    public int getnIscritti() {
        return nIscritti;
    }
    public String getAllieviIscritti() {
        if (cancellato) {
            throw new IllegalArgumentException("IL CORSO è STATO CANCELLATO!!");
        }
        String listaAllievi = "";
        for (int i = 0; i < allieviIscritti.size(); i++) {
            listaAllievi += allieviIscritti.get(i).toString() + "\n";
        }
        return listaAllievi;
    }
    public boolean getDisponibilita() {
        return disponibilita;
    }
    public void setDisponibilita(boolean cambiaDisponibilita) {
        this.disponibilita = cambiaDisponibilita;
    }
    public void setFine(LocalDateTime fine) {
        this.fine = fine;
    }
    public void setCancellato(boolean cancellato) {
        this.cancellato = cancellato;
    }
    public boolean isCancellato() {
        return cancellato;
    }

    @Override
    public String toString() {
        return "Corso:"+
                "tipoCorso=" + tipoCorso +
                ", allieviIscritti=" + allieviIscritti +
                ", disponibilita=" + disponibilita +
                ", nIscritti=" + nIscritti +
                ", nIscrittiMax=" + nIscrittiMax +
                ", inizio=" + inizio +
                ", fine=" + fine +
                ", cancellato=" + cancellato ;

    }
}