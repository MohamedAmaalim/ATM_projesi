package atm;

public class MusteriMantigi implements Musteri {
    private String isim, hesapNo, kartNo, sifre;

    public MusteriMantigi(String isim, String hesapNo, String kartNo, String sifre) {
        this.isim = isim; this.hesapNo = hesapNo; this.kartNo = kartNo; this.sifre = sifre;
    }
    @Override public String getIsim() { return isim; }
    @Override public String getHesapNumarasi() { return hesapNo; }
    @Override public String getKartNumarasi() { return kartNo; }
    @Override public String getSifre() { return sifre; }
}