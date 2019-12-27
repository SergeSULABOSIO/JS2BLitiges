/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.ConstanteViewer;
import SOURCES.DetailViewer;
import SOURCES.GenerateurPDF.DocumentPDFLitige;
import SOURCES.ModelesTables.ModeleListeLitiges;
import SOURCES.ProprieteViewer;
import SOURCES.RendusTables.RenduTableLitiges;
import SOURCES.Utilitaires.DataLitiges;
import SOURCES.Utilitaires.ParametresLitige;
import SOURCES.Utilitaires.SortiesLitiges;
import SOURCES.Utilitaires.UtilLitige;
import Source.Callbacks.EcouteurEnregistrement;
import Source.Callbacks.EcouteurUpdateClose;
import Source.Callbacks.EcouteurValeursChangees;
import Source.Callbacks.EcouteurCrossCanal;
import Source.Callbacks.EcouteurFreemium;
import Source.GestionClickDroit;
import Source.Interface.InterfaceEntreprise;
import Source.Interface.InterfaceLitige;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Ayantdroit;
import Source.Objet.Classe;
import Source.Objet.CouleurBasique;
import Source.Objet.Echeance;
import Source.Objet.Eleve;
import Source.Objet.Frais;
import Source.Objet.Litige;
import Source.Objet.Monnaie;
import Source.Objet.Periode;
import Source.Objet.Utilisateur;
import Source.UI.NavigateurPages;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP Pavilion
 */
