/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceFrais;
import SOURCES.Interface.InterfacePaiement;
import SOURCES.Interface.InterfacePeriode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class GestionLitiges {

    public static Vector<InterfaceEcheance> getEcheances(int idSolvabilite, int idFraisFiltre, int idPeriodeFiltre, InterfaceEleve eleveEncours, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        /*
        System.out.println();
        System.out.println("idSolvabilite = " + idSolvabilite);
        System.out.println("idFraisFiltre = " + idFraisFiltre);
        System.out.println("idPeriodeFiltre = " + idPeriodeFiltre);  
        */
        
        Vector<InterfaceEcheance> listeEcheances = new Vector<>();
        for (InterfacePeriode Iperiode : parametresLitige.getListePeriodes(idPeriodeFiltre)) {
            
            //Recherche des montants dûs
            double montantDu = 0;
            Vector dataAyantDroit = null;
            for (InterfaceFrais Iarticle : parametresLitige.getFrais(idFraisFiltre)) {
                for (LiaisonPeriodeFrais liaison : Iarticle.getLiaisonsPeriodes()) {
                    if (liaison.getIdPeriode() == Iperiode.getId() && liaison.getNomPeriode().equals(Iperiode.getNom())) {
                        
                        /*
                            Il faut appliquer la conversion selon la monnaie Output définie
                            Il faut aussi ne prendre en compte que le montant payable au cas où il s'agit d'un ayant-droit
                        
                         */
                        
                        dataAyantDroit = isAyantDroit(eleveEncours, Iarticle.getId(), donneesLitige);
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
            
            // && dataAyantDroit == null
            if(montantDu == 0){ //Si le montant du est égal à Zéro alors on saute cette ligne !
                continue;
            }

            //Recherche des montants payes
            double montantPaye = 0;
            for (InterfacePaiement Ipaiement : donneesLitige.getListePaiements(idFraisFiltre)) {
                if (Ipaiement.getIdPeriode() == Iperiode.getId() && eleveEncours.getId() == Ipaiement.getIdEleve()) {

                    /*
                        Il faut appliquer la conversion selon la monnaie Output définie
                     */
                    InterfaceFrais Iart = UtilLitige.getFrais(parametresLitige, Ipaiement.getIdArticle());
                    montantPaye += UtilLitige.getMontantOutPut(parametresLitige, Iart.getIdMonnaie(), Ipaiement.getMontant());

                }
            }
            
            XX_Echeance echeance = new XX_Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresLitige.getMonnaieOutPut().getId());
            
            if (idSolvabilite == -1) {
                listeEcheances.add(echeance);
            } else {
                if (idSolvabilite == 0) {//Solvables Uniquement
                    if (echeance.getMontantDu() <= echeance.getMontantPaye()) {
                        listeEcheances.add(echeance);
                    }
                }else{//I solvables Uniquement
                    if (echeance.getMontantDu() > echeance.getMontantPaye()) {
                        listeEcheances.add(echeance);
                    }
                }
            }
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
