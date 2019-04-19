/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF;

import SOURCES.UI.Panel;
import SOURCES.Utilitaires.Util;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import SOURCES.Interface.InterfaceAyantDroit;
import SOURCES.Interface.InterfaceClasse;
import SOURCES.Interface.InterfaceEcheance;
import SOURCES.Interface.InterfaceEleve;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceLitige;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.Utilitaires.SortiesLitiges;
import java.util.Vector;

/**
 *
 * @author Gateway
 */
public class DocumentPDF extends PdfPageEventHelper {

    private Document document = new Document(PageSize.A4);
    private Font Font_Titre1 = null;
    private Font Font_Titre2 = null;
    private Font Font_Titre3 = null;
    private Font Font_TexteSimple = null;
    private Font Font_TexteSimple_petit, Font_TexteSimple_petit_Gras = null;
    private Font Font_TexteSimple_Gras = null;
    private Font Font_TexteSimple_Italique = null;
    private Font Font_TexteSimple_Gras_Italique = null;
    public static final int ACTION_IMPRIMER = 0;
    public static final int ACTION_OUVRIR = 1;
    public SortiesLitiges sortiesLitiges = null;
    private Panel gestionnaireLitiges;
    private String nomFichier = "Litiges_S2B.pdf";
    private String[] titreColonnes = null;
    private int[] taillesColonnes = null;

