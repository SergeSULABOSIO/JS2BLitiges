/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceArticle;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelesTables.ModeleListeLitiges;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class Util {

    public static boolean contientMotsCles(String base, String motscles) {
        boolean rep = false;
        String[] tabMotsCles = motscles.split(" ");
        for (int i = 0; i < tabMotsCles.length; i++) {
            if (base.toLowerCase().contains(tabMotsCles[i].toLowerCase().trim())) {
                return true;
            }
        }
        return rep;
    }

    public static Vector<String> getTablePeriodes(ModeleListeLitiges modeleListeLitiges) {
        Vector<String> nomsPeriodes = new Vector<>();
        if (modeleListeLitiges != null) {
            for (InterfaceLitige Ilit : modeleListeLitiges.getListeData()) {
                if (Ilit != null) {
                    for (InterfaceEcheance Ich : Ilit.getListeEcheances()) {
                        if (!nomsPeriodes.contains(Ich.getNom())) {
                            nomsPeriodes.add(Ich.getNom());
                        }
                    }
                }
            }
        }
        return nomsPeriodes;
    }

    public static boolean isSolvable(InterfaceLitige Ilitige) {
        double totDu = 0;
        double totPaye = 0;
        for (InterfaceEcheance Ieche : Ilitige.getListeEcheances()) {
            totDu += Ieche.getMontantDu();
            totPaye += Ieche.getMontantPaye();
        }
        return (totDu <= totPaye);
    }

    public static InterfaceMonnaie getMonnaie(ParametresLitige parametresFacture, int idMonnaie) {
        for (InterfaceMonnaie Imonnaie : parametresFacture.getListeMonnaies()) {
            if (Imonnaie.getId() == idMonnaie) {
                return Imonnaie;
            }
        }
        return null;
    }

    public static InterfaceArticle getArticle(ParametresLitige parametresLitige, int idArticle) {
        for (InterfaceArticle Iart : parametresLitige.getArticles(-1)) {
            if (Iart.getId() == idArticle) {
                return Iart;
            }
        }
        return null;
    }

    public static InterfaceClasse getClasse(ParametresLitige parametresFacture, int idClasse) {
        for (InterfaceClasse Iclasse : parametresFacture.getListeClasse()) {
            if (Iclasse.getId() == idClasse) {
                return Iclasse;
            }
        }
        return null;
    }

    public static double getMontantOutPut(ParametresLitige parametresFacture, int idMonnaieInput, double montant) {
        InterfaceMonnaie monnaieOutPut = getMonnaie(parametresFacture, parametresFacture.getMonnaieOutPut().getId());
        InterfaceMonnaie monnaieInPut = getMonnaie(parametresFacture, idMonnaieInput);

        if (monnaieOutPut != null && monnaieInPut != null) {
            if (parametresFacture.getMonnaieOutPut().getId() == idMonnaieInput) {
                return montant;
            } else {
                return (montant * monnaieInPut.getTauxMonnaieLocale() / monnaieOutPut.getTauxMonnaieLocale());
            }
        } else {
            return 0;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getLettres(double montant, String NomMonnaie) {
        String texte = "";
        try {
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texte;
    }

    public static Date getDate_AjouterAnnee(Date dateActuelle, int nbAnnee) {
        try {
            int plus = dateActuelle.getYear() + nbAnnee;
            dateActuelle.setYear(plus);
            return dateActuelle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getNombre_jours(Date dateFin, Date dateDebut) {
        try {
            double nb = (int) ((dateFin.getTime() - dateDebut.getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getNombre_jours_from_today(Date dateFin) {
        try {
            double nb = (double) ((dateFin.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateFrancais(Date date) {
        String dateS = "";
        try {
            String pattern = "dd MMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            dateS = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateS;
    }

    public static String getMontantFrancais(double montant) {
        String val = "";
        int ValEntiere = (int) montant;
        char[] valInput = (ValEntiere + "").toCharArray();
        int index = 0;
        for (int i = valInput.length - 1; i >= 0; i--) {
            //System.out.println(" \t *  " + valInput[i]);
            if (index % 3 == 0 && index != 0) {
                val = valInput[i] + "." + val;
            } else {
                val = valInput[i] + val;
            }
            index++;
        }
        int ValApresVirgule = (int) (round(((montant - ValEntiere) * 100), 0));
        //System.out.println("Valeur d'origine = " + montant);
        //System.out.println("Partie entière = " + ValEntiere);
        //System.out.println("Partie décimale = " + ValApresVirgule);
        return val + "," + ValApresVirgule;
    }

    public static String getMontantLettres(double montant, String NomMonnaie) {
        String texte = "";
        try {
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        } catch (Exception e) {
            System.out.println("Un problème est survenu lors de la conversion des chiffres en nombre.");
            texte = "";
        }
        return texte;
    }

    public static void main(String[] args) {
        double origine = 10000.14;

        String res = Util.getMontantFrancais(origine);
        System.out.println("Résultat = " + res);
        System.out.println("Résultat = " + Util.getMontantLettres(origine, "Dollars Américains"));
    }
}
