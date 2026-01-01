package atm;

public interface ATM {
    boolean dogrula(String kartNo, String sifre);
    void oturumuKapat();
    boolean paraCek(double miktar);
    double getBakiye();
    Musteri getAktifMusteri();
}