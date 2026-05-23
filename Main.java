

import model.DemandeCredit;
import model.Traitement;

import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SYSTEME AUTOMATISE D'OCTROI DE CREDIT (SUNUCREDIT) ===");
        System.out.println("--- Chargement et Traitement du Flux par Lots ---\n");

        Stream<String> fluxDeDemandesBrutes = Stream.of(
            "CL-101;1500000;12;TITRE_FONCIER,CONTRAT_TRAVAIL",
            "CL-102;quatre-vingts-mille;24;GARANTIE_SOCIETE",
            "CL-103;20000;12;CAUTION,HYPOTHEQUE",
            "   ",
            "CL-104;4900000;36;TITRE_FONCIER,CONTRAT_TRAVAIL",
            "CL-105;4950000;24;AVAL_BANCAIRE,CONTRAT_COMMERCIAL",
            "CL-106;2000000;65;TITRE_FONCIER,CAUTION",
            "CL-107;1000000;24;TITRE_FONCIER"
        );

Traitement importateur = new Traitement();

List<DemandeCredit> demandesValides = importateur.traiterFlux(fluxDeDemandesBrutes);

importateur.traiterDemandes(demandesValides);

        System.out.println("Resultat du parsing du lot :");
        System.out.println("-> Nombre de demandes importees avec succes : " + demandesValides.size() + " / 8 lignes reçues.\n");
        System.out.println("--- Execution du Cycle de Vie et Decisions Metier ---");
        demandesValides.forEach(demande -> {
            System.out.println("\n------------------------------------------------------------");
            System.out.println("ID Demande : " + demande.getIdDemande());
            System.out.println("ID Client  : " + demande.getId());
            System.out.println("Montant    : " + demande.getMontant().valeurEnFcfA() + " FCFA");
            System.out.println("Duree      : " + demande.getDuree().mois() + " mois");
            System.out.println("Garanties  : " + String.join(", ", demande.getDocumentsGarantis()));
            System.out.println("Statut Initial : " + demande.getStatut());

            try {


                if (demande.getStatut().name().equals("VALIDE")) {
                    System.out.println("Decision : CREDIT ACCORDE");
                } else {
                    System.out.println("Decision : CREDIT REJETE (Depassement du plafond de 5 000 000 FCFA avec frais de dossier)");
                }

            } catch (Exception e) {
                System.err.println("Violation des regles de l'etat : " + e.getMessage());
            }
        });
    }
}
