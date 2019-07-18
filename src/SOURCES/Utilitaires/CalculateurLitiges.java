/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;



import Source.Interface.InterfaceAyantDroit;
import Source.Interface.InterfaceEleve;
import Source.Interface.InterfaceFrais;
import Source.Interface.InterfacePaiement;
import Source.Objet.Ayantdroit;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Frais;
import Source.Objet.LiaisonFraisEleve;
import Source.Objet.LiaisonFraisPeriode;
import Source.Objet.Periode;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class CalculateurLitiges {

    public static Vector<Echeance> getEcheances(int idSolvabilite, int idFraisFiltre, int idPeriodeFiltre, Eleve eleveEncours, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        
        Vector<Echeance> listeEcheances = new Vector<>();
        for (Periode Iperiode : parametresLitige.getListePeriodes(idPeriodeFiltre)) {
            
            //Recherche des montants dûs
            double montantDu = 0;
            Vector dataAyantDroit = null;
            for (Frais Iarticle : parametresLitige.getFrais(idFraisFiltre)) {
                for (LiaisonFraisPeriode liaison : Iarticle.getLiaisonsPeriodes()) {
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
            
            Echeance echeance = new Echeance(-1, Iperiode.getNom(), -1, Iperiode.getDebut(), Iperiode.getFin(), "", montantPaye, montantDu, parametresLitige.getMonnaieOutPut().getId());
            
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

    private static Vector isAyantDroit(Eleve eleve, int idfrais, DonneesLitige donneesLitige) {
        Vector reponses = new Vector();
        for (Ayantdroit Iaya : donneesLitige.getListeAyantDroits()) {
            if (Iaya.getIdEleve() == eleve.getId()) {
                for (LiaisonFraisEleve liaison : Iaya.getListeLiaisons()) {
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