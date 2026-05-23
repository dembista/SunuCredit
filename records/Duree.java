package records;

import exceptions.DomainRuleException;

public record Duree(int mois) {
public Duree {
        if (mois < 6 || mois > 60) {
            throw new DomainRuleException("La durée doit être comprise entre 6 et 60 mois.");
        }
    }
}
