package accademiaMusicale;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        AccademiaMusicale accademia = new AccademiaMusicale();

        int scelta = -1;
        while (scelta != 0) {
            IO.println("\n--- MENU ACCADEMIA MUSICALE ---");
            IO.println("0 - Esci");
            IO.println("1 - Aggiungi Corso");
            IO.println("2 - Aggiungi alunno al corso");
            IO.println("3 - Rimuovi alunno");
            IO.println("4 - Rimuovi Corso");
            IO.println("5 - Vedi registro");
            IO.println("6 - Vedi tutti i corsi");

            try {
                scelta = Integer.parseInt(IO.readln("Scegli: "));
            } catch (IllegalArgumentException e) {
                IO.println("Errore: devi inserire un numero!");
                scelta=-1;
            }

            switch (scelta) {
                case 0:
                    IO.println("Arrivederci!");
                    break;

                case 1:
                    IO.println("Tipi di corso disponibili:");
                    TipoCorso[] tipi = TipoCorso.values();
                    for (int i = 0; i < tipi.length; i++) {
                        IO.println("- " + tipi[i]);
                    }

                    boolean corsoCorretto = false;
                    while (!corsoCorretto) {
                        try {
                            String tipoScelto = IO.readln("Inserisci il nome del tipo di corso da creare: ");
                            Corso nuovoCorso = new Corso(tipoScelto);
                            accademia.aggiungiCorso(nuovoCorso);
                            accademia.apriCorso(nuovoCorso);
                            IO.println("Corso aggiunto e aperto con successo!");
                            corsoCorretto = true;
                        } catch (IllegalArgumentException e) {
                            IO.println("Errore: " + e.getMessage() + " Riprova.");
                        }
                    }
                    break;

                case 2:
                    ArrayList<Corso> corsiAttuali = accademia.getCorsi();
                    if (corsiAttuali.isEmpty()) {
                        IO.println("Non ci sono corsi disponibili! Creane prima uno (Opzione 1).");
                        break;
                    }

                    boolean iscrizioneCorretta = false;
                    while (!iscrizioneCorretta) {
                        try {

                            IO.println("Scegli il corso a cui iscrivere l'allievo:");
                            for (int i = 0; i < corsiAttuali.size(); i++) {
                                IO.println(i + " -> " + corsiAttuali.get(i).toString());
                            }
                            int indiceCorso = Integer.parseInt(IO.readln("Inserisci il numero del corso: "));
                            Corso corsoScelto = corsiAttuali.get(indiceCorso);


                            String nome = IO.readln("Inserisci il nome dell'allievo: ");
                            int eta = Integer.parseInt(IO.readln("Inserisci l'età dell'allievo: "));
                            String strumento = IO.readln("Inserisci lo strumento dell'allievo (es. pianista): ");


                            boolean iscritto = corsoScelto.iscriviAllievo(nome, eta, strumento);
                            if (iscritto) {
                                IO.println("Allievo iscritto con successo!");
                            } else {
                                IO.println("Impossibile iscrivere: il corso ha raggiunto il numero massimo di iscritti.");
                            }
                            iscrizioneCorretta = true;


                        } catch (IllegalArgumentException e) {
                            IO.println("Errore nei dati inseriti: " + e.getMessage() + " Riprova.");
                        }
                    }
                    break;

                case 3:
                    ArrayList<Corso> corsiDaRimuovere = accademia.getCorsi();
                    if (corsiDaRimuovere.isEmpty()) {
                        IO.println("Non ci sono corsi presenti.");
                        break;
                    }

                    try {
                        for (int i = 0; i <corsiDaRimuovere.size(); i++) {
                            IO.println(i + " -- " + corsiDaRimuovere.get(i).toString());
                        }
                        int indice = Integer.parseInt(IO.readln("Da quale corso vuoi rimuovere l'alunno? (inserisci numero): "));
                        Corso corsoTarget = corsiDaRimuovere.get(indice);

                        String nomeDaRimuovere = IO.readln("Inserisci il nome dell'alunno da rimuovere: ");
                        boolean rimosso = corsoTarget.rimuoviAllievo(nomeDaRimuovere);

                        if (rimosso) {
                            IO.println("Alunno rimosso con successo!");
                        } else {
                            IO.println("Alunno non trovato in questo corso.");
                        }
                    } catch (Exception e) {
                        IO.println("Errore durante la rimozione: " + e.getMessage());
                    }
                    break;

                case 4:
                    ArrayList<Corso> corsiDaCancellare = accademia.getCorsi();
                    if (corsiDaCancellare.isEmpty()) {
                        IO.println("Non ci sono corsi da cancellare.");
                        break;
                    }

                    try {
                        for (int i = 0; i < corsiDaCancellare.size(); i++) {
                            IO.println(i + " -> " + corsiDaCancellare.get(i).toString());
                        }
                        int indice = Integer.parseInt(IO.readln("Quale corso vuoi cancellare? (inserisci numero): "));
                        Corso corsoDaEliminare = corsiDaCancellare.get(indice);

                        boolean cancellato = accademia.cancellaCorso(corsoDaEliminare);
                        if (cancellato) {
                            IO.println("Corso cancellato con successo!");
                        } else {
                            IO.println("Impossibile cancellare il corso.");
                        }
                    } catch (Exception e) {
                        IO.println("Errore: " + e.getMessage());
                    }
                    break;

                case 5:
                    IO.println("--- REGISTRO ACCADEMIA ---");
                    IO.println(accademia.getRegistro().toString());
                    break;

                case 6:
                    IO.println("--- LISTA CORSI ---");
                    accademia.controllaScadenzeCorsi();
                    IO.println(accademia.toString());
                    break;

                default:
                    IO.println("Opzione non valida! Scegli un numero tra 0 e 6.");
                    break;
            }
        }
    }
}