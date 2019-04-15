/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelesTables;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.Utilitaires.DonneesLitige;
import SOURCES.Utilitaires.GestionLitiges;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.Util;
import SOURCES.Utilitaires.XX_Litige;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeLitiges extends AbstractTableModel {

    private String[] titreColonnes = null;
    private Vector<InterfaceLitige> listeData = new Vector<>();
    private EcouteurValeursChangees ecouteurModele;
    private ParametresLitige parametresLitige;
    private DonneesLitige donneesLitige;

    public ModeleListeLitiges(int idSolvabilite, String nomEleve, int idClasse, int idFrais, int idPeriode, DonneesLitige donneesLitige, ParametresLitige parametresLitige, EcouteurValeursChangees ecouteurModele) {
        this.ecouteurModele = ecouteurModele;
        this.parametresLitige = parametresLitige;
        this.donneesLitige = donneesLitige;
        //On charge les données dans la liste
        chercher(idSolvabilite, nomEleve, idClasse, idFrais, idPeriode);
    }

    private boolean verifierNomEleve(String nomEleve, InterfaceEleve Ieleve) {
        boolean reponse = false;
        if (nomEleve.trim().length() == 0) {
            reponse = true;
        } else {
            reponse = ((Util.contientMotsCles(Ieleve.getNom() + " " + Ieleve.getPostnom() + " " + Ieleve.getPrenom(), nomEleve)));
        }
        return reponse;
    }

    private boolean verifierClasse(int idClasse, InterfaceEleve Ieleve) {
        boolean reponse = false;
        if (idClasse == -1) {
            reponse = true;
        } else {
            reponse = (idClasse == Ieleve.getIdClasse());
        }
        return reponse;
    }

    public void chercher(int idSolvabilite, String nomEleve, int idClasse, int idFrais, int idPeriode) {
        /*
            C'est ici qu'il faut charger automatiquement les données
            Calculer les litiges, puis les afficher
         */
        
        listeData.removeAllElements();
        actualiser();
        for (InterfaceEleve Ieleve : donneesLitige.getEleves()) {
            if (verifierNomEleve(nomEleve, Ieleve) == true) {
                if (verifierClasse(idClasse, Ieleve) == true) {
                    Vector<InterfaceEcheance> listeEcheances = GestionLitiges.getEcheances(idSolvabilite, idFrais, idPeriode, Ieleve, donneesLitige, parametresLitige);
                    if (listeEcheances != null) {
                        listeData.add(new XX_Litige(1, Ieleve.getId(), Ieleve.getIdClasse(), listeEcheances, InterfaceLitige.BETA_EXISTANT));
                    }
                }
            }
        }
        actualiser();
    }

    public InterfaceLitige getLitiges(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceLitige art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceLitige getLitige_id(int id) {
        if (id != -1) {
            for (InterfaceLitige art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceLitige> getListeData() {
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
        Vector titresCols = new Vector();
        titresCols.add("N°");
        titresCols.add("Elève");
        titresCols.add("Classe");
        titresCols.add("Solvable?");
        if (!listeData.isEmpty()) {
            Vector<InterfaceEcheance> lisEchea = listeData.firstElement().getListeEcheances();
            if (lisEchea != null) {
                for (InterfaceEcheance Ieche : lisEchea) {
                    titresCols.add(Ieche.getNom());
                }
            }
        }

        //On verse les titres dans le tableau static
        this.titreColonnes = new String[titresCols.size()];
        for (int i = 0; i < titreColonnes.length; i++) {
            this.titreColonnes[i] = titresCols.elementAt(i) + "";
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
        return titreColonnes[column];
    }
    
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Elève", "Classe", "Solvable?"};
        InterfaceLitige Ilitige = listeData.elementAt(rowIndex);
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
                        return Util.isSolvable(Ilitige);
                }
            } else {
                Vector<InterfaceEcheance> listeEcheances = Ilitige.getListeEcheances();
                int idex = columnIndex - 4;
                if (idex <= listeEcheances.size()-1) {
                    //System.out.println(columnIndex - 4);
                    return listeEcheances.elementAt(idex);
                }
            }
        }
        //System.out.println("NULL");
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
