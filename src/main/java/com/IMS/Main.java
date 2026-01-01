package atm;

import atm.gui.GUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Banka banka = new Banka();
        ATM atm = new ATMMantigi(banka);
        SwingUtilities.invokeLater(() -> new GUI(atm));
    }
}