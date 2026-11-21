package accademiaMusicale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        AccademiaMusicale accademia = new AccademiaMusicale();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int scelta = -1;

        while (scelta != 0) {
            stampaMenu(accademia, formatoData);
            scelta = leggiIntero("Scegli un'opzione: ");
            switch (scelta) {
                case 1:
                    aggiungiCorso(accademia);
                    break;
                case 2:
                    gestisciAperturaChiusuraCorso(accademia);
                    break;
                case 3:
                    cancellaCorso(accademia);
                    break;
                case 4:
                    iscriviAllievo(accademia);
                    break;
                case 5:
                    rimuoviAllievo(accademia);
                    break;
                case 6:
                    visualizzaCorsiEDettagli(accademia);
                    break;
                case 7:
                    avanzaGiornoSimulazione(accademia, formatoData);
                    simulaEventiCasuali(accademia);
                    break;
                case 8:
                    visualizzaRegistro(accademia);
                    break;
                case 9:
                    visualizzaStatistiche(accademia);
                    break;
                case 0:
                    IO.println("Chiusura programma. Arrivederci!");
                    break;
                default:
                    IO.println("Scelta non valida! Inserisci un numero da 0 a 8.");
                    break;
            }
        }
    }

    private static void stampaMenu(AccademiaMusicale accademia, DateTimeFormatter formatoData) {
        IO.println("\n==========================================");
        IO.println("   ACCADEMIA MUSICALE - MENU PRINCIPALE");
        IO.println("   Data simulazione: " + accademia.getDataCorrente().format(formatoData));
        IO.println("==========================================");
        IO.println("1 - Aggiungi nuovo corso");
        IO.println("2 - Apri / Chiudi iscrizioni di un corso");
        IO.println("3 - Cancella un corso");
        IO.println("4 - Iscrivi allievo a un corso");
        IO.println("5 - Rimuovi allievo da un corso");
        IO.println("6 - Visualizza corsi e dettagli allievi");
        IO.println("7 - Passa al giorno successivo (controlla scadenze)");
        IO.println("8 - Visualizza registro attività");
        IO.println("9 - Visualizza statistiche");
        IO.println("0 - Esci");
    }

    private static void aggiungiCorso(AccademiaMusicale accademia) {
        IO.println("\nTipi di corso disponibili:");
        TipoCorso[] tipi = TipoCorso.values();
        for (int i = 0; i < tipi.length; i++) {
            IO.println(" - " + tipi[i]);
        }

        boolean creato = false;
        while (!creato) {
            try {
                String tipoScelto = IO.readln("Inserisci il tipo di corso da creare: ");
                Corso nuovoCorso = new Corso(tipoScelto);

                Random rand = new Random();
                LocalDateTime dataCalcolata = accademia.generaData();
                nuovoCorso.setInizio(dataCalcolata);
                int durataMesiRandom = rand.nextInt(4,7);
                nuovoCorso.setFine(dataCalcolata.plusMonths(durataMesiRandom));

                accademia.aggiungiCorso(nuovoCorso);
                accademia.apriCorso(nuovoCorso);

                IO.println("Corso '" + tipoScelto.trim().toLowerCase() + "' aggiunto e aperto con successo!");
                creato = true;
            } catch (IllegalArgumentException e) {
                IO.println("Errore: " + e.getMessage() + ". Riprova.");
            }
        }
    }

    private static void gestisciAperturaChiusuraCorso(AccademiaMusicale accademia) {
        Corso corso = selezionaCorso(accademia);
        if (corso == null) {
            return;
        }

        if (corso.getDisponibilita()) {
            IO.println("Stato attuale iscrizioni: APERTE");
        } else {
            IO.println("Stato attuale iscrizioni: CHIUSE");
        }

        IO.println("1 - Apri iscrizioni");
        IO.println("2 - Chiudi iscrizioni");
        int azione = leggiIntero("Scelta: ");

        try {
            if (azione == 1) {
                accademia.apriCorso(corso);
                IO.println("Iscrizioni aperte per il corso.");
            } else if (azione == 2) {
                accademia.chiudiCorso(corso);
                IO.println("Iscrizioni chiuse per il corso.");
            } else {
                IO.println("Scelta non valida.");
            }
        } catch (IllegalArgumentException e) {
            IO.println("Errore: " + e.getMessage());
        }
    }

    private static void cancellaCorso(AccademiaMusicale accademia) {
        Corso corso = selezionaCorso(accademia);
        if (corso == null) {
            return;
        }

        try {
            if (accademia.cancellaCorso(corso) && corso.isCancellato()) {
                IO.println("Corso cancellato e allievi liberati con successo!");
            }
        } catch (IllegalArgumentException e) {
            IO.println("Errore durante la cancellazione: " + e.getMessage());
        }
    }

    private static void iscriviAllievo(AccademiaMusicale accademia) {
        Corso corsoScelto = selezionaCorso(accademia);
        if (corsoScelto == null) {
            return;
        }

        if (corsoScelto.isCancellato() || !corsoScelto.getDisponibilita()) {
            IO.println("Impossibile iscrivere allievi: il corso è chiuso o cancellato.");
            return;
        }

        boolean iscritto = false;
        while (!iscritto) {
            try {
                String nome = IO.readln("Inserisci il nome dell'allievo (solo lettere, min. 3 caratteri): ").trim();
                if (!nome.matches("[a-zA-Z]+")) {
                    throw new IllegalArgumentException("Il nome deve contenere solo lettere senza spazi.");
                }

                int eta = leggiIntero("Inserisci l'età dell'allievo (18-130): ");
                String tipoCorsoAlunno = IO.readln("Conferma il tipo di corso dell'allievo: ");
                TipoCorso tipoInserito = TipoCorso.valueOf(tipoCorsoAlunno.toLowerCase());

                if (tipoInserito != corsoScelto.getTipoCorso()) {
                    throw new IllegalArgumentException("Il corso confermato (" + tipoInserito
                            + ") non corrisponde al corso selezionato (" + corsoScelto.getTipoCorso() + ").");
                }


                verificaConflittiOrariAllievo(accademia, nome, corsoScelto);

                boolean esito = corsoScelto.iscriviAllievo(nome, eta, tipoCorsoAlunno);
                if (esito) {
                    ArrayList<Allievo> iscritti = corsoScelto.getAllieviIscritti();
                    Allievo appenaIscritto = iscritti.get(iscritti.size() - 1);
                    appenaIscritto.setIscritto(true);

                    IO.println("Allievo " + appenaIscritto.getNome() + " iscritto! ("
                            + corsoScelto.getnIscritti() + " iscritti totali)");
                } else {
                    IO.println("Impossibile iscrivere: numero massimo di iscritti raggiunto.");
                }
                iscritto = true;

            } catch (IllegalArgumentException e) {
                IO.println("Errore nei dati inseriti: " + e.getMessage() + ". Riprova.");
            }
        }
    }

    private static void verificaConflittiOrariAllievo(AccademiaMusicale accademia, String nomeAllievo, Corso nuovoCorso) {
        int totaleCorsi = accademia.getCorsi().size();

        for (int i = 0; i < totaleCorsi; i++) {
            Corso c = (Corso) accademia.getCorsi().get(i);
            if (!c.isCancellato() && c != nuovoCorso) {
                ArrayList<Allievo> listaAllievi = c.getAllieviIscritti();
                for (int j = 0; j < listaAllievi.size(); j++) {
                    Allievo a = listaAllievi.get(j);
                    if (a.getNome().equals(nomeAllievo) && a.isIscritto()) {
                        if (c.getInizio() != null && nuovoCorso.getInizio() != null) {
                            a.ControlloIscritti(c.getInizio(), nuovoCorso.getInizio());
                        }
                    }
                }
            }
        }
    }

    private static void rimuoviAllievo(AccademiaMusicale accademia) {
        Corso corsoTarget = selezionaCorso(accademia);
        if (corsoTarget == null) {
            return;
        }

        if (corsoTarget.getnIscritti() == 0) {
            IO.println("Non ci sono allievi iscritti in questo corso.");
            return;
        }

        IO.println("Allievi attualmente iscritti:");
        ArrayList<Allievo> iscritti = corsoTarget.getAllieviIscritti();
        for (int i = 0; i < iscritti.size(); i++) {
            Allievo a = iscritti.get(i);
            IO.println(" - " + a.getNome() + " (" + a.getEta() + " anni, " + a.getTc() + ")");
        }

        try {
            String nomeDaRimuovere = IO.readln("Inserisci il nome esatto dell'allievo da rimuovere: ");
            if (corsoTarget.rimuoviAllievo(nomeDaRimuovere)) {
                IO.println("Allievo rimosso con successo!");
            } else {
                IO.println("Allievo non trovato in questo corso.");
            }
        } catch (IllegalArgumentException e) {
            IO.println("Errore: " + e.getMessage());
        }
    }

    private static void visualizzaCorsiEDettagli(AccademiaMusicale accademia) {
        IO.println("\n--- ELENCO CORSI ACCADEMIA ---");
        IO.println(accademia.toString());

        int totaleCorsi = accademia.getCorsi().size();
        for (int i = 0; i < totaleCorsi; i++) {
            Corso c = (Corso) accademia.getCorsi().get(i);

            String statoDisponibile;
            if (c.getDisponibilita()) {
                statoDisponibile = "Sì";
            } else {
                statoDisponibile = "No";
            }

            IO.println("Dettaglio Corso N°" + i + " | Iscritti: " + c.getnIscritti()
                    + " | Disponibile: " + statoDisponibile);
            ArrayList<Allievo> iscritti = c.getAllieviIscritti();
            for (int j = 0; j < iscritti.size(); j++) {
                Allievo a = iscritti.get(j);
                IO.println("   -> " + a.getNome() + " | Età: " + a.getEta()
                        + " | Corso: " + a.getTc() + " | Attivo: " + a.isIscritto());
            }
        }
    }


    private static void avanzaGiornoSimulazione(AccademiaMusicale accademia, DateTimeFormatter formatoData) {
        int giorni = leggiIntero("Di quanti giorni vuoi avanzare la simulazione? ");
        if (giorni <= 0) {
            IO.println("Inserisci un numero di giorni maggiore di 0.");
            return;
        }

        for (int i = 0; i < giorni; i++) {
            accademia.passaAlGiornoSuccessivo();
        }
        accademia.controllaScadenzeCorsi();
        IO.println("Simulazione aggiornata al: " + accademia.getDataCorrente().format(formatoData));
    }

    private static void visualizzaRegistro(AccademiaMusicale accademia) {
        Registro reg = accademia.getRegistro();
        IO.println("\nTotale operazioni nel registro: " + reg.getRegistrazioni().size());
        IO.println(reg.toString());
    }

    private static Corso selezionaCorso(AccademiaMusicale accademia) {
        int totaleCorsi = accademia.getCorsi().size();
        if (totaleCorsi == 0) {
            IO.println("Nessun corso presente nell'accademia.");
            return null;
        }

        IO.println("\nCorsi disponibili:");
        for (int i = 0; i < totaleCorsi; i++) {
            Corso c = (Corso) accademia.getCorsi().get(i);
            IO.println((i + 1) + " -> " + c.toString());
        }

        int indice = leggiIntero("Seleziona l'indice del corso: ") - 1;
        if (indice < 0 || indice >= totaleCorsi) {
            IO.println("Indice corso non valido.");
            return null;
        }
        return (Corso) accademia.getCorsi().get(indice);
    }

    private static int leggiIntero(String messaggio) {
        while (true) {
            try {
                return Integer.parseInt(IO.readln(messaggio).trim());
            } catch (IllegalArgumentException e) {
                IO.println("Errore: devi inserire un numero intero valido.");
            }
        }
    }

    private static void visualizzaStatistiche(AccademiaMusicale accademia){
        ArrayList<Corso> corsi = accademia.getCorsi();
        ArrayList<String> log = accademia.getRegistro().getRegistrazioni();

        if (corsi.isEmpty()) {
            IO.println("Nessun corso attivo al momento per calcolare le statistiche.");
            IO.println("Totale eventi registrati nel log: " + log.size());
            return;
        }

        int corsiAperti = 0;
        int corsiChiusi = 0;
        int totaleAllievi = 0;
        int sommaEta = 0;

        for (int i = 0; i < corsi.size(); i++) {
            Corso c = corsi.get(i);

            if (c.getDisponibilita()){
                corsiAperti += 1;
            }else{
                corsiChiusi += 1;
            }

            ArrayList<Allievo> allievi = c.getAllieviIscritti();
            totaleAllievi += allievi.size();
            for (int j = 0; j < allievi.size(); j++) {
                sommaEta += allievi.get(j).getEta();
            }
        }

        IO.println("Corsi totali: " + corsi.size() + " (Aperti: " + corsiAperti + " | Chiusi: " + corsiChiusi + ")");
        IO.println("Totale allievi iscritti:   " + totaleAllievi);
        if(sommaEta > 0 && totaleAllievi > 0){
            float etaMedia = (float) sommaEta / totaleAllievi;
            IO.println(String.format("Media eta allievi iscritti : " + etaMedia));
        }else{
            IO.println("Allievi non presenti");
        }

    }



    private static void simulaEventiCasuali(AccademiaMusicale accademia) {
        Random rand = new Random();
        String[] nomiCasuali = {"Marco", "Giulia", "Luca", "Sofia", "Matteo", "Chiara", "Alessandro", "Elena", "Davide", "Sara"};
        TipoCorso[] tipiCorso = TipoCorso.values();

        int numeroEventi = rand.nextInt(1, 4); //
        IO.println("\n--- EVENTI CASUALI DI OGGI (" + numeroEventi + ") ---");

        for (int i = 0; i < numeroEventi; i++) {
            int tipoEvento = rand.nextInt(4);

            try {
                if (tipoEvento == 0 || accademia.getCorsi().isEmpty()) {
                    TipoCorso tipoScelto = tipiCorso[rand.nextInt(tipiCorso.length)];
                    Corso nuovoCorso = new Corso(tipoScelto.name());
                    LocalDateTime inizio = accademia.generaData();
                    nuovoCorso.setInizio(inizio);
                    nuovoCorso.setFine(inizio.plusMonths(3));

                    accademia.aggiungiCorso(nuovoCorso);
                    accademia.apriCorso(nuovoCorso);
                    IO.println("Aperto nuovo corso di " + tipoScelto + ".");

                } else if (tipoEvento == 1) {
                    ArrayList<Corso> listaCorsi = accademia.getCorsi();
                    Corso corsoCasuale = listaCorsi.get(rand.nextInt(listaCorsi.size()));

                    if (!corsoCasuale.isCancellato() && corsoCasuale.getDisponibilita()) {
                        String nome = nomiCasuali[rand.nextInt(nomiCasuali.length)];
                        int eta = rand.nextInt(18, 65);
                        String tipo = corsoCasuale.getTipoCorso().name();

                        verificaConflittiOrariAllievo(accademia, nome, corsoCasuale);
                        if (corsoCasuale.iscriviAllievo(nome, eta, tipo)) {
                            ArrayList<Allievo> iscritti = corsoCasuale.getAllieviIscritti();
                            iscritti.get(iscritti.size() - 1).setIscritto(true);
                            accademia.getRegistro().scriviRegistro(
                                    "Iscritto allievo casuale " + nome + " al corso " + tipo,
                                    accademia.getDataCorrente()
                            );
                            IO.println(nome + " (" + eta + " anni) si è iscritto al corso di " + tipo + ".");
                        }
                    }

                } else if (tipoEvento == 2) {
                    ArrayList<Corso> listaCorsi = accademia.getCorsi();
                    Corso corsoCasuale = listaCorsi.get(rand.nextInt(listaCorsi.size()));

                    if (corsoCasuale.getDisponibilita()) {
                        accademia.chiudiCorso(corsoCasuale);
                        IO.println("Chiuse le iscrizioni per il corso di " + corsoCasuale.getTipoCorso() + ".");
                    }

                } else if (tipoEvento == 3) {
                    ArrayList<Corso> listaCorsi = accademia.getCorsi();
                    if (listaCorsi.size() > 1) {
                        Corso corsoCasuale = listaCorsi.get(rand.nextInt(listaCorsi.size()));
                        TipoCorso tipo = corsoCasuale.getTipoCorso();
                        accademia.cancellaCorso(corsoCasuale);
                        IO.println("Il corso di " + tipo + " è stato cancellato.");
                    }
                }
            } catch (IllegalArgumentException e) {
                IO.println("Evento saltato: " + e.getMessage());
            }
        }
        IO.println("-----------------------------------");


    }
}