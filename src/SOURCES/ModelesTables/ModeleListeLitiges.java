/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelesTables;

import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.UtilLitige;
import Source.Callbacks.EcouteurValeursChangees;
import Source.Interface.InterfaceEcheance;
import Source.Interface.InterfaceLitige;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Litige;
import Source.Objet.Periode;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeLitiges extends AbstractTableModel {

    private String[] titreColonnes = null;
    private Vector<Litige> listeData = new Vector<>();
    private EcouteurValeursChangees ecouteurModele;
    private ParametresLitige parametresLitige;
    private JProgressBar progress;
    //private DonneesLitige donneesLitige;

    public ModeleListeLitiges(JProgressBar progress, int idSolvabilite, String nomEleve, int idClasse, int idFrais, int idPeriode, ParametresLitige parametresLitige, EcouteurValeursChangees ecouteurModele) {
        this.ecouteurModele = ecouteurModele;
        this.parametresLitige = parametresLitige;
        this.progress = progress;
    }

    private boolean verifierNomEleve(String nomEleve, Eleve Ieleve) {
        boolean reponse = false;
        if (nomEleve.trim().length() == 0) {
            reponse = true;
        } else {
            reponse = ((UtilLitige.contientMotsCles(Ieleve.getNom() + " " + Ieleve.getPostnom() + " " + Ieleve.getPrenom(), nomEleve)));
        }
        return reponse;
    }

    

    /*
    
    
    public void chercher(JProgressBar progress, int idSolvabilite, String nomEleve, int idClasse, int idFrais, int idPeriode) {
        listeData.removeAllElements();
        actualiser();
        if (progress != null) {
            progress.setVisible(true);
            progress.setIndeterminate(true);
        }
        for (Eleve Ieleve : donneesLitige.getListeEleves()) {
            if (verifierNomEleve(nomEleve, Ieleve) == true) {
                if (verifierClasse(idClasse, Ieleve) == true) {
                    Vector<Echeance> listeEcheances = CalculateurLitiges.getEcheances(idSolvabilite, idFrais, idPeriode, Ieleve, donneesLitige, parametresLitige);
                    if (listeEcheances != null) {
                        if (!listeEcheances.isEmpty()) {
                            listeData.add(new Litige(1, Ieleve.getId(), Ieleve.getIdClasse(), listeEcheances, InterfaceLitige.BETA_EXISTANT));
                        }
                    }
                }
            }
        }
        initTitresColonnes();
        actualiser();
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
    }
    
     */
    
    public void setDonneesLitiges(Litige newLitige) {
        if (progress != null) {
            progress.setVisible(true);
            progress.setIndeterminate(true);
        }
        listeData.add(newLitige);
        initTitresColonnes();
        actualiser();
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
    }

    public void reinitialiserListe() {
        this.listeData.removeAllElements();
        redessinerTable();
    }

    public double[] getTotaux() {
        double[] tabTotaux = new double[3];  //Total Du, Total Payé, Total Solde
        for (InterfaceLitige iLitige : listeData) {
            for (InterfaceEcheance iEche : iLitige.getListeEcheances()) {
                tabTotaux[0] += UtilLitige.getMontantOutPut(parametresLitige, iEche.getIdMonnaie(), iEche.getMontantDu());
                tabTotaux[1] += UtilLitige.getMontantOutPut(parametresLitige, iEche.getIdMonnaie(), iEche.getMontantPaye());
            }
        }
        tabTotaux[2] = tabTotaux[0] - tabTotaux[1];
        return tabTotaux;
    }

    public Litige getLitiges(int row) {
        if (row < listeData.size() && row != -1) {
            Litige art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Litige getLitige_id(int id) {
        if (id != -1) {
            for (Litige art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<Litige> getListeData() {
        return this.listeData;
    }

    public void redessinerTable() {
        if (ecouteurModele != null) {
            ecouteurModele.onValeurChangee();
        }
        fireTableDataChanged();
    }

    public void actualiser() {
        redessinerTable();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    private void initTitresColonnes() {
        //Premiers Groupes
        Vector titres = new Vector();
        titres.add("N°");
        titres.add("Elève");
        titres.add("Classe");
        titres.add("Solvable?");
        //Deuxième Groupe
        //Vector<String> temptab = UtilLitige.getTablePeriodes(this);
        Vector<Periode> temptab = parametresLitige.getListePeriodes(-1);
        if (!temptab.isEmpty()) {
            for (Periode prd : temptab) {
                titres.add(prd.getNom());
            }
        }
        //On verse les titres dans le tableau static
        this.titreColonnes = new String[titres.size()];
        for (int i = 0; i < titreColonnes.length; i++) {
            this.titreColonnes[i] = titres.elementAt(i) + "";
        }
    }

    @Override
    public int getColumnCount() {
        initTitresColonnes();
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        initTitresColonnes();
        if (column < titreColonnes.length) {
            return titreColonnes[column];
        } else {
            return "Null";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Elève", "Classe", "Solvable?"};
        if (rowIndex < listeData.size()) {
            Litige Ilitige = listeData.elementAt(rowIndex);
            if (Ilitige != null) {
                if (columnIndex < 4) {
                    switch (columnIndex) {
                        case 0:
                            return (rowIndex + 1) + "";
                        case 1:
                            return Ilitige.getIdEleve();
                        case 2:
                            return Ilitige.getIdClasse();
                        case 3:
                            return UtilLitige.isSolvable(Ilitige);
                    }
                } else {
                    if (columnIndex < this.getColumnCount()) {
                        String titreColonneEche = this.getColumnName(columnIndex);
                        Vector<Echeance> listeEcheances = Ilitige.getListeEcheances();
                        for (InterfaceEcheance Iech : listeEcheances) {
                            if (Iech.getNom().equals(titreColonneEche)) {
                                return Iech;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Nom", "Capacité", "Solvable?"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Integer.class;//Elève
            case 2:
                return Integer.class;//Classe
            case 3:
                return boolean.class;//Solvable?
            default:
                return InterfaceEcheance.class; //Les restes sont des Objets du type Echéances
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //On ne peut pas modifier cette liste
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Elève", "Classe", "Solvable?"};
        //On ne peut pas modifier cette liste
    }

}


