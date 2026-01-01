package atm.gui;

import atm.ATM;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class GUI {
    private ATM atm;
    private JFrame cerceve;
    private JLabel baslikMesaj, altMesaj, merkezGosterge, pinKutusu, miktarKutusu, miktarAciklama;
    private JLabel[] solEtiketler = new JLabel[4], sagEtiketler = new JLabel[4];
    private JButton[] solButonlar = new JButton[4], sagButonlar = new JButton[4];

    private StringBuilder girisTamponu = new StringBuilder();
    private String mevcutEkran = "HOSGELDIN";
    private DecimalFormat paraFormat = new DecimalFormat("#,##0.00 TL");
    private int[] yCoords = {141, 261, 381, 501};

    private boolean makbuzIstendi = false;
    private double kaynakBakiye = 0;
    private String transferYon = "";
    private int sonIslemMiktari = 0;
    private boolean isYatirma = false;

    public GUI(ATM atm) {
        this.atm = atm;
        arayuzuHazirla();
    }

    private void arayuzuHazirla() {
        cerceve = new JFrame("Abant Bankası Terminali");
        cerceve.setSize(1024, 900);
        cerceve.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cerceve.setLayout(new BorderLayout());

        cerceve.add(bezelPanel(solButonlar, 50, 10), BorderLayout.WEST);
        cerceve.add(bezelPanel(sagButonlar, 10, 10), BorderLayout.EAST);

        JPanel monitor = new JPanel(null);
        monitor.setBackground(new Color(210, 215, 220));

        baslikMesaj = new JLabel("");
        baslikMesaj.setBounds(20, 10, 600, 25);
        baslikMesaj.setForeground(new Color(0, 45, 114));
        baslikMesaj.setFont(new Font("SansSerif", Font.BOLD, 18));
        monitor.add(baslikMesaj);

        altMesaj = new JLabel("");
        altMesaj.setBounds(20, 32, 400, 20);
        altMesaj.setForeground(new Color(60, 60, 60));
        altMesaj.setFont(new Font("SansSerif", Font.PLAIN, 14));
        monitor.add(altMesaj);

        merkezGosterge = new JLabel("", SwingConstants.CENTER);
        merkezGosterge.setBounds(0, 40, 800, 250);
        merkezGosterge.setFont(new Font("Arial", Font.BOLD, 22));
        monitor.add(merkezGosterge);

        pinKutusu = new JLabel("", SwingConstants.CENTER);
        pinKutusu.setBounds(300, 220, 200, 60);
        pinKutusu.setFont(new Font("Monospaced", Font.BOLD, 35));
        pinKutusu.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2, true));
        pinKutusu.setBackground(Color.WHITE);
        pinKutusu.setOpaque(true);
        pinKutusu.setVisible(false);
        monitor.add(pinKutusu);

        miktarAciklama = new JLabel("");
        miktarAciklama.setFont(new Font("SansSerif", Font.BOLD, 14));
        miktarAciklama.setForeground(new Color(0, 45, 114));
        miktarAciklama.setVisible(false);
        monitor.add(miktarAciklama);

        miktarKutusu = new JLabel("", SwingConstants.CENTER);
        miktarKutusu.setFont(new Font("Monospaced", Font.BOLD, 24));
        miktarKutusu.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        miktarKutusu.setBackground(Color.WHITE);
        miktarKutusu.setOpaque(true);
        miktarKutusu.setVisible(false);
        monitor.add(miktarKutusu);

        for (int i = 0; i < 4; i++) {
            solEtiketler[i] = new JLabel("");
            solEtiketler[i].setFont(new Font("SansSerif", Font.BOLD, 22));
            solEtiketler[i].setForeground(new Color(0, 45, 114));
            solEtiketler[i].setBounds(5, yCoords[i], 350, 40);

            sagEtiketler[i] = new JLabel("");
            sagEtiketler[i].setFont(new Font("SansSerif", Font.BOLD, 22));
            sagEtiketler[i].setForeground(new Color(0, 45, 114));
            sagEtiketler[i].setHorizontalAlignment(SwingConstants.RIGHT);
            sagEtiketler[i].setBounds(450, yCoords[i], 350, 40);

            monitor.add(solEtiketler[i]);
            monitor.add(sagEtiketler[i]);
        }

        cerceve.add(monitor, BorderLayout.CENTER);
        cerceve.add(TusTakimi(), BorderLayout.SOUTH);

        ekranDegistir("HOSGELDIN");
        cerceve.setLocationRelativeTo(null);
        cerceve.setVisible(true);
    }

    private void ekranDegistir(String ekranAdi) {
        mevcutEkran = ekranAdi;
        temizle();
        switch (ekranAdi) {
            case "HOSGELDIN" -> {
                merkezGosterge.setText("<html><center><font size='7' color='#002D72'>ABANT BANKASI</font><br><br>LÜTFEN KARTINIZI TAKINIZ</center></html>");
                sagEtiketler[3].setText("KART TAK");
                sagButonlar[3].addActionListener(e -> ekranDegistir("SIFRE_GIRIS"));
            }
            case "SIFRE_GIRIS" -> {
                merkezGosterge.setText("<html><center>ŞİFRE GİRİNİZ<br><br><br><br><br><br><font size='4' color='gray'>(Örnek PIN: 1234)</font></center></html>");
                pinKutusu.setVisible(true);
            }
            case "ANA_MENU" -> {
                isYatirma = false; transferYon = "";
                baslikMesaj.setText(atm.getAktifMusteri().getIsim().toUpperCase());
                altMesaj.setText("Kart No: 1234 5678 9012 3456");
                merkezGosterge.setText("<html><center>HOŞGELDİNİZ<br>İŞLEM SEÇİNİZ</center></html>");
                solEtiketler[1].setText("BAKİYE"); solButonlar[1].addActionListener(e -> bakiyeGoster());
                sagEtiketler[1].setText("PARA ÇEK"); sagButonlar[1].addActionListener(e -> ekranDegistir("HESAP_SEC_MENU"));
                solEtiketler[2].setText("PARA TRANSFER"); solButonlar[2].addActionListener(e -> ekranDegistir("TRANSFER_MENU"));
                sagEtiketler[2].setText("PARA YATIR"); sagButonlar[2].addActionListener(e -> { isYatirma = true; ekranDegistir("HESAP_SEC_YATIR"); });
                solEtiketler[3].setText("HESAP HAREKETLERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("HESAP_HAREKETLERI"));
                sagEtiketler[3].setText("İADE"); sagButonlar[3].addActionListener(e -> iadeEt());
            }
            case "HESAP_SEC_MENU" -> {
                merkezGosterge.setText("ÇEKİLECEK HESABI SEÇİNİZ");
                solEtiketler[1].setText("VADESİZ HESAP"); solButonlar[1].addActionListener(e -> ekranDegistir("PARA_CEK_MENU"));
                sagEtiketler[1].setText("VADELİ HESAP"); sagButonlar[1].addActionListener(e -> ekranDegistir("PARA_CEK_MENU"));
                solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
            }
            case "PARA_CEK_MENU" -> {
                merkezGosterge.setText("MİKTAR SEÇİNİZ");
                solEtiketler[0].setText("50 TL"); solButonlar[0].addActionListener(e -> baslatParaCek(50));
                solEtiketler[1].setText("100 TL"); solButonlar[1].addActionListener(e -> baslatParaCek(100));
                solEtiketler[2].setText("200 TL"); solButonlar[2].addActionListener(e -> baslatParaCek(200));
                sagEtiketler[0].setText("500 TL"); sagButonlar[0].addActionListener(e -> baslatParaCek(500));
                sagEtiketler[1].setText("1000 TL"); sagButonlar[1].addActionListener(e -> baslatParaCek(1000));
                sagEtiketler[2].setText("2000 TL"); sagButonlar[2].addActionListener(e -> baslatParaCek(2000));
                solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
                miktarAciklama.setText("DİĞER TUTARI GİRİNİZ:");
                miktarAciklama.setBounds(580, yCoords[3] - 25, 215, 20); miktarAciklama.setVisible(true);
                miktarKutusu.setBounds(580, yCoords[3], 215, 40); miktarKutusu.setVisible(true);
            }
            case "TRANSFER_MENU" -> {
                merkezGosterge.setText("HESAPLAR ARASI TRANSFER");
                solEtiketler[1].setText("VADESİZ -> VADELİ");
                solButonlar[1].addActionListener(e -> { this.kaynakBakiye = atm.getBakiye(); this.transferYon = "TRF"; ekranDegistir("TRANSFER_MIKTAR"); });
                sagEtiketler[1].setText("VADELİ -> VADESİZ");
                sagButonlar[1].addActionListener(e -> { this.kaynakBakiye = 12500.50; this.transferYon = "TRF"; ekranDegistir("TRANSFER_MIKTAR"); });
                solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
            }
            case "TRANSFER_MIKTAR" -> {
                merkezGosterge.setText("<html><center>LÜTFEN MİKTARI GİRİNİZ VE<br><font color='green'>GİRİŞ</font> TUŞUNA BASINIZ<br><br><font color='blue' size='4'>Bakiye: " + paraFormat.format(kaynakBakiye) + "</font></center></html>");
                miktarKutusu.setBounds(250, 220, 300, 50); miktarKutusu.setVisible(true);
                solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("TRANSFER_MENU"));
            }
            case "MAKBUZ_SORGU" -> {
                merkezGosterge.setText("<html><center>İŞLEM MAKBUZU İSTER MİSİNİZ?</center></html>");
                solEtiketler[3].setText("HAYIR"); solButonlar[3].addActionListener(e -> { makbuzIstendi = false; ilerleIslem(); });
                sagEtiketler[3].setText("EVET"); sagButonlar[3].addActionListener(e -> { makbuzIstendi = true; ilerleIslem(); });
            }
            case "PARA_ALINIZ" -> {
                merkezGosterge.setText("<html><center>LÜTFEN PARA BÖLÜMÜNDEN ALINIZ<br><br><font size='6' color='blue'>" + paraFormat.format(sonIslemMiktari) + "</font></center></html>");
                Timer t = new Timer(3000, e -> finalizeReceipt());
                t.setRepeats(false); t.start();
            }
            case "HESAP_HAREKETLERI" -> {
                baslikMesaj.setText("HESAP HAREKETLERİ");
                merkezGosterge.setHorizontalAlignment(SwingConstants.LEFT);
                merkezGosterge.setVerticalAlignment(SwingConstants.TOP);
                merkezGosterge.setBounds(10, 60, 780, 460); // Pushed to the far left
                merkezGosterge.setText(hareketleriOlustur());
                solEtiketler[3].setText("GERİ");
                solButonlar[3].addActionListener(e -> {
                    merkezGosterge.setHorizontalAlignment(SwingConstants.CENTER);
                    merkezGosterge.setVerticalAlignment(SwingConstants.CENTER);
                    merkezGosterge.setBounds(0, 40, 800, 250);
                    ekranDegistir("ANA_MENU");
                });
            }
            case "HESAP_SEC_YATIR" -> {
                merkezGosterge.setText("YATIRILACAK HESABI SEÇİNİZ");
                solEtiketler[1].setText("VADESİZ HESAP"); solButonlar[1].addActionListener(e -> ekranDegistir("MAKBUZ_SORGU_YATIR"));
                sagEtiketler[1].setText("VADELİ HESAP"); sagButonlar[1].addActionListener(e -> ekranDegistir("MAKBUZ_SORGU_YATIR"));
                solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
            }
            case "MAKBUZ_SORGU_YATIR" -> {
                merkezGosterge.setText("<html><center>İŞLEM MAKBUZU İSTER MİSİNİZ?<br><font size='4'>(İşlem sonunda verilecektir)</font></center></html>");
                solEtiketler[3].setText("HAYIR"); solButonlar[3].addActionListener(e -> { makbuzIstendi = false; paraBolmesiAc(); });
                sagEtiketler[3].setText("EVET"); sagButonlar[3].addActionListener(e -> { makbuzIstendi = true; paraBolmesiAc(); });
            }
            case "DEVAM_MI" -> {
                merkezGosterge.setText("<html><center>BAŞKA İŞLEM YAPMAK İSTİYOR MUSUNUZ?</center></html>");
                solEtiketler[3].setText("HAYIR"); solButonlar[3].addActionListener(e -> iadeEt());
                sagEtiketler[3].setText("EVET"); sagButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
            }
        }
    }

    private void baslatParaCek(int m) {
        if (atm.getBakiye() >= m) {
            this.sonIslemMiktari = m;
            atm.paraCek(m);
            ekranDegistir("MAKBUZ_SORGU");
        } else { hataGoster("YETERSİZ BAKİYE!"); }
    }

    private void ilerleIslem() {
        if (mevcutEkran.equals("MAKBUZ_SORGU") && transferYon.isEmpty()) {
            ekranDegistir("PARA_ALINIZ");
        } else {
            finalizeReceipt();
        }
    }

    private void finalizeReceipt() {
        if (makbuzIstendi) {
            temizle();
            merkezGosterge.setText("<html><center>MAKBUZ YAZDIRILIYOR...<br>LÜTFEN BEKLEYİNİZ</center></html>");
            Timer t = new Timer(2500, e -> islemTamamlandi());
            t.setRepeats(false); t.start();
        } else {
            islemTamamlandi();
        }
    }

    private void paraBolmesiAc() {
        temizle();
        merkezGosterge.setText("<html><center>LÜTFEN NOTLARI DÜZGÜNCE YERLEŞTİRİNİZ<br><font color='red' size='5'>(MAKSİMUM 100 ADET)</font></center></html>");
        Timer t1 = new Timer(3500, e -> {
            merkezGosterge.setText("<html><center>PARALAR SAYILIYOR...<br>LÜTFEN BEKLEYİNİZ</center></html>");
            Timer t2 = new Timer(3000, e2 -> { this.sonIslemMiktari = 500; finalizeReceipt(); });
            t2.setRepeats(false); t2.start();
        });
        t1.setRepeats(false); t1.start();
    }

    private String hareketleriOlustur() {
        String[][] veriler = {{"01.01.2026", "MAAŞ ÖDEMESİ", "+42.500,00"}, {"31.12.2025", "KİRA ÖDEMESİ", "-12.000,00"}, {"30.12.2025", "MİGROS TÜRK A.Ş.", "-1.450,20"}, {"29.12.2025", "ATM NAKİT YATIRMA", "+2.000,00"}, {"28.12.2025", "STARBUCKS COFFEE", "-185,50"}, {"27.12.2025", "NETFLIX.COM", "-159,99"}};
        StringBuilder html = new StringBuilder("<html><body style='width: 780px; padding: 0px;'>");
        html.append("<table width='750' style='border-collapse: collapse; font-family: SansSerif; font-size: 14px; margin-left: 5px;'>");
        html.append("<tr style='background-color: #002D72; color: white;'><th align='left' style='padding:8px;'>TARİH</th><th align='left' style='padding:8px;'>AÇIKLAMA</th><th align='right' style='padding:8px;'>TUTAR</th></tr>");
        for (int i = 0; i < veriler.length; i++) {
            String bg = (i % 2 == 0) ? "#E8ECF1" : "#FFFFFF";
            String renk = veriler[i][2].startsWith("+") ? "#008000" : "#CC0000";
            html.append("<tr style='background-color: ").append(bg).append(";'><td style='padding:10px;'>").append(veriler[i][0]).append("</td><td style='padding:10px;'>").append(veriler[i][1]).append("</td><td align='right' style='padding:10px; color: ").append(renk).append(";'><b>").append(veriler[i][2]).append(" TL</b></td></tr>");
        }
        return html.append("</table></body></html>").toString();
    }

    private void bakiyeGoster() {
        temizle();
        String htmlContent = "<html><body style='width: 700px;'><div style='text-align: center; font-size: 20px; color: #002D72;'>HESAP BAKİYELERİ</div><br><br>" +
                "<div style='text-align: left; padding-left: 50px; font-size: 18px;'>VADESİZ HESAP: <b>" + paraFormat.format(atm.getBakiye()) + "</b><br><br>" +
                "VADELİ HESAP: <b>12.500,50 TL</b></div></body></html>";
        merkezGosterge.setText(htmlContent);
        solEtiketler[3].setText("GERİ"); solButonlar[3].addActionListener(e -> ekranDegistir("ANA_MENU"));
    }

    private void islemTamamlandi() {
        temizle();
        merkezGosterge.setText("<html><center>İŞLEM BAŞARIYLA TAMAMLANDI</center></html>");
        Timer t = new Timer(2000, e -> ekranDegistir("DEVAM_MI"));
        t.setRepeats(false); t.start();
    }

    private void hataGoster(String mesaj) {
        temizle();
        merkezGosterge.setText("<html><center><font color='red'>" + mesaj + "</font></center></html>");
        Timer t = new Timer(2000, e -> ekranDegistir("ANA_MENU"));
        t.setRepeats(false); t.start();
    }

    private void iadeEt() {
        temizle();
        merkezGosterge.setText("<html><center>KARTINIZI ALMAYI UNUTMAYIN,<br>İYİ GÜNLER DİLERİZ</center></html>");
        Timer t = new Timer(2000, e -> ekranDegistir("HOSGELDIN"));
        t.setRepeats(false); t.start();
    }

    private void temizle() {
        girisTamponu.setLength(0); baslikMesaj.setText(""); altMesaj.setText("");
        if (pinKutusu != null) pinKutusu.setVisible(false);
        if (miktarAciklama != null) miktarAciklama.setVisible(false);
        if (miktarKutusu != null) { miktarKutusu.setVisible(false); miktarKutusu.setText(""); }
        for(int i=0; i<4; i++) {
            solEtiketler[i].setText(""); sagEtiketler[i].setText("");
            for(java.awt.event.ActionListener al : solButonlar[i].getActionListeners()) solButonlar[i].removeActionListener(al);
            for(java.awt.event.ActionListener al : sagButonlar[i].getActionListeners()) sagButonlar[i].removeActionListener(al);
        }
    }

    private void tusBasildi(String k) {
        if (mevcutEkran.equals("SIFRE_GIRIS")) {
            if (k.matches("\\d") && girisTamponu.length() < 4) {
                girisTamponu.append(k);
                pinKutusu.setText("* ".repeat(girisTamponu.length()).trim());
                if (girisTamponu.length() == 4) {
                    if (atm.dogrula("1234567890123456", girisTamponu.toString())) ekranDegistir("ANA_MENU");
                    else { hataGoster("HATALI ŞİFRE!"); }
                }
            }
        } else if (mevcutEkran.equals("PARA_CEK_MENU") || mevcutEkran.equals("TRANSFER_MIKTAR")) {
            if (k.matches("\\d") && girisTamponu.length() < 6) {
                girisTamponu.append(k);
                miktarKutusu.setText(girisTamponu.toString() + " TL");
            }
        }
    }

    private JPanel TusTakimi() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        p.setBackground(new Color(30, 30, 30));
        JPanel numPad = new JPanel(new GridLayout(4, 3, 10, 10));
        numPad.setOpaque(false);
        for(String k : new String[]{"1","2","3","4","5","6","7","8","9","*","0","#"}) {
            JButton btn = new JButton(k); btn.setPreferredSize(new Dimension(80, 50));
            btn.addActionListener(e -> tusBasildi(k)); numPad.add(btn);
        }
        JPanel ctrlPad = new JPanel(new GridLayout(4, 1, 0, 10));
        ctrlPad.setOpaque(false);
        String[] cKeys = {"İPTAL", "SİL", "DÜZELT", "GİRİŞ"};
        Color[] cCols = {new Color(180, 50, 50), new Color(200, 140, 20), new Color(200, 180, 30), new Color(40, 130, 40)};
        for(int i=0; i<4; i++) {
            JButton btn = new JButton(cKeys[i]); btn.setPreferredSize(new Dimension(120, 50));
            btn.setBackground(cCols[i]); btn.setForeground(Color.WHITE);
            btn.setOpaque(true); btn.setBorderPainted(false);
            final String cmd = cKeys[i];
            btn.addActionListener(e -> {
                if(cmd.equals("İPTAL")) iadeEt();
                else if(cmd.equals("SİL")) { girisTamponu.setLength(0); miktarKutusu.setText(""); pinKutusu.setText(""); }
                else if(cmd.equals("GİRİŞ")) {
                    if (girisTamponu.length() > 0) {
                        try {
                            int m = Integer.parseInt(girisTamponu.toString());
                            if(mevcutEkran.equals("PARA_CEK_MENU")) baslatParaCek(m);
                            else if(mevcutEkran.equals("TRANSFER_MIKTAR")) {
                                if (kaynakBakiye >= m) { this.sonIslemMiktari = m; ekranDegistir("MAKBUZ_SORGU"); }
                                else { hataGoster("YETERSİZ BAKİYE!"); }
                            }
                        } catch(Exception ex) {}
                    }
                }
            });
            ctrlPad.add(btn);
        }
        p.add(numPad); p.add(ctrlPad);
        return p;
    }

    private JPanel bezelPanel(JButton[] btns, int leftPad, int rightPad) {
        JPanel p = new JPanel(new GridLayout(4, 1, 0, 100));
        p.setBackground(new Color(60, 60, 60));
        p.setBorder(new EmptyBorder(150, leftPad, 50, rightPad));
        for(int i=0; i<4; i++) { btns[i] = new JButton(""); btns[i].setPreferredSize(new Dimension(65, 55)); p.add(btns[i]); }
        return p;
    }
}