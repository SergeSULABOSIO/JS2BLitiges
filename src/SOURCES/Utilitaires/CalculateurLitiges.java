/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import Source.Objet.Ayantdroit;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Paiement;
import Source.Objet.Periode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class CalculateurLitiges {
    
    public static Vector<Echeance> getEcheances(int idSolvabilite, int idFraisFiltre, int idPeriodeFiltre, Eleve eleveEncours, Ayantdroit ayantdroit, Vector<Paiement> lisPaiementsEleve, ParametresLitige parametresLitige) {
        /*
        System.out.println("CRITERES DE RECHERCHER:");
        System.out.println(" - idSolvabilite: " + idSolvabilite);
        System.out.println(" - idFraisFiltre: " + idFraisFiltre);
        System.out.println(" - idPeriodeFiltre: " + idPeriodeFiltre);
        System.out.println(" - eleveEncours: " + eleveEncours.toString());
        */

        Vector<Echeance> listeEcheances = new Vector<>();
        for (Periode Iperiode : parametresLitige.getListePeriodes(idPeriodeFiltre)) {
            double montantDu = 0;
            Vector dataAyantDroit = null;
            for (Frais Iarticle : parametresLitige.getFrais(idFraisFiltre)) {
                for (LiaisonFraisPeriode liaison : Iarticle.getLiaisonsPeriodes()) {
                    if (liaison.getSignaturePeriode() == Iperiode.getSignature()) {
                        dataAyantDroit = isAyantDroit(eleveEncours, Iarticle.getId(), ayantdroit);
                        double montDu = 0;
                        if (dataAyantDroit != null) {
                            double montantAyantDroit = (double) dataAyantDroit.elementAt(1);
                            int idMonnaieAyantDroit = (int) dataAyantDroit.elementAt(2);
                            montDu = UtilLitige.round((montantAyantDroit * liaison.getPourcentage()) / 100, 2);
                            montantDu += UtilLitige.getMontantOutPut(parametresLitige, idMonnaieAyantDroit, montDu);
                        } else {
                            montDu = UtilLitige.round((Iarticle.getMontantDefaut() * liaison.getPourcentage()) / 100, 2);
                            montantDu += UtilLitige.getMontantOutPut(parametresLitige, Iarticle.getIdMonnaie(), montDu);
                        }
                    }
                }
            }

            if (montantDu == 0 && dataAyantDroit == null) { //Si le montant du est égal à Zéro alors on saute cette ligne !
                continue;
            }
            //Recherche des montants payes
            double montantPaye = 0;
            if (lisPaiementsEleve != null) {
                for (Paiement Ipaiement : lisPaiementsEleve) {//donneesLitige.getListePaiements(idFraisFiltre)
                    if (Iperiode != null && Ipaiement != null) {
                        if (Ipaiement.getIdPeriode() == Iperiode.getId() && eleveEncours.getId() == Ipaiement.getIdEleve()) {
                            Frais Iart = UtilLitige.getFrais(parametresLitige, Ipaiement.getIdFrais());
                            montantPaye += UtilLitige.getMontantOutPut(parametresLitige, Iart.getIdMonnaie(), Ipaiement.getMontant());
                        }
                    }
                }
            }

            Echeance echeance = new Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresLitige.getMonnaieOutPut().getId());

            if (idSolvabilite == -1) {
                listeEcheances.add(echeance);
            } else {
                if (idSolvabilite == 0) {//Solvables Uniquement
                    if (echeance.getMontantDu() <= echeance.getMontantPaye()) {
                        listeEcheances.add(echeance);
                    }
                } else {//I solvables Uniquement
                    if (echeance.getMontantDu() > echeance.getMontantPaye()) {
                        listeEcheances.add(echeance);
                    }
                }
            }
        }
        return listeEcheances;
    }

    private static Vector isAyantDroit(Eleve eleve, int idfrais, Ayantdroit ayantdroit) {
        Vector reponses = new Vector();
        if (ayantdroit != null) {
            if (ayantdroit.getSignatureEleve() == eleve.getSignature()) {
                for (LiaisonFraisEleve liaison : ayantdroit.getListeLiaisons()) {
                    if (liaison.getIdFrais() == idfrais) {
                        reponses.add(true);
                        reponses.add(liaison.getMontant());
                        reponses.add(liaison.getIdMonnaie());
                        return reponses;
                    }
                }
            }
        }
        return null;
    }
}