    public DocumentPDF(Panel panel, int action, SortiesLitiges sortiesLitiges) {
        try {
            this.init(panel, action, sortiesLitiges);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Panel panel, int action, SortiesLitiges sortiesLitiges) {
        this.gestionnaireLitiges = panel;
        this.sortiesLitiges = sortiesLitiges;
        parametre_initialisation_fichier();
        parametre_construire_fichier();
        if (action == ACTION_OUVRIR) {
            parametres_ouvrir_fichier();
        } else if (action == ACTION_IMPRIMER) {
            parametres_imprimer_fichier();
        }
    }

    private void parametre_initialisation_fichier() {
        //Les titres du document
        this.Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.DARK_GRAY);
        this.Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        this.Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        //Les textes simples
        this.Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        this.Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);
    }

    private void parametre_construire_fichier() {
        try {
            if (this.gestionnaireLitiges != null) {
                nomFichier = this.gestionnaireLitiges.getNomfichierPreuve();
            }
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            writer.setPageEvent(new MarqueS2B());
            this.document.open();
            this.setDonneesBibliographiques();
            this.setContenuDeLaPage();
            this.document.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionnaireLitiges, "Impossible de produire la facture\nAssurez vous qu'aucun fichier du même nom ne soit actuellement ouvert.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parametres_ouvrir_fichier() {
        if (gestionnaireLitiges != null) {
            nomFichier = gestionnaireLitiges.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
                if (sortiesLitiges != null) {
                    sortiesLitiges.getEcouteurEnregistrement().onDone("PDF ouvert avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'ouvrir le fichier !";
                JOptionPane.showMessageDialog(gestionnaireLitiges, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesLitiges != null) {
                    sortiesLitiges.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void parametres_imprimer_fichier() {
        if (gestionnaireLitiges != null) {
            nomFichier = gestionnaireLitiges.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
                if (sortiesLitiges != null) {
                    sortiesLitiges.getEcouteurEnregistrement().onDone("Impression effectuée avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'imprimer les données ";
                JOptionPane.showMessageDialog(gestionnaireLitiges, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesLitiges != null) {
                    sortiesLitiges.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void setDonneesBibliographiques() {
        this.document.addTitle("Document généré par JS2BFacture");
        this.document.addSubject("Etat");
        this.document.addKeywords("Java, PDF, Contentieux");
        this.document.addAuthor("S2B. Simple.Intuitif");
        this.document.addCreator("SULA BOSIO Serge, S2B, sulabosiog@gmail.com");
    }

    private void ajouterLigne(int number) throws Exception {
        Paragraph paragraphe = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraphe.add(new Paragraph(" "));
        }
        this.document.add(paragraphe);
    }

    private void setTitreEtDateDocument() throws Exception {
        Paragraph preface = new Paragraph();
        if (gestionnaireLitiges != null) {
            preface.add(getParagraphe("Date: " + gestionnaireLitiges.getDateDocument(), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe(gestionnaireLitiges.getTitreDoc(), Font_Titre1, Element.ALIGN_CENTER));
        }
        this.document.add(preface);
    }

    private void setSignataire() throws Exception {
        if (gestionnaireLitiges != null) {
            this.document.add(getParagraphe(""
                    + "Produit par " + gestionnaireLitiges.getNomUtilisateur() + "\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        } else {
            this.document.add(getParagraphe(""
                    + "Produit par Serge SULA BOSIO\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        }

    }

    private void setBasDePage() throws Exception {
        if (gestionnaireLitiges != null) {
            InterfaceEntreprise entreprise = gestionnaireLitiges.getEntreprise();
            if (entreprise != null) {
                this.document.add(getParagraphe(entreprise.getNom() + "\n" + entreprise.getAdresse() + " | " + entreprise.getTelephone() + " | " + entreprise.getEmail() + " | " + entreprise.getSiteWeb(), Font_TexteSimple, Element.ALIGN_CENTER));
            } else {
                addDefaultEntreprise();
            }
        } else {
            addDefaultEntreprise();
        }
    }

    private void addDefaultEntreprise() throws Exception {
        this.document.add(getParagraphe(""
                + "UAP RDC Sarl. Courtier d’Assurances n°0189\n"
                + "Prins van Luikschool, Av de la Gombe, Gombe, Kinshasa, DRC | (+243) 975 33 88 33 | info@aib-brokers.com", Font_TexteSimple, Element.ALIGN_CENTER));
    }

    private Paragraph getParagraphe(String texte, Font font, int alignement) {
        Paragraph par = new Paragraph(texte, font);
        par.setAlignment(alignement);
        return par;
    }

    private Phrase getPhrase(String texte, Font font) {
        Phrase phrase = new Phrase(texte, font);
        return phrase;
    }

    private void setLogoEtDetailsEntreprise() {
        try {
            PdfPTable tableauEnteteFacture = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableauEnteteFacture.setWidths(dimensionsWidthHeight);
            tableauEnteteFacture.setHorizontalAlignment(Element.ALIGN_LEFT);

            //CELLULE DU LOGO DE L'ENTREPRISE
            PdfPCell celluleLogoEntreprise = null;
            String logo = "";
            if (gestionnaireLitiges != null) {
                logo = gestionnaireLitiges.getEntreprise().getLogo();
            }
            File ficLogo = new File(logo);
            if (ficLogo.exists() == true) {
                //Chargement du logo et redimensionnement afin que celui-ci convienne dans l'espace qui lui est accordé
                Image Imglogo = Image.getInstance(logo);
                Imglogo.scaleAbsoluteWidth(70);
                Imglogo.scaleAbsoluteHeight(70);
                celluleLogoEntreprise = new PdfPCell(Imglogo);
            } else {
                celluleLogoEntreprise = new PdfPCell();
            }
            celluleLogoEntreprise.setPadding(2);
            celluleLogoEntreprise.setBorderWidth(0);
            celluleLogoEntreprise.setBorderColor(BaseColor.BLACK);
            tableauEnteteFacture.addCell(celluleLogoEntreprise);

            //CELLULE DES DETAILS SUR L'ENTREPRISE - TEXTE (Nom, Adresse, Téléphone, Email, etc)
            PdfPCell celluleDetailsEntreprise = new PdfPCell();
            celluleDetailsEntreprise.setPadding(2);
            celluleDetailsEntreprise.setPaddingLeft(5);
            celluleDetailsEntreprise.setBorderWidth(0);
            celluleDetailsEntreprise.setBorderWidthLeft(1);
            celluleDetailsEntreprise.setBorderColor(BaseColor.BLACK);
            celluleDetailsEntreprise.setHorizontalAlignment(Element.ALIGN_TOP);

            if (gestionnaireLitiges != null) {
                InterfaceEntreprise entreprise = gestionnaireLitiges.getEntreprise();
                if (entreprise != null) {
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getNom(), Font_Titre2, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getAdresse(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getSiteWeb() + " | " + entreprise.getEmail() + " | " + entreprise.getTelephone(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe("RCC : " + entreprise.getRCCM() + "\nID. NAT : " + entreprise.getIDNAT() + "\nNIF : " + entreprise.getNumeroImpot(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                }
            } else {
                celluleDetailsEntreprise.addElement(getParagraphe("UAP RDC Sarl, Courtier d'Assurances n°0189", Font_Titre2, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("Avenue de la Gombe, Kinshasa/Gombe", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("https://www.aib-brokers.com | info@aib-brokers.com | (+243)84 480 35 14 - (+243)82 87 27 706", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("RCC : CDF/KIN/2015-1245\nID. NAT : 0112487789\nNIF : 012245", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            }
            tableauEnteteFacture.addCell(celluleDetailsEntreprise);

            //On insère le le tableau entete (logo et détails de l'entreprise) dans la page
            document.add(tableauEnteteFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTitresColonnes() {
        Vector titresCols = new Vector();
        //Premier groupe
        titresCols.add("N°");
        titresCols.add("Elève");
        titresCols.add("Classe");
        titresCols.add("Solvable?");

        //Deuxième groupe
        Vector<String> tabTemp = Util.getTablePeriodes(gestionnaireLitiges.getModeleListeLitiges());
        if (!tabTemp.isEmpty()) {
            for (String nomPeriode : tabTemp) {
                titresCols.add(nomPeriode);
            }
        }

        //On verse les titres dans le tableau static
        titreColonnes = new String[titresCols.size()];
        taillesColonnes = new int[titresCols.size()];
        for (int i = 0; i < titreColonnes.length; i++) {
            titreColonnes[i] = titresCols.elementAt(i) + "";
            //On précise les tailles de chaque colonne
            switch (i) {
                case 0:
                    taillesColonnes[i] = 40;   //N°
                    break;
                case 1:
                    taillesColonnes[i] = 250;  //Elève
                    break;
                case 2:
                    taillesColonnes[i] = 50;   //Classe
                    break;
                case 3:
                    taillesColonnes[i] = 60;   //Solvable?
                    break;
                default:
                    taillesColonnes[i] = 120;  //Pour chaque période...
                    break;
            }
        }
    }

    private String getClasse(int idClasse) {
        for (InterfaceClasse Iclasse : gestionnaireLitiges.getParametresLitige().getListeClasse()) {
            if (idClasse == Iclasse.getId()) {
                return Iclasse.getNom();
            }
        }
        return "";
    }

    private String getEleve(int idEleve) {
        for (InterfaceEleve Ieleve : gestionnaireLitiges.getDonneesLitige().getEleves()) {
            if (idEleve == Ieleve.getId()) {
                String txtAyantDroit = (isAyanDroit(idEleve)?"(*) ":"");
                return txtAyantDroit + "" +Ieleve.getNom() + " " + Ieleve.getPostnom() + " " + Ieleve.getPrenom();
            }
        }
        return "";
    }

    private boolean isAyanDroit(int idEleve) {
        for (InterfaceAyantDroit Iaya : gestionnaireLitiges.getDonneesLitige().getListeAyantDroits()) {
            if (idEleve == Iaya.getIdEleve()) {
                return true;
            }
        }
        return false;
    }

    private void setTableauDetailsLitiges() {
        /* */
        try {
            initTitresColonnes();
            PdfPTable tableLitiges = getTableau(
                    -1,
                    titreColonnes,
                    taillesColonnes,
                    Element.ALIGN_CENTER,
                    0.2f
            );

            double totPaye = 0, totReste = 0;
            if (gestionnaireLitiges != null) {
                ModeleListeLitiges modelLitiges = this.gestionnaireLitiges.getModeleListeLitiges();
                int i = 0;
                totPaye = 0;
                for (InterfaceLitige iLitige : modelLitiges.getListeData()) {
                    //cumuls
                    String nomEleve = getEleve(iLitige.getIdEleve());
                    String nomClasse = getClasse(iLitige.getIdClasse());
                    String isSolvable = Util.isSolvable(iLitige) ? "Oui" : "Non";
                    setLigneTabLitige(tableLitiges, i, nomEleve, nomClasse, isSolvable, iLitige.getListeEcheances());
                    i++;
                }
                //setDerniereLigneTabReleve(tableLitiges, gestionnaireFacture.getParametres().getMonnaieOutPut().getCode(), totPaye, totReste);
            }
            document.add(getParagraphe("Détails.", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            document.add(tableLitiges);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCriteresFiltre() {
        try {
            String txtCriteres = "Elève: [" + gestionnaireLitiges.getCritereEleve() + "]\n"
                    + "Classe: [" + gestionnaireLitiges.getCritereClasse() + "]\n"
                    + "Frais: [" + gestionnaireLitiges.getCritereFrais() + "]\n"
                    + "Période: [" + gestionnaireLitiges.getCriterePeriode() + "]\n"
                    + "Solvabilité: [" + gestionnaireLitiges.getCritereSolvabilite() + "]";
            document.add(getParagraphe("CRITERES DE SELECTION", Font_TexteSimple_Gras, Element.ALIGN_LEFT));
            document.add(getParagraphe(txtCriteres, Font_TexteSimple, Element.ALIGN_LEFT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauSynthese() {
        try {
            PdfPTable tableSynthese = getTableau(
                    120f,
                    new String[]{"Synthèse", ""},
                    new int[]{120, 120},
                    Element.ALIGN_LEFT,
                    0
            );
            if (gestionnaireLitiges != null) {
                ModeleListeLitiges mLitiges = gestionnaireLitiges.getModeleListeLitiges();
                if (mLitiges != null) {
                    setLignesTabSynthese(tableSynthese, 0, gestionnaireLitiges.getParametresLitige().getMonnaieOutPut().getCode(), mLitiges.getTotaux());
                }
            } else {
                setLignesTabSynthese(tableSynthese, 0, "$", new double[]{100, 80, 20});
            }
            document.add(tableSynthese);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPTable getTableau(float totalWidth, String[] titres, int[] widths, int alignement, float borderWidth) {
        try {
            PdfPTable tableau = new PdfPTable(widths.length);
            if (totalWidth != -1) {
                tableau.setTotalWidth(totalWidth);
            } else {
                tableau.setTotalWidth(PageSize.A4.getWidth() - 72);
            }
            tableau.setLockedWidth(true);
            tableau.setWidths(widths);
            tableau.setHorizontalAlignment(alignement);
            if (titres != null) {
                tableau.setSpacingBefore(3);
                for (String titre : titres) {
                    tableau.addCell(getCelluleTableau(titre, borderWidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_CENTER, Font_TexteSimple_Gras));
                }
            }

            return tableau;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private InterfaceEcheance getEcheance(String nomEcheance, Vector<InterfaceEcheance> listeEchenaces) {
        InterfaceEcheance IecheEnCours = null;
        for (InterfaceEcheance Ieche : listeEchenaces) {
            if (Ieche.getNom().equals(nomEcheance)) {
                IecheEnCours = Ieche;
            }
        }
        return IecheEnCours;
    }

    private void setLigneTabLitige(PdfPTable tableDetailsArticles, int i, String eleve, String classe, String solvabilite, Vector<InterfaceEcheance> listeEchenaces) {
        tableDetailsArticles.addCell(getCelluleTableau("" + (i + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(eleve, 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(classe, 0.2f, BaseColor.WHITE, null, Element.ALIGN_CENTER, Font_TexteSimple));
        tableDetailsArticles.addCell(getCelluleTableau(solvabilite, 0.2f, BaseColor.WHITE, null, Element.ALIGN_CENTER, Font_TexteSimple));

        //On parcours maintenant les échéances
        for (String nomPeriode : Util.getTablePeriodes(gestionnaireLitiges.getModeleListeLitiges())) {
            InterfaceEcheance IecheEnCours = getEcheance(nomPeriode, listeEchenaces);
            if (IecheEnCours != null) {
                String monnaie = Util.getMonnaie(gestionnaireLitiges.getParametresLitige(), IecheEnCours.getIdMonnaie()).getCode();
                tableDetailsArticles.addCell(getCelluleTableau(Util.getMontantFrancais(IecheEnCours.getMontantPaye()) + " " + monnaie + " / " + Util.getMontantFrancais(IecheEnCours.getMontantDu()) + " " + monnaie, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            }else{
                tableDetailsArticles.addCell(getCelluleTableau(" ", 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
            }
        }
    }

    private void setLignesTabSynthese(PdfPTable tableau, float borderwidth, String monnaie, double[] tabTotaux) {
        tableau.addCell(getCelluleTableau("Montant Dû", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(tabTotaux[0]) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        tableau.addCell(getCelluleTableau("Montant payé", borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_LEFT, Font_TexteSimple_Italique));
        tableau.addCell(getCelluleTableau("- " + Util.getMontantFrancais(tabTotaux[1]) + " " + monnaie, borderwidth, BaseColor.WHITE, BaseColor.RED, Element.ALIGN_RIGHT, Font_TexteSimple_Italique));

        tableau.addCell(getCelluleTableau("Solde global", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(Util.getMontantFrancais(tabTotaux[2]) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

    }

    private void setDetailsBanque(PdfPTable tableau, float borderwidth) {
        if (gestionnaireLitiges != null) {
            InterfaceEntreprise entreprise = gestionnaireLitiges.getEntreprise();
            if (entreprise != null) {
                if (entreprise.getBanque().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Banque :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getBanque() + "", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getIntituleCompte().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Intitulé du compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getIntituleCompte() + "", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getNumeroCompte().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("N° de compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getNumeroCompte(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getCodeSwift().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("Code Swift :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getCodeSwift(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
                if (entreprise.getIBAN().trim().length() != 0) {
                    tableau.addCell(getCelluleTableau("IBAN :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                    tableau.addCell(getCelluleTableau(entreprise.getIBAN(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
                }
            } else {
                setDefaultDetailsBancaires(tableau, borderwidth);
            }
        } else {
            setDefaultDetailsBancaires(tableau, borderwidth);
        }
    }

    private void setDefaultDetailsBancaires(PdfPTable tableau, float borderwidth) {
        tableau.addCell(getCelluleTableau("Banque :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("EquityBank Congo SA", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("Intitulé du compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("UAP RDC Sarl", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("N° de compte :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("00018000010267415120011", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

        tableau.addCell(getCelluleTableau("Code Swift :", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau("PRCBCDKI", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));

    }

    private PdfPCell getCelluleTableau(String texte, float BorderWidth, BaseColor background, BaseColor textColor, int alignement, Font font) {
        PdfPCell cellule = new PdfPCell();
        cellule.setBorderWidth(BorderWidth);
        if (background != null) {
            cellule.setBackgroundColor(background);
        } else {
            cellule.setBackgroundColor(BaseColor.WHITE);
        }
        if (textColor != null) {
            font.setColor(textColor);
        } else {
            font.setColor(BaseColor.BLACK);
        }
        cellule.setHorizontalAlignment(alignement);
        cellule.setPhrase(getPhrase(texte, font));
        return cellule;
    }

    private void setTableauDetailsBancaires() {
        try {
            PdfPTable tableBanque = getTableau(
                    400f,
                    null,
                    new int[]{100, 300},
                    Element.ALIGN_LEFT,
                    0
            );
            setDetailsBanque(tableBanque, 0);
            Font_TexteSimple_Italique.setColor(BaseColor.BLACK);
            document.add(getParagraphe("Référence bancaire", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            document.add(tableBanque);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setContenuDeLaPage() throws Exception {
        if (sortiesLitiges != null) {
            sortiesLitiges.getEcouteurEnregistrement().onUploading("Construction du contenu...");
        }
        setLogoEtDetailsEntreprise();
        setTitreEtDateDocument();
        setCriteresFiltre();
        ajouterLigne(1);
        setTableauSynthese();
        ajouterLigne(1);
        setTableauDetailsLitiges();
        ajouterLigne(1);
        setSignataire();
        //setLigneSeparateur();
        setTableauDetailsBancaires();
        setLigneSeparateur();
        setBasDePage();
        if (sortiesLitiges != null) {
            sortiesLitiges.getEcouteurEnregistrement().onUploading("Finalisation...");
        }
    }

    private void setLigneSeparateur() {
        try {
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            document.add(linebreak);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        //Exemple
        DocumentPDF docpdf = new DocumentPDF(null, ACTION_OUVRIR, null);
    }

}
