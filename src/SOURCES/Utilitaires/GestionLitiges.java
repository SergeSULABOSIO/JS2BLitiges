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
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfacePaiement;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class GestionLitiges {

    public static Vector<InterfaceEcheance> getEcheances(int idFraisFiltre, InterfaceEleve eleveEncours, DonneesLitige donneesLitige, ParametresLitige parametresLitige) {
        Vector<InterfaceEcheance> listeEcheances = new Vector<>();
        if (parametresLitige != null && donneesLitige != null && eleveEncours != null) {
            InterfaceExercice Iexercice = parametresLitige.getExercice();
            if (Iexercice != null) {
                int nombreMax = getNbTranchesMax(parametresLitige.getArticles(idFraisFiltre));
                double daysExercice = Util.getNombre_jours(Iexercice.getFin(), Iexercice.getDebut());
                long nbDaysParTrancheLong = (long) ((daysExercice / nombreMax) * 1000 * 60 * 60 * 24);
                long days = 0;
                for (int i = 0; i < nombreMax; i++) {
                    String nomTranche = "1ère Tranche";
                    if ((i + 1) > 1) {
                        nomTranche = (i + 1) + "ème Tranche";
                    }

                    Date debutTranche = new Date(Iexercice.getDebut().getTime() + days);
                    Date finTranche = new Date(debutTranche.getTime() + nbDaysParTrancheLong);

                    listeEcheances.add(new XX_Echeance(-1, nomTranche, -1, debutTranche, finTranche, "", 0, 0, parametresLitige.getMonnaieOutPut().getId()));
                    days = days + nbDaysParTrancheLong;
                }

                //Chargement des données sur la liste des créances
                setData(idFraisFiltre, eleveEncours, listeEcheances, parametresLitige, donneesLitige);
            }
        }
        return listeEcheances;
    }

    private static void setData(int idFraisFiltre, InterfaceEleve eleveEncours, Vector<InterfaceEcheance> listeEcheances, ParametresLitige parametresLitige, DonneesLitige donneesLitige) {
        GestionLitiges.setMontantDu(idFraisFiltre, eleveEncours, listeEcheances, parametresLitige, donneesLitige);
        GestionLitiges.setMontantPaye(idFraisFiltre, eleveEncours, listeEcheances, parametresLitige, donneesLitige);
    }

    public static int getNbTranchesMax(Vector<InterfaceArticle> listeArticles) {
        int nombreTranches = 0;
        if (listeArticles != null) {
            if (!listeArticles.isEmpty()) {
                for (InterfaceArticle article : listeArticles) {
                    //System.out.println(" * " + article.toString() + ", tranches = " + article.getTranches());
                    //Si et seulement si le rabais reste strictement inférieur au prix unitaire avant remise
                    if (article.getPrixUHT_avant_rabais() > article.getRabais()) {
                        if (article.getTranches() > nombreTranches) {
                            nombreTranches = article.getTranches();
                        }
                    }
                }
            }
        }
        return nombreTranches;
    }

    private static Vector isAyantDroit(InterfaceEleve eleve, int idfrais, DonneesLitige donneesLitige) {
        Vector reponses = new Vector();
        for (InterfaceAyantDroit Iaya : donneesLitige.getAyantDroits()) {
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

    private static void setMontantDu(int idFraisFiltre, InterfaceEleve eleveEncours, Vector<InterfaceEcheance> listeEcheance, ParametresLitige parametresLitige, DonneesLitige donneesLitige) {
        Vector<InterfaceArticle> listeArticles = parametresLitige.getArticles(idFraisFiltre);
        double nombreTranches = getNbTranchesMax(listeArticles);
        if (!listeEcheance.isEmpty()) {
            for (int indexTranche = 0; indexTranche < nombreTranches; indexTranche++) {
                InterfaceEcheance echeEncours = listeEcheance.elementAt(indexTranche);
                double mont = 0;
                for (InterfaceArticle Iart : listeArticles) {
                    if (indexTranche + 1 <= Iart.getTranches()) {
                        /*
                        On doit savoir si cet élève est un ayantdroit ou pas
                        Si oui, alors on ne doit considérer que le montant qu'il doit payer
                         */
                        Vector isAyantDroit = isAyantDroit(eleveEncours, Iart.getId(), donneesLitige);
                        if (isAyantDroit == null) {//Ce n'est pas un AyantDroit
                            mont += Util.getMontantOutPut(parametresLitige, Iart.getIdMonnaie(), Iart.getTotalTTC()) / Iart.getTranches();
                        } else {
                            boolean isAD = (boolean) isAyantDroit.firstElement();
                            double montantAD = Double.parseDouble(isAyantDroit.elementAt(1) + "");
                            int idMonnaieAD = Integer.parseInt(isAyantDroit.elementAt(2) + "");
                            if (isAD == true) {
                                mont += Util.getMontantOutPut(parametresLitige, idMonnaieAD, montantAD) / Iart.getTranches();
                            }
                        }
                    }
                    echeEncours.setMontantDu(mont);
                }
            }
        }
    }

    private static void setMontantPaye(int idFraisFiltre, InterfaceEleve eleveEncours, Vector<InterfaceEcheance> listeEcheance, ParametresLitige parametresFacture, DonneesLitige donneesLitige) {
        Vector<InterfaceArticle> listeArticles = parametresFacture.getArticles(idFraisFiltre);
        double nombreTranches = getNbTranchesMax(listeArticles);
        //On calcul les montants déjà payés pour cette tranche
        if (!listeEcheance.isEmpty()) {
            for (int i = 0; i < nombreTranches; i++) {
                InterfaceEcheance echeEncours = listeEcheance.elementAt(i);
                double montPaye = 0;
                for (InterfacePaiement paiement : donneesLitige.getPaiements()) {
                    //Si ce paiement est effectué par l'élève encours
                    if (paiement.getIdEleve() == eleveEncours.getId()) {//Si ce paiement est effectué par l'élève encours
                        boolean isPaiementDansEchanceEncours = (paiement.getDate().compareTo(echeEncours.getDateInitiale()) > 0 && paiement.getDate().compareTo(echeEncours.getDateFinale()) <= 0);
                        //Si ce paiement est compris entre la période de cette échéance
                        if (isPaiementDansEchanceEncours == true) {
                            InterfaceArticle Iart = getArticle(listeArticles, paiement.getIdArticle());
                            if (Iart != null) {
                                montPaye = montPaye + Util.getMontantOutPut(parametresFacture, Iart.getIdMonnaie(), paiement.getMontant());
                            }
                        }
                    }
                }
                echeEncours.setMontantPaye(montPaye);
            }
        }
    }

    private static InterfaceArticle getArticle(Vector<InterfaceArticle> listeArticle, int id) {
        for (InterfaceArticle Iart : listeArticle) {
            if (id == Iart.getId()) {
                return Iart;
            }
        }
        return null;
    }
}
