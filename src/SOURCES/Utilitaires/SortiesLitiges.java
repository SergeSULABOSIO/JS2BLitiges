/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.Interface.InterfaceLitige;
import java.util.Vector;

/**
 *
 * @author user
 */
public class SortiesLitiges {
    private Vector<InterfaceLitige> paiements;
    private EcouteurEnregistrement ecouteurEnregistrement;

    public SortiesLitiges(Vector<InterfaceLitige> paiements, EcouteurEnregistrement ecouteurEnregistrement) {
        this.paiements = paiements;
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }
    
    public Vector<InterfaceLitige> getPaiements() {
        return paiements;
    }

    public void setPaiements(Vector<InterfaceLitige> paiements) {
        this.paiements = paiements;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    @Override
    public String toString() {
        return "SortiesLitiges{" + "paiements=" + paiements + ", ecouteurEnregistrement=" + ecouteurEnregistrement + '}';
    }
}
