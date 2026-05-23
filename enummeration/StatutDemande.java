package enummeration;

import exceptions.DomainRuleException;

public enum StatutDemande {
NOUVEAU {
        @Override
        public StatutDemande suivant() {
            return EN_COURS;
        }
    },
EN_COURS {
        @Override
        public StatutDemande suivant() {
            return APPROUVE;
        }
    },
APPROUVE {
        @Override
        public StatutDemande suivant() {
            return TERMINE;
        }
    },
TERMINE,
REJETE;

    public StatutDemande suivant() {
        throw new DomainRuleException("Transition impossible depuis " + this);
    }



}
