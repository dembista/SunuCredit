package records;

import exceptions.DomainRuleException;

public record Montant(long valeurEnFcfA) {
private static final long MIN_MONTANT = 100_000;
private static final long MAX_MONTANT = 10_000_000;
public Montant {
        if (valeurEnFcfA < MIN_MONTANT || valeurEnFcfA > MAX_MONTANT) {
            throw new DomainRuleException("Le montant doit être compris entre " + MIN_MONTANT + " et " + MAX_MONTANT + " FCFA.");
        }
    }
}
