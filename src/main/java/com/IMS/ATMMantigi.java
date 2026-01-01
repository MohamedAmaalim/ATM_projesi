package atm;

public class ATMMantigi implements ATM {
    private Banka bankaMerkezi;
    private Musteri aktifMusteri;
    private double bakiye = 15000.0;

    public ATMMantigi(Banka banka) { this.bankaMerkezi = banka; }

    @Override
    public boolean dogrula(String kartNo, String sifre) {
        aktifMusteri = bankaMerkezi.musteriSorgula(kartNo, sifre);
        return aktifMusteri != null;
    }

    @Override public void oturumuKapat() { aktifMusteri = null; }
    @Override public double getBakiye() { return bakiye; }
    @Override public Musteri getAktifMusteri() { return aktifMusteri; }

    @Override
    public boolean paraCek(double miktar) {
        if (miktar > 0 && miktar <= bakiye && miktar % 100 == 0) {
            bakiye -= miktar;
            return true;
        }
        return false;
    }
}