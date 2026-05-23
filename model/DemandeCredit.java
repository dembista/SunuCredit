package model;

import java.util.ArrayList;
import java.util.List;

import enummeration.StatutDemande;
import records.Duree;
import records.Montant;

public class DemandeCredit {
    private static long idCounter = 0;
    public String idDemande;
    private final long id;
    private final String nomClient;
    private final Montant montant;
    private final Duree duree;
    private StatutDemande statut;
    private List<String> documentsGarantis;

public DemandeCredit(String nomClient, Montant montant, Duree duree, List<String> garanties) {
    this.nomClient = nomClient;
    this.montant = montant;
    this.duree = duree;

    if (garanties == null || garanties.size() < 2) {
        throw new IllegalArgumentException("Une demande exige un minimum de 2 documents de garantie.");
    }
    this.documentsGarantis = new ArrayList<>(garanties);

    idCounter++;
    this.id = idCounter;
    this.idDemande = "CRD-" + this.id;

    this.statut = StatutDemande.NOUVEAU;
}

    public long getId() {
        return id;
    }
    public String getNomClient() {
        return nomClient;
    }
    public Montant getMontant() {
        return montant;
    }
    public Duree getDuree() {
        return duree;
    }
    public StatutDemande getStatut() {
        return statut;
    }
    public List<String> getDocumentsGarantis() {
        return documentsGarantis;
    }

    public void ajouterDocumentGaranti(String document) {
        documentsGarantis.add(document);
    }
    public void avancerStatut() {
        this.statut = this.statut.suivant();
    }
    public void rejeter() {
        this.statut = StatutDemande.REJETE;
    }
    public boolean estTerminee() {
        return this.statut == StatutDemande.TERMINE || this.statut == StatutDemande.REJETE;
    }
    public boolean estRejetee() {
        return this.statut == StatutDemande.REJETE;
    }
    public boolean estEnCours() {
        return this.statut == StatutDemande.EN_COURS;
    }
    public boolean estNouveau() {
        return this.statut == StatutDemande.NOUVEAU;
    }
    public boolean estApprouvee() {
        return this.statut == StatutDemande.APPROUVE;
    }
    public String toString() {
        return "DemandeCredit{id=" + id + ", nomClient='" + nomClient + "', montant=" + montant.valeurEnFcfA() +
                " FCFA, duree=" + duree.mois() + " mois, statut=" + statut + ", documentsGarantis=" + documentsGarantis + "}";
    }

    public String getIdDemande() {
        return idDemande;
    }
    public void setIdDemande(String idDemande) {
        // Ne pas permettre de changer l'id une fois défini
        if (this.idDemande.isEmpty()) {
            this.idDemande = idDemande;
        }
    }
    public boolean aDocumentsGarantis() {
        return !documentsGarantis.isEmpty();
    }
    public boolean aDocumentGaranti(String document) {
        return documentsGarantis.contains(document);
    }
    public void retirerDocumentGaranti(String document) {
        documentsGarantis.remove(document);
    }
    public void clearDocumentsGarantis() {
        documentsGarantis.clear();
    }
    public void evaluer() {
        if (montant.valeurEnFcfA() > 5_000_000) {
            rejeter();
        } else if (montant.valeurEnFcfA() > 2_000_000 && !aDocumentsGarantis()) {
            rejeter();
        } else {
            avancerStatut();
        }
    }

    public void entamerAnalyse() {
        if (this.statut != StatutDemande.NOUVEAU) {
            throw new IllegalStateException("Impossible d'entamer l'analyse depuis l'état : " + this.statut);
        }
        this.statut = StatutDemande.EN_COURS;
    }
    public void evaluerEtDecider() {
        if (this.statut != StatutDemande.EN_COURS) {
            throw new IllegalStateException("La demande doit être EN_COURS pour recevoir une décision.");
        }

        long fraisDossier = Math.round(this.montant.valeurEnFcfA() * 0.02);
        long montantTotal = this.montant.valeurEnFcfA() + fraisDossier;

        if (montantTotal <= 5_000_000) {
            this.statut = StatutDemande.APPROUVE;
        } else {
            rejeter();
        }
    }
}
