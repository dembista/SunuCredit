package model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import records.Duree;
import records.Montant;

public class Traitement {

    public List<DemandeCredit> traiterFlux(Stream<String> lignesBrutes) {
        return Optional.ofNullable(lignesBrutes)
                .orElseGet(Stream::empty)
                .map(this::trouverDemandeParId)
                .flatMap(Optional::stream)
                .toList();
    }

    public List<DemandeCredit> traiterDemandes(List<DemandeCredit> demandes) {
        for (DemandeCredit demande : demandes) {
            try {
                // Étape A : Passage obligatoire en analyse
                demande.entamerAnalyse();

                demande.evaluerEtDecider();
            } catch (Exception e) {
                System.err.println(
                        " Impossible de traiter la demande de " + demande.getId() + " : " + e.getMessage());
            }
        }
        return demandes;
    }

    private Optional<DemandeCredit> trouverDemandeParId(String ligne) {
        return Optional.ofNullable(ligne)
                .filter(l -> !l.isBlank())
                .map(l -> l.split(";"))
                .filter(parts -> parts.length == 4)
                .flatMap(parts -> {
                    try {
                        String nomClient = parts[0].strip();
                        long montantVal = Long.parseLong(parts[1].strip());
                        int dureeVal = Integer.parseInt(parts[2].strip());

                        List<String> garanties = Arrays.stream(parts[3].split(","))
                                .map(String::strip)
                                .filter(g -> !g.isEmpty())
                                .toList();

                        DemandeCredit nouvelleDemande = new DemandeCredit(
                                nomClient,
                                new Montant(montantVal),
                                new Duree(dureeVal),
                                garanties);

                        return Optional.of(nouvelleDemande);
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                });
    }
}