public class PanelLitige extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    private Icones icones = null;
    private final JTabbedPane parent;
    private PanelLitige moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    public Bouton btImprimer, btPDF, btFermer, btActualiser, btPaiements, btInscription; //, btPDFSynth;        //btEnregistrer, btAjouter, btSupprimer, btVider, 
    public RubriqueSimple mImprimer, mPDF, mFermer, mActualiser, mPaiements, mInscription; //, mPDFSynth;     //mEnregistrer, mAjouter, mSupprimer, mVider, 
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;

    private ModeleListeLitiges modeleListeLitiges;
    public int indexTabSelected = -1;
    public DataLitiges dataLitiges;
    public double totMontantDu, totMontantPaye, totMontantNet = 0;
    public double totMontantDuSelected, totMontantPayeSelected, totMontantNetSelected = 0;

    public String monnaieOutput = "";
    public static final int TYPE_EXPORT_TOUT = 0;
    public static final int TYPE_EXPORT_SELECTION = 1;
    public int typeExport = TYPE_EXPORT_TOUT;

    private Litige SelectedLitige = null;
    public String selectedEleve = "";
    private Eleve SelectedEleve = null;
    private Monnaie monnaieLocal = null;
    private CouleurBasique couleurBasique;
    private JProgressBar progress;
    private EcouteurCrossCanal ecouteurCrossCanal;
    private Vector<Eleve> listeEleves = new Vector<>();
    private Vector<Ayantdroit> listeAyantDroit = new Vector<>();
    private EcouteurFreemium ef = null;

    public PanelLitige(EcouteurFreemium ef, CouleurBasique couleurBasique, JTabbedPane parent, DataLitiges dataLitiges, JProgressBar progress, EcouteurCrossCanal ecouteurCrossCanal) {
        this.initComponents();
        this.ef = ef;
        this.ecouteurCrossCanal = ecouteurCrossCanal;
        this.progress = progress;
        this.couleurBasique = couleurBasique;
        this.icones = new Icones();
        this.parent = parent;
        this.dataLitiges = dataLitiges;
        this.init();
        //Initialisaterus
        parametrerTableLitiges();
        setIconesTabs();
        initMonnaieTotaux();
        actualiserTotaux();
        ecouterLitigeSelectionne();
        onEcouterComboMonnaie();

        //On ecoute les click droit afin d'afficher le menu contextuel
        new GestionClickDroit(menuContextuel, tableListeLitige, scrollListeLitiges).init();
    }

    private void onEcouterComboMonnaie() {
        ItemListener ecouteurComboMonnaieOutput = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                actualiserTotaux("combototMonnaieItemStateChanged");
            }
        };
        chMonnaie.addItemListener(ecouteurComboMonnaieOutput);
    }

    public boolean verifierNomEleve(String nomEleve, Eleve Ieleve) {
        boolean reponse = false;
        if (nomEleve.trim().length() == 0) {
            reponse = true;
        } else {
            reponse = ((UtilLitige.contientMotsCles(Ieleve.getNom() + " " + Ieleve.getPostnom() + " " + Ieleve.getPrenom(), nomEleve)));
        }
        return reponse;
    }

    public boolean verifierClasse(String nomClasse, Eleve Ieleve) {
        boolean reponse = false;
        if (nomClasse.trim().length() == 0) {
            reponse = true;
        } else {
            Classe cls = getClasse(nomClasse);
            if (cls != null) {
                reponse = (cls.getId() == Ieleve.getIdClasse());
            } else {
                return false;
            }
        }
        return reponse;
    }

    public Classe getClasse(String nomClasse) {
        for (Classe cls : dataLitiges.getParametresLitige().getListeClasse()) {
            if (cls.getNom().toLowerCase().trim().equals(nomClasse.toLowerCase().trim())) {
                return cls;
            }
        }
        return null;
    }

    public Periode getPeriode(String nomPeriode) {
        for (Periode cls : dataLitiges.getParametresLitige().getListePeriodes(-1)) {
            if (cls.getNom().toLowerCase().trim().equals(nomPeriode.toLowerCase().trim())) {
                return cls;
            }
        }
        return null;
    }

    public Frais getFrais(String nomFrais) {
        for (Frais cls : dataLitiges.getParametresLitige().getListeFraises()) {
            if (cls.getNom().toLowerCase().trim().equals(nomFrais.toLowerCase().trim())) {
                return cls;
            }
        }
        return null;
    }

    public NavigateurPages getNavigateurPage() {
        return navigateurPage;
    }

    public void setDonneesLitiges(Litige newLitige, Eleve eleve, Ayantdroit ayantdroit) {
        if (!listeEleves.contains(eleve) && eleve != null) {
            listeEleves.add(eleve);
        }
        if (!listeAyantDroit.contains(ayantdroit) && ayantdroit != null) {
            listeAyantDroit.add(ayantdroit);
        }
        if (modeleListeLitiges != null && newLitige != null) {
            modeleListeLitiges.setDonneesLitiges(newLitige);
        }
        if (navigateurPage != null) {
            navigateurPage.patienter(false, "Prêt.");
        }
    }

    public Vector<Eleve> getListeEleves() {
        return listeEleves;
    }

    public void setListeEleves(Vector<Eleve> listeEleves) {
        this.listeEleves = listeEleves;
    }

    public Vector<Ayantdroit> getListeAyantDroit() {
        return listeAyantDroit;
    }

    public void setListeAyantDroit(Vector<Ayantdroit> listeAyantDroit) {
        this.listeAyantDroit = listeAyantDroit;
    }

    public void reiniliserLitige() {
        if (listeEleves != null) {
            listeEleves.removeAllElements();
        }
        if (listeAyantDroit != null) {
            listeAyantDroit.removeAllElements();
        }
        if (modeleListeLitiges != null) {
            modeleListeLitiges.reinitialiserListe();
        }
    }

    public ModeleListeLitiges getModeleListeLitiges() {
        return modeleListeLitiges;
    }

    public InterfaceEntreprise getEntreprise() {
        return dataLitiges.getParametresLitige().getEntreprise();
    }

    public String getNomUtilisateur() {
        return dataLitiges.getParametresLitige().getUtilisateur().getNom();
    }

    public String getTitreDoc() {
        if (typeExport == PanelLitige.TYPE_EXPORT_SELECTION) {
            return "LITIGES";
        } else {
            return "LITIGE - LISTE GLOBALE";
        }
    }

    public String getDateDocument() {
        return UtilLitige.getDateFrancais(new Date());
    }

    public int getTypeExport() {
        return typeExport;
    }

    public int getTailleResultatLitiges() {
        if (modeleListeLitiges != null) {
            return modeleListeLitiges.getListeData().size();
        }
        return 0;
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change: ";
        chMonnaie.removeAllItems();
        for (Monnaie monnaie : dataLitiges.getParametresLitige().getListeMonnaies()) {
            chMonnaie.addItem(monnaie.getCode());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (InterfaceMonnaie monnaie : dataLitiges.getParametresLitige().getListeMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + UtilLitige.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private void initTranchesFrais(Vector<ProprieteViewer> proprietes) {
        if (modeleListeLitiges != null) {
            if (!modeleListeLitiges.getListeData().isEmpty()) {
                for (Echeance Ieche : modeleListeLitiges.getListeData().firstElement().getListeEcheances()) {
                    String periode = "" + UtilLitige.getDateFrancais(Ieche.getDateInitiale()) + "-" + UtilLitige.getDateFrancais(Ieche.getDateFinale());
                    proprietes.add(new ProprieteViewer(Ieche.getNom(), periode, ProprieteViewer.TYPE_PERIODE));
                }
            }
        }
    }

    private Monnaie getSelectedMonnaieTotaux() {
        for (Monnaie monnaie : dataLitiges.getParametresLitige().getListeMonnaies()) {
            if ((monnaie.getCode()).equals(chMonnaie.getSelectedItem() + "")) {
                return monnaie;
            }
        }
        return null;
    }

    private Monnaie getMonnaie(int idMonnaie) {
        for (Monnaie monnaie : dataLitiges.getParametresLitige().getListeMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private void getMontants(Monnaie ImonnaieOutput, Litige Ilitige) {
        if (Ilitige != null && ImonnaieOutput != null) {
            if (Ilitige.getListeEcheances() != null) {
                for (Echeance Iecheance : Ilitige.getListeEcheances()) {
                    if (ImonnaieOutput.getId() == Iecheance.getIdMonnaie()) {
                        totMontantDu += Iecheance.getMontantDu();
                        totMontantPaye += Iecheance.getMontantPaye();
                        totMontantNet += (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
                    } else {
                        Monnaie ImonnaieOrigine = getMonnaie(Iecheance.getIdMonnaie());
                        if (ImonnaieOrigine != null) {
                            totMontantDu += (Iecheance.getMontantDu() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                            totMontantPaye += (Iecheance.getMontantPaye() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                            totMontantNet += ((Iecheance.getMontantDu() - Iecheance.getMontantPaye()) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        }
                    }
                }
            }

        }
    }

    private void getMontantsSelection(Monnaie ImonnaieOutput, Litige Ilitige) {
        if (Ilitige != null && ImonnaieOutput != null) {
            if (Ilitige.getListeEcheances() != null) {
                for (Echeance Iecheance : Ilitige.getListeEcheances()) {
                    if (ImonnaieOutput.getId() == Iecheance.getIdMonnaie()) {
                        totMontantDuSelected += Iecheance.getMontantDu();
                        totMontantPayeSelected += Iecheance.getMontantPaye();
                        totMontantNetSelected += (Iecheance.getMontantDu() - Iecheance.getMontantPaye());
                    } else {
                        Monnaie ImonnaieOrigine = getMonnaie(Iecheance.getIdMonnaie());
                        if (ImonnaieOrigine != null) {
                            totMontantDuSelected += (Iecheance.getMontantDu() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                            totMontantPayeSelected += (Iecheance.getMontantPaye() * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                            totMontantNetSelected += ((Iecheance.getMontantDu() - Iecheance.getMontantPaye()) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                        }
                    }
                }
            }
        }
    }

    private void actualiserTotaux() {
        //Montant brut
        totMontantDu = 0;
        totMontantDuSelected = 0;

        //Montant payé
        totMontantPaye = 0;
        totMontantPayeSelected = 0;

        //Net à payer (Solde restant dû)
        totMontantNet = 0;
        totMontantNetSelected = 0;

        //Pour les Montants globaux (Montant du, montant payé et Reste à payer)
        Monnaie ImonnaieOutput = null;
        if (modeleListeLitiges != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (Litige iFiche : modeleListeLitiges.getListeData()) {
                getMontants(ImonnaieOutput, iFiche);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeLitige.getSelectedRows();
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeLitiges != null) {
                    Litige iFiche = modeleListeLitiges.getLitiges(tabLignesSelected[i]);
                    if (iFiche != null && ImonnaieOutput != null) {
                        getMontantsSelection(ImonnaieOutput, iFiche);
                    }
                }
            }
        }

        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }

        //Le global
        valMontDu.setText(UtilLitige.getMontantFrancais(totMontantDu) + " " + monnaieOutput);
        valMontPaye.setText(UtilLitige.getMontantFrancais(totMontantPaye) + " " + monnaieOutput);
        valMontReste.setText(UtilLitige.getMontantFrancais(totMontantNet) + " " + monnaieOutput);

        //L'élements séléctionné
        valMontDuSelect.setText(UtilLitige.getMontantFrancais(totMontantDuSelected) + " " + monnaieOutput);
        valMontPayeSelect.setText(UtilLitige.getMontantFrancais(totMontantPayeSelected) + " " + monnaieOutput);
        valMontResteSelect.setText(UtilLitige.getMontantFrancais(totMontantNetSelected) + " " + monnaieOutput);

        aficherProprietes();
    }

    public double getTotMontantDu() {
        return totMontantDu;
    }

    public double getTotMontantPaye() {
        return totMontantPaye;
    }

    public double getTotMontantNet() {
        return totMontantNet;
    }

    public double getTotMontantDuSelected() {
        return totMontantDuSelected;
    }

    public double getTotMontantPayeSelected() {
        return totMontantPayeSelected;
    }

    public double getTotMontantNetSelected() {
        return totMontantNetSelected;
    }

    public String getMonnaieOutput() {
        return this.monnaieOutput;
    }

    public String getTauxChange() {
        return labTauxDeChange.getText();
    }

    private void actualiserTotaux(String methode) {
        //System.out.println(methode);
        actualiserTotaux();
    }

    private void parametrerTableLitiges() {
        initModelTableLitige("", -1, -1, -1, -1);
        fixerColonnesTableLitige(false);
        ecouterSelectionTableLitige();
    }

    private void initModelTableLitige(String nomEleve, int idClasse, int idFrais, int idPeriode, int idSolvabilite) {
        //C'est justement ici que l'on va charger les litiges après les avoir calculés
        this.modeleListeLitiges = new ModeleListeLitiges(progress, idSolvabilite, nomEleve, idClasse, idFrais, idPeriode, dataLitiges.getParametresLitige(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null && modeleListeLitiges != null) {
                    ecouteurClose.onActualiser(modeleListeLitiges.getRowCount() + " élement(s).", icones.getDossier_01());

                    if (progress != null) {
                        progress.setVisible(false);
                        progress.setIndeterminate(false);
                    }
                }
            }
        });

        //C'est cette portion qu'il faut appeler dans la méthode d'actualisation.
        tableListeLitige.setModel(this.modeleListeLitiges);
    }

    private void fixerColonnesTableLitige(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeLitige.setDefaultRenderer(Object.class, new RenduTableLitiges(couleurBasique, icones, modeleListeLitiges, listeEleves, listeAyantDroit, dataLitiges.getParametresLitige()));
        this.tableListeLitige.setRowHeight(25);

        //{"N°", "Elève", "Classe", + Echeances};
        setTaille(this.tableListeLitige.getColumnModel().getColumn(0), 30, true, null);//N°
        setTaille(this.tableListeLitige.getColumnModel().getColumn(1), 200, true, null);//Elève
        setTaille(this.tableListeLitige.getColumnModel().getColumn(2), 150, false, null);//Classe
        setTaille(this.tableListeLitige.getColumnModel().getColumn(3), 100, false, null);//Solvable?

        //Les écheances ou périodes
        if (modeleListeLitiges != null) {
            Vector<Periode> temptab = dataLitiges.getParametresLitige().getListePeriodes(-1);//UtilLitige.getTablePeriodes(modeleListeLitiges);
            for (int i = 0; i < temptab.size(); i++) {
                setTaille(this.tableListeLitige.getColumnModel().getColumn(4 + i), 150, true, null);//Tranche
            }
        }

        if (resizeTable == false) {
            this.tableListeLitige.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    private void ecouterSelectionTableLitige() {
        //On écoute les sélction
        this.tableListeLitige.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    actualiserTotaux("ecouterSelection - Table Litiges");
                    ecouterLitigeSelectionne();
                }
            }
        });
    }

    public Litige getSelectedLitige() {
        return SelectedLitige;
    }

    private void ecouterLitigeSelectionne() {
        int ligneSelected = tableListeLitige.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedLitige = modeleListeLitiges.getLitiges(ligneSelected);
            if (SelectedLitige != null) {
                this.SelectedEleve = getEleve(SelectedLitige.getIdEleve());
                if (SelectedEleve != null) {
                    selectedEleve = SelectedEleve.getNom() + " " + SelectedEleve.getPostnom() + " " + SelectedEleve.getPrenom();
                    renameTitrePaneAgent("Sélection - " + selectedEleve);

                    String brut = UtilLitige.getMontantFrancais(totMontantDuSelected) + " " + monnaieOutput;
                    String paye = UtilLitige.getMontantFrancais(totMontantPayeSelected) + " " + monnaieOutput;
                    String reste = UtilLitige.getMontantFrancais(totMontantNetSelected) + " " + monnaieOutput;
                    this.ecouteurClose.onActualiser(selectedEleve + ", Montant dû: " + brut + ", Payé: " + paye + " et " + reste + ".", icones.getClient_01());

                    //On actualise les boutons qui doivent être personnalisés
                    btPaiements.setInfosBulle("Accéder aux paiement de " + selectedEleve);
                    btPaiements.appliquerDroitAccessDynamique(true);
                    mPaiements.appliquerDroitAccessDynamique(true);
                    //Inscription
                    btInscription.appliquerDroitAccessDynamique(true);
                    mInscription.appliquerDroitAccessDynamique(true);
                }
            }
        }
    }

    private void renameTitrePaneAgent(String titre) {
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, titre, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(51, 51, 255))); // NOI18N
    }

    private Eleve getEleve(int idEleve) {
        for (Eleve Icha : listeEleves) {
            if (Icha.getId() == idEleve) {
                return Icha;
            }
        }
        return null;
    }

    private void setTaille(TableColumn column, int taille, boolean fixe, TableCellEditor editor) {
        column.setPreferredWidth(taille);
        if (fixe == true) {
            column.setMaxWidth(taille);
            column.setMinWidth(taille);
        }
        if (editor != null) {
            column.setCellEditor(editor);
        }
    }

    private void setBoutons() {
        btImprimer = new Bouton(12, "Imprimer", "Imprimer", false, icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", "Fermer la fenêtre", false, icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. Tout", "Exporter vers un doc PDF", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        btPaiements = new Bouton(12, "Paiements", "Accéder ou effectuer le paiement des frais.", false, icones.getCaisse_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                if (ecouteurCrossCanal != null) {
                    ecouteurCrossCanal.onOuvrirPaiements(SelectedEleve);
                }
            }
        });
        btPaiements.appliquerDroitAccessDynamique(false);

        btInscription = new Bouton(12, "Fiche d'Inscr.", "Ouvrir la fiche d'inscription", false, icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                if (ecouteurCrossCanal != null) {
                    ecouteurCrossCanal.onOuvrirInscription(SelectedEleve);
                }
            }
        });
        btInscription.appliquerDroitAccessDynamique(false);

        btActualiser = new Bouton(12, "Actualiser", "Actualiser cette liste", false, icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        bOutils = new BarreOutils(barreOutils);
        if (dataLitiges.getParametresLitige().getUtilisateur() != null) {
            Utilisateur user = dataLitiges.getParametresLitige().getUtilisateur();

            if (user.getDroitLitige() == InterfaceUtilisateur.DROIT_CONTROLER) {
                //RAS
            }
            bOutils.AjouterBouton(btActualiser);
            if (user.getDroitFacture() != InterfaceUtilisateur.DROIT_PAS_ACCES) {
                bOutils.AjouterBouton(btPaiements);
            }
            if (user.getDroitInscription() != InterfaceUtilisateur.DROIT_PAS_ACCES) {
                bOutils.AjouterBouton(btInscription);
            }
            bOutils.AjouterBouton(btImprimer);
            bOutils.AjouterBouton(btPDF);
            bOutils.AjouterSeparateur();
            bOutils.AjouterBouton(btFermer);
        }
    }

    private void setMenuContextuel() {
        mImprimer = new RubriqueSimple("Imprimer", 12, false, icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        mFermer = new RubriqueSimple("Fermer", 12, false, icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        mPDF = new RubriqueSimple("Exporter tout", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        mActualiser = new RubriqueSimple("Actualiser", 12, false, icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        mPaiements = new RubriqueSimple("Paiements", 12, false, icones.getCaisse_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                if (ecouteurCrossCanal != null) {
                    ecouteurCrossCanal.onOuvrirPaiements(SelectedEleve);
                }
            }
        });
        mPaiements.appliquerDroitAccessDynamique(false);

        mInscription = new RubriqueSimple("Fiche d'inscription", 12, false, icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                if (ecouteurCrossCanal != null) {
                    ecouteurCrossCanal.onOuvrirInscription(SelectedEleve);
                }
            }
        });
        mInscription.appliquerDroitAccessDynamique(false);

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        menuContextuel = new MenuContextuel();
        if (dataLitiges.getParametresLitige().getUtilisateur() != null) {
            Utilisateur user = dataLitiges.getParametresLitige().getUtilisateur();

            if (user.getDroitLitige() == InterfaceUtilisateur.DROIT_CONTROLER) {
                //RAS
            }
            menuContextuel.Ajouter(mActualiser);
            if (user.getDroitFacture() != InterfaceUtilisateur.DROIT_PAS_ACCES) {
                menuContextuel.Ajouter(mPaiements);
            }
            if (user.getDroitInscription() != InterfaceUtilisateur.DROIT_PAS_ACCES) {
                menuContextuel.Ajouter(mInscription);
            }
            menuContextuel.Ajouter(mImprimer);
            menuContextuel.Ajouter(mPDF);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(mFermer);
        }
    }

    public void init() {
        this.moi = this;
        this.labInfos.setIcon(icones.getInfos_01());
        this.labInfos.setText("Prêt.");

        this.ecouteurClose = new EcouteurUpdateClose() {
            @Override
            public void onFermer() {
                parent.remove(moi);
            }

            @Override
            public void onActualiser(String texte, ImageIcon icone) {
                labInfos.setText(texte);
                labInfos.setIcon(icone);
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    private void aficherProprietes() {
        if (icones != null) {
            new DetailViewer(null, "Proprietés", null, scrollPropo, null, 10) {
                @Override
                public void initPropConstantes(Vector<ConstanteViewer> constantes) {
                    //constantes.add(new Constante("sexe", "0", "MASCULIN"));
                    //constantes.add(new Constante("sexe", "1", "FEMININ"));
                }

                @Override
                public void initPropAEviter(Vector<ProprieteViewer> proprietes) {
                    //proprietes.add(new Propriete("tailleResultat"));
                }

                @Override
                public void initPropSpeciaux(Vector<ProprieteViewer> proprietes) {
                    //Chargement des tranches
                    initTranchesFrais(proprietes);
                    //Chargement des taux de change
                    if (monnaieLocal != null) {
                        for (Monnaie monnaie : dataLitiges.getParametresLitige().getListeMonnaies()) {
                            if (monnaie != monnaieLocal) {
                                proprietes.add(new ProprieteViewer("1 " + monnaie.getCode(), UtilLitige.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode(), ProprieteViewer.TYPE_MONNETAIRE));
                            }
                        }
                    }
                }
            };
        }
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        actualiserTotaux("activerBoutons");
        ecouterLitigeSelectionne();
    }

    private void setIconesTabs() {
        this.tabPrincipal.setIconAt(0, icones.getArgent_01());  //Liste des Fiches de paie
        this.llabMontDu.setIcon(icones.getNombre_01());
        this.llabMontantPaye.setIcon(icones.getNombre_01());
        this.llabMontantSolde.setIcon(icones.getNombre_01());
        this.llabMontantDuSelected.setIcon(icones.getNombre_01());
        this.llabMontantPayeSelected.setIcon(icones.getNombre_01());
        this.llabMontantResteSelected.setIcon(icones.getNombre_01());
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'encaissements
        for (Litige Ienc : modeleListeLitiges.getListeData()) {
            if (Ienc.getBeta() == InterfaceLitige.BETA_MODIFIE || Ienc.getBeta() == InterfaceLitige.BETA_NOUVEAU) {
                rep = true;
            }
        }
        return rep;
    }

    public void fermer() {
        //Vérifier s'il n'y a rien à enregistrer
        if (mustBeSaved() == true) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications et/ou ajouts apportés à ces données?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            } else if (dialogResult == JOptionPane.NO_OPTION) {
                this.ecouteurClose.onFermer();
            }
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            }
        }
    }

    public void imprimer() {
        if (ef != null) {
            if (ef.onVerifieNombre(null) == true) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir imprimer ce document?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        SortiesLitiges sortie = getSortieLitige(btImprimer, mImprimer);
                        DocumentPDFLitige docpdf = new DocumentPDFLitige(this, DocumentPDFLitige.ACTION_IMPRIMER, sortie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public ParametresLitige getParametresLitige() {
        return dataLitiges.getParametresLitige();
    }

    public String getNomfichierPreuve() {
        return "Output.pdf";
    }

    public void exporterPDF() {
        if (ef != null) {
            if (ef.onVerifieNombre(null) == true) {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        SortiesLitiges sortie = getSortieLitige(btPDF, mPDF);
                        DocumentPDFLitige docpdf = new DocumentPDFLitige(this, DocumentPDFLitige.ACTION_OUVRIR, sortie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private SortiesLitiges getSortieLitige(Bouton boutonDeclencheur, RubriqueSimple rubriqueDeclencheur) {
        SortiesLitiges sortiesLitiges = new SortiesLitiges(
                modeleListeLitiges.getListeData(),
                new EcouteurEnregistrement() {
            @Override
            public void onDone(String message) {
                ecouteurClose.onActualiser(message, icones.getAimer_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }

                //On redessine les tableau afin que les couleurs se réinitialisent / Tout redevient noire
                if (modeleListeLitiges != null) {
                    modeleListeLitiges.redessinerTable();
                }
            }

            @Override
            public void onError(String message) {
                ecouteurClose.onActualiser(message, icones.getAlert_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }
            }

            @Override
            public void onUploading(String message) {
                ecouteurClose.onActualiser(message, icones.getSablier_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(false);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(false);
                }
            }
        });
        return sortiesLitiges;
    }

    public void actualiser() {
        if (navigateurPage != null) {
            navigateurPage.reload();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barreOutils = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeLitiges = new javax.swing.JScrollPane();
        tableListeLitige = new javax.swing.JTable();
        scrollPropo = new javax.swing.JScrollPane();
        labInfos = new javax.swing.JLabel();
        labTauxDeChange = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        valMontDu = new javax.swing.JLabel();
        valMontPaye = new javax.swing.JLabel();
        valMontReste = new javax.swing.JLabel();
        llabMontDu = new javax.swing.JLabel();
        llabMontantPaye = new javax.swing.JLabel();
        llabMontantSolde = new javax.swing.JLabel();
        panSelected = new javax.swing.JPanel();
        valMontDuSelect = new javax.swing.JLabel();
        valMontPayeSelect = new javax.swing.JLabel();
        valMontResteSelect = new javax.swing.JLabel();
        llabMontantDuSelected = new javax.swing.JLabel();
        llabMontantPayeSelected = new javax.swing.JLabel();
        llabMontantResteSelected = new javax.swing.JLabel();
        navigateurPage = new Source.UI.NavigateurPages();
        chMonnaie = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
        barreOutils.setFloatable(false);
        barreOutils.setRollover(true);
        barreOutils.setAutoscrolls(true);
        barreOutils.setPreferredSize(new java.awt.Dimension(59, 61));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture02.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barreOutils.add(jButton5);

        tabPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        tabPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPrincipalStateChanged(evt);
            }
        });

        scrollListeLitiges.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeLitiges.setAutoscrolls(true);
        scrollListeLitiges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeLitigesMouseClicked(evt);
            }
        });

        tableListeLitige.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeLitige.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListeLitigeMouseDragged(evt);
            }
        });
        tableListeLitige.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeLitigeMouseClicked(evt);
            }
        });
        tableListeLitige.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListeLitigeKeyReleased(evt);
            }
        });
        scrollListeLitiges.setViewportView(tableListeLitige);

        tabPrincipal.addTab("Litiges", scrollListeLitiges);

        scrollPropo.setBackground(new java.awt.Color(255, 255, 255));
        tabPrincipal.addTab("Propriétés", scrollPropo);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        labTauxDeChange.setForeground(new java.awt.Color(51, 51, 255));
        labTauxDeChange.setText("Taux");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Litiges", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        valMontDu.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontDu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontDu.setText("00000000 $");

        valMontPaye.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontPaye.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontPaye.setText("00000000 $");

        valMontReste.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontReste.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontReste.setText("00000000 $");

        llabMontDu.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontDu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontDu.setText("Total - Montant dû");

        llabMontantPaye.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontantPaye.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontantPaye.setText("Total - Montant payé");

        llabMontantSolde.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontantSolde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontantSolde.setText("Total - Reste à payer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(llabMontantPaye, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(llabMontDu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(llabMontantSolde, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valMontReste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valMontPaye, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valMontDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontDu)
                    .addComponent(llabMontDu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontPaye)
                    .addComponent(llabMontantPaye))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontReste)
                    .addComponent(llabMontantSolde))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panSelected.setBackground(new java.awt.Color(255, 255, 255));
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fiche de paie séléctionnée", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        valMontDuSelect.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontDuSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontDuSelect.setText("0000000000 $ ");

        valMontPayeSelect.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontPayeSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontPayeSelect.setText("0000000000 $ ");

        valMontResteSelect.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        valMontResteSelect.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        valMontResteSelect.setText("0000000000 $ ");

        llabMontantDuSelected.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontantDuSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontantDuSelected.setText("Montant dû");

        llabMontantPayeSelected.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontantPayeSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontantPayeSelected.setText("Montant payé");

        llabMontantResteSelected.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        llabMontantResteSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG_Litige/Facture01.png"))); // NOI18N
        llabMontantResteSelected.setText("Reste à payer");

        javax.swing.GroupLayout panSelectedLayout = new javax.swing.GroupLayout(panSelected);
        panSelected.setLayout(panSelectedLayout);
        panSelectedLayout.setHorizontalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(llabMontantDuSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabMontantPayeSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabMontantResteSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valMontResteSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(valMontPayeSelect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valMontDuSelect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        panSelectedLayout.setVerticalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSelectedLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontDuSelect)
                    .addComponent(llabMontantDuSelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontPayeSelect)
                    .addComponent(llabMontantPayeSelected))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valMontResteSelect)
                    .addComponent(llabMontantResteSelected))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        chMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MONNAIE (*)" }));
        chMonnaie.setToolTipText("Monnaie de sortie");
        chMonnaie.setMinimumSize(new java.awt.Dimension(50, 30));
        chMonnaie.setPreferredSize(new java.awt.Dimension(50, 30));
        chMonnaie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chMonnaieItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 101, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labTauxDeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(navigateurPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(navigateurPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(chMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(labTauxDeChange)
                .addGap(5, 5, 5)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeLitigeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeLitigeMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeLitigeMouseClicked

    private void scrollListeLitigesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeLitigesMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeLitigesMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListeLitigeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeLitigeKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeLitigeKeyReleased

    private void chMonnaieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chMonnaieItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_chMonnaieItemStateChanged

    private void tableListeLitigeMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeLitigeMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeLitigeMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JComboBox<String> chMonnaie;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labInfos;
    private javax.swing.JLabel labTauxDeChange;
    private javax.swing.JLabel llabMontDu;
    private javax.swing.JLabel llabMontantDuSelected;
    private javax.swing.JLabel llabMontantPaye;
    private javax.swing.JLabel llabMontantPayeSelected;
    private javax.swing.JLabel llabMontantResteSelected;
    private javax.swing.JLabel llabMontantSolde;
    private Source.UI.NavigateurPages navigateurPage;
    private javax.swing.JPanel panSelected;
    private javax.swing.JScrollPane scrollListeLitiges;
    private javax.swing.JScrollPane scrollPropo;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeLitige;
    private javax.swing.JLabel valMontDu;
    private javax.swing.JLabel valMontDuSelect;
    private javax.swing.JLabel valMontPaye;
    private javax.swing.JLabel valMontPayeSelect;
    private javax.swing.JLabel valMontReste;
    private javax.swing.JLabel valMontResteSelect;
    // End of variables declaration//GEN-END:variables
}
