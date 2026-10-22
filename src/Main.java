import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu[] daftarMenu = buatDaftarMenu();
        tampilkanDaftarMenu(daftarMenu);

        Scanner scanner = new Scanner(System.in);
        prosesPemesanan(scanner, daftarMenu);
        scanner.close();
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

    private static void prosesPemesanan(Scanner scanner, Menu[] daftarMenu) {
        System.out.println();
        System.out.println("=== PEMESANAN PELANGGAN ===");
        System.out.println("Ketik 'selesai' untuk mengakhiri pemesanan.");

        boolean pemesananSelesai = false;

        while (!pemesananSelesai) {
            System.out.print("Masukkan nomor menu: ");
            String inputMenu = scanner.nextLine().trim();

            if (inputMenu.equalsIgnoreCase("selesai")) {
                pemesananSelesai = true;
            } else {
                int nomorMenu = 0;
                boolean nomorValid = false;

                try {
                    nomorMenu = Integer.parseInt(inputMenu);

                    if (nomorMenu >= 1
                            && nomorMenu <= daftarMenu.length
                            && daftarMenu[nomorMenu - 1] != null) {
                        nomorValid = true;
                    }
                } catch (NumberFormatException e) {
                    nomorValid = false;
                }

                if (!nomorValid) {
                    System.out.println("Nomor menu tidak valid. Silakan pilih nomor menu yang tersedia.");
                    continue;
                }

                int jumlah = 0;
                boolean jumlahValid = false;

                while (!jumlahValid) {
                    System.out.print("Masukkan jumlah: ");
                    String inputJumlah = scanner.nextLine().trim();

                    if (inputJumlah.equalsIgnoreCase("selesai")) {
                        pemesananSelesai = true;
                        break;
                    }

                    try {
                        jumlah = Integer.parseInt(inputJumlah);

                        if (jumlah > 0) {
                            jumlahValid = true;
                        } else {
                            System.out.println("Jumlah harus berupa bilangan bulat positif. Silakan coba lagi.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Jumlah harus berupa bilangan bulat positif. Silakan coba lagi.");
                    }
                }

                if (!pemesananSelesai) {
                    Menu menuTerpilih = daftarMenu[nomorMenu - 1];
                    System.out.println("Pesanan dicatat: " + jumlah + " x " + menuTerpilih.getNama());
                    System.out.println();
                }
            }
        }

        System.out.println("Pemesanan telah berakhir.");
    }
}
