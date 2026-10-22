import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Menu[] daftarMenu = buatDaftarMenu();
        tampilkanDaftarMenu(daftarMenu);
    }

    private static Menu[] buatDaftarMenu() {
        Menu[] daftarMenu = new Menu[8];

        daftarMenu[0] = new Menu("Nasi Goreng", 25000, "Makanan");
        daftarMenu[1] = new Menu("Mie Goreng", 22000, "Makanan");
        daftarMenu[2] = new Menu("Ayam Bakar", 30000, "Makanan");
        daftarMenu[3] = new Menu("Sate Ayam", 28000, "Makanan");

        daftarMenu[4] = new Menu("Es Teh Manis", 5000, "Minuman");
        daftarMenu[5] = new Menu("Es Jeruk", 7000, "Minuman");
        daftarMenu[6] = new Menu("Kopi Hitam", 10000, "Minuman");
        daftarMenu[7] = new Menu("Air Mineral", 4000, "Minuman");

        return daftarMenu;
    }

    private static void tampilkanDaftarMenu(Menu[] daftarMenu) {
        System.out.println("========================================");
        System.out.println("          DAFTAR MENU RESTORAN");
        System.out.println("========================================");
        System.out.printf("%-4s %-20s %-10s %12s%n", "No", "Nama", "Kategori", "Harga");
        System.out.println("----------------------------------------");

        tampilkanMenuBerdasarkanKategori(daftarMenu, "Makanan");
        System.out.println();
        tampilkanMenuBerdasarkanKategori(daftarMenu, "Minuman");
    }

    private static void tampilkanMenuBerdasarkanKategori(Menu[] daftarMenu, String kategori) {
        System.out.println("--- " + kategori.toUpperCase() + " ---");

        for (int i = 0; i < daftarMenu.length; i++) {
            if (daftarMenu[i] != null && daftarMenu[i].getKategori().equals(kategori)) {
                System.out.printf("%-4d %-20s %-10s %12s%n",
                        i + 1,
                        daftarMenu[i].getNama(),
                        daftarMenu[i].getKategori(),
                        formatRupiah(daftarMenu[i].getHarga()));
            }
        }
    }

    private static String formatRupiah(double harga) {
        return String.format(Locale.forLanguageTag("id-ID"), "Rp%,.0f", harga);
    }
}
