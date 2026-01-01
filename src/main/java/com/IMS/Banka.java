package atm;

import java.util.HashMap;
import java.util.Map;

public class Banka {
    private Map<String, Musteri> musteriler = new HashMap<>();

    public Banka() {
        musteriler.put("1234567890123456", new MusteriMantigi("Murat Deneysel", "TR1001", "1234567890123456", "1234"));
    }

    public Musteri musteriSorgula(String kartNo, String sifre) {
        Musteri m = musteriler.get(kartNo);
        if (m != null && m.getSifre().equals(sifre)) return m;
        return null;
    }
}