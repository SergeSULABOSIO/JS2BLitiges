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
import SOURCES.Utilitaires.XX_Litige;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeLitiges extends AbstractTableModel {

    private String[] titreColonnes = null;
    private Vector<InterfaceLitige> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private ParametresLitige parametresLitige;
    private DonneesLitige donneesLitige;

    public ModeleListeLitiges(JScrollPane parent, DonneesLitige donneesLitige, ParametresLitige parametresLitige, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.parametresLitige = parametresLitige;
        this.donneesLitige = donneesLitige;

        //C'est ici qu'il faut charger automatiquement les données
        //CAlculer les litiges, puis les afficher
        for (InterfaceEleve Ieleve : donneesLitige.getEleves()) {
            Vector<InterfaceEcheance> listeEcheances = GestionLitiges.getEcheances(parametresLitige.getArticles(), parametresLitige.getPaiements(), parametresLitige);
            if (listeEcheances != null) {
                //System.out.println("NB Echeances: " + listeEcheances.size());
                listeData.add(new XX_Litige(1, Ieleve.getId(), Ieleve.getIdClasse(), listeEcheances, InterfaceLitige.BETA_EXISTANT));
            }
        }
    }

    public void chercher(String modeCle, int idClasse, int idFrais) {

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
        ecouteurModele.onValeurChangee();
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
        //{"N°", "Elève", "Classe"};
        InterfaceLitige Ilitige = listeData.elementAt(rowIndex);
        if (Ilitige != null) {
            if (columnIndex < 3) {
                switch (columnIndex) {
                    case 0:
                        return (rowIndex + 1) + "";
                    case 1:
                        return Ilitige.getIdEleve();
                    case 2:
                        return Ilitige.getIdClasse();
                }
            } else {
                Vector<InterfaceEcheance> listeEcheances = Ilitige.getListeEcheances();
                if(listeEcheances != null){
                    return listeEcheances.elementAt(columnIndex - 3);
                }else{
                    return null;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Nom", "Capacité"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Integer.class;//Elève
            case 2:
                return Integer.class;//Classe
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
        //{"N°", "Elève", "Classe"};
        //On ne peut pas modifier cette liste
    }

}
