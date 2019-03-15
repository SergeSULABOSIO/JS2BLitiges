/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceLitige;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class XX_Litige implements InterfaceLitige{
    public int id;
    public int idEleve;
    public int idClasse;
    public Vector<InterfaceEcheance> listeEcheances;
    public int beta;

    public XX_Litige(int id, int idEleve, int idClasse, Vector<InterfaceEcheance> listeEcheances, int beta) {
        this.id = id;
        this.idEleve = idEleve;
        this.idClasse = idClasse;
        this.listeEcheances = listeEcheances;
        this.beta = beta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(int idEleve) {
        this.idEleve = idEleve;
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }

    public Vector<InterfaceEcheance> getListeEcheances() {
        return listeEcheances;
    }

    public void setListeEcheances(Vector<InterfaceEcheance> listeEcheances) {
        this.listeEcheances = listeEcheances;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "XX_Litige{" + "id=" + id + ", idEleve=" + idEleve + ", idClasse=" + idClasse + ", listeEcheances=" + listeEcheances + ", beta=" + beta + '}';
    }
}
