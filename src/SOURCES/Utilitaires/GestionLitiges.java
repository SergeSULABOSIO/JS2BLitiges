/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfacePeriode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class GestionLitiges {

    public static Vector<InterfaceEcheance> getEcheances(int idFraisFiltre, InterfaceEleve eleveEncours, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        Vector<InterfaceEcheance> listeEcheances = new Vector<>();
        for (InterfacePeriode Iperiode : parametresLitige.getListePeriodes()) {

            //Recherche des montants dûs
            double montantDu = 0;
            for (InterfaceArticle Iarticle : parametresLitige.getListeArticles(idFraisFiltre)) {
                for (LiaisonPeriodeFrais liaison : Iarticle.getLiaisonsPeriodes()) {
                    if (liaison.getIdPeriode() == Iperiode.getId() && liaison.getNomPeriode().equals(Iperiode.getNom())) {

                        /*
                            Il faut appliquer la conversion selon la monnaie Output définie
                            Il faut aussi ne prendre en compte que le montant payable au cas où il s'agit d'un ayant-droit
                        
                         */
                        
                        Vector dataAyantDroit = isAyantDroit(eleveEncours, Iarticle.getId(), donneesLitige);
                        double montDu = 0;
                        if (dataAyantDroit != null) {
                            double montantAyantDroit = (double) dataAyantDroit.elementAt(1);
                            int idMonnaieAyantDroit = (int) dataAyantDroit.elementAt(2);
                            montDu = Util.round((montantAyantDroit * liaison.getPourcentage()) / 100, 2);
                            montantDu += Util.getMontantOutPut(parametresLitige, idMonnaieAyantDroit, montDu);
                        } else {
                            montDu = Util.round((Iarticle.getTotalTTC() * liaison.getPourcentage()) / 100, 2);
                            montantDu += Util.getMontantOutPut(parametresLitige, Iarticle.getIdMonnaie(), montDu);
                        }
                    }
                }
            }

            //Recherche des montants payes
            double montantPaye = 0;
            for (InterfacePaiement Ipaiement : donneesLitige.getListePaiements(idFraisFiltre)) {
                if (Ipaiement.getIdPeriode() == Iperiode.getId() && eleveEncours.getId() == Ipaiement.getIdEleve()) {

                    /*
                        Il faut appliquer la conversion selon la monnaie Output définie
                     */
                    
                    InterfaceArticle Iart = Util.getArticle(parametresLitige, Ipaiement.getIdArticle());
                    montantPaye += Util.getMontantOutPut(parametresLitige, Iart.getIdMonnaie(), Ipaiement.getMontant());

                }
            }
            //if (montantDu != 0) {
            listeEcheances.add(new XX_Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresLitige.getMonnaieOutPut().getId()));
            //}
        }
        return listeEcheances;
    }

    private static Vector isAyantDroit(InterfaceEleve eleve, int idfrais, DonneesLitige donneesLitige) {
        Vector reponses = new Vector();
        for (InterfaceAyantDroit Iaya : donneesLitige.getListeAyantDroits()) {
            if (Iaya.getIdEleve() == eleve.getId()) {
                for (LiaisonEleveFrais liaison : Iaya.getListeLiaisons()) {
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
