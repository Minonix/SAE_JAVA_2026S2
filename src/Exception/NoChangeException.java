package Exception;

/**
 * Exception personnalisée signalant qu'aucune modification n'a été effectuée 
 * ou qu'une action a été annulée par l'utilisateur.
 * Cette exception est principalement utilisée lors de l'ouverture de fenêtres de dialogue 
 * (comme la création d'un nouveau secteur ou le paramétrage d'une tournée). Elle permet 
 * d'indiquer au code appelant que l'utilisateur a cliqué sur "Retour", a fermé la fenêtre 
 * avec la croix, ou n'a rien validé, évitant ainsi le traitement de données nulles ou incomplètes.
 */
public class NoChangeException extends Exception {

    /**
     * Constructeur par défaut de l'exception.
     * Initialise l'exception sans message de détail spécifique en faisant appel 
     * au constructeur de la classe parente {@link Exception}.
     */
    public NoChangeException() {
        super();
    }
}