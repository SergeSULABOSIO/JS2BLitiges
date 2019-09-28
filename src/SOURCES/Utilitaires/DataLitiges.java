/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

/**
 *
 * @author HP Pavilion
 */
public class DataLitiges {
    public ParametresLitige parametresLitige;

    public DataLitiges(ParametresLitige parametresLitige) {
        this.parametresLitige = parametresLitige;
    }
    
    public ParametresLitige getParametresLitige() {
        return parametresLitige;
    }

    public void setParametresLitige(ParametresLitige parametresLitige) {
        this.parametresLitige = parametresLitige;
    }

    @Override
    public String toString() {
        return "DataLitiges{" + "parametresLitige=" + parametresLitige + '}';
    }

    
    
}
