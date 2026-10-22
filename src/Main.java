import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final int KAPASITAS_PESANAN = 5;
    private static final double BATAS_DISKON = 100000;
    private static final double BATAS_PROMO = 50000;
    private static final double PERSENTASE_DISKON = 0.10;
    private static final double TARIF_PAJAK = 0.10;
    private static final double BIAYA_PELAYANAN = 20000;

    public static void main(String[] args) {
        Menu[] daftarMenu = buatDaftarMenu();
        tampilkanDaftarMenu(daftarMenu);

        Menu[] pesanan = new Menu[KAPASITAS_PESANAN];
        int[] jumlahPesanan = new int[KAPASITAS_PESANAN];

        Scanner scanner = new Scanner(System.in);
        int jumlahItemPesanan = prosesPemesanan(scanner, daftarMenu, pesanan, jumlahPesanan);
        scanner.close();

        tampilkanRingkasanPesanan(pesanan, jumlahPesanan, jumlahItemPesanan);
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

    private static double hitungSubtotal(Menu[] pesanan, int[] jumlahPesanan,
            int jumlahItemPesanan) {
        double subtotal = 0;

        for (int i = 0; i < jumlahItemPesanan; i++) {
            subtotal = subtotal + (pesanan[i].getHarga() * jumlahPesanan[i]);
        }

        return subtotal;
    }

    private static double hitungDiskon(double subtotal) {
        if (subtotal > BATAS_DISKON) {
            return subtotal * PERSENTASE_DISKON;
        }

        return 0;
    }

    private static int hitungUnitGratis(int jumlahPesanan) {
        return jumlahPesanan / 2;
    }

    private static double hitungNilaiPromo(Menu menu, int jumlahPesanan) {
        return hitungUnitGratis(jumlahPesanan) * menu.getHarga();
    }

    private static int cariIndeksPromoMinuman(Menu[] pesanan, int[] jumlahPesanan,
            int jumlahItemPesanan, double subtotal) {
        if (subtotal <= BATAS_PROMO) {
            return -1;
        }

        int indeksPromo = -1;
        double nilaiPromoTerbesar = 0;

        for (int i = 0; i < jumlahItemPesanan; i++) {
            if (pesanan[i].getKategori().equals("Minuman")) {
                double nilaiPromo = hitungNilaiPromo(pesanan[i], jumlahPesanan[i]);

                if (nilaiPromo > nilaiPromoTerbesar) {
                    nilaiPromoTerbesar = nilaiPromo;
                    indeksPromo = i;
                }
            }
        }

        return indeksPromo;
    }

    private static double hitungDasarPajak(double subtotal, double diskon, double nilaiPromo) {
        double dasarPajak = subtotal - diskon - nilaiPromo;

        if (dasarPajak < 0) {
            return 0;
        }

        return dasarPajak;
    }

    private static double hitungPajak(double dasarPajak) {
        return dasarPajak * TARIF_PAJAK;
    }

    private static double hitungBiayaPelayanan(int jumlahItemPesanan) {
        if (jumlahItemPesanan > 0) {
            return BIAYA_PELAYANAN;
        }

        return 0;
    }

    private static double hitungTotalAkhir(double subtotal, double diskon, double nilaiPromo,
            double pajak, double biayaPelayanan) {
        return subtotal - diskon - nilaiPromo + pajak + biayaPelayanan;
    }

    private static int prosesPemesanan(Scanner scanner, Menu[] daftarMenu,
            Menu[] pesanan, int[] jumlahPesanan) {
        System.out.println();
        System.out.println("=== PEMESANAN PELANGGAN ===");
        System.out.println("Ketik 'selesai' untuk mengakhiri pemesanan.");

        boolean pemesananSelesai = false;
        int jumlahItemPesanan = 0;

        while (!pemesananSelesai) {
            System.out.print("Masukkan nomor menu: ");
            String inputMenu = scanner.nextLine().trim();

            if (inputMenu.equalsIgnoreCase("selesai")) {
                pemesananSelesai = true;
            } else if (inputMenu.isEmpty()) {
                System.out.println("Nomor menu tidak boleh kosong. Silakan pilih nomor menu yang tersedia.");
                continue;
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
                    } else if (inputJumlah.isEmpty()) {
                        System.out.println("Jumlah tidak boleh kosong. Silakan masukkan bilangan bulat positif.");
                        continue;
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
                    int indeksPesanan = cariIndeksPesanan(pesanan, jumlahItemPesanan, menuTerpilih);

                    if (indeksPesanan != -1) {
                        jumlahPesanan[indeksPesanan] = jumlahPesanan[indeksPesanan] + jumlah;
                        System.out.println("Pesanan digabungkan: " + jumlah + " x "
                                + menuTerpilih.getNama() + " (total "
                                + jumlahPesanan[indeksPesanan] + ").");
                    } else if (jumlahItemPesanan < pesanan.length) {
                        pesanan[jumlahItemPesanan] = menuTerpilih;
                        jumlahPesanan[jumlahItemPesanan] = jumlah;
                        jumlahItemPesanan++;

                        System.out.println("Pesanan dicatat: " + jumlah + " x " + menuTerpilih.getNama());
                    } else {
                        System.out.println("Kapasitas pesanan sudah penuh. Pesanan baru tidak dapat ditambahkan.");
                        System.out.println("Data pesanan yang sudah tersimpan tetap dipertahankan.");
                    }

                    System.out.println();
                }
            }
        }

        System.out.println("Pemesanan telah berakhir.");
        return jumlahItemPesanan;
    }

    private static int cariIndeksPesanan(Menu[] pesanan, int jumlahItemPesanan, Menu menuDicari) {
        for (int i = 0; i < jumlahItemPesanan; i++) {
            if (pesanan[i] == menuDicari) {
                return i;
            }
        }

        return -1;
    }

    private static void tampilkanRingkasanPesanan(Menu[] pesanan, int[] jumlahPesanan,
            int jumlahItemPesanan) {
        System.out.println();
        System.out.println("=== RINGKASAN PESANAN ===");

        if (jumlahItemPesanan == 0) {
            System.out.println("Belum ada pesanan.");
            return;
        }

        System.out.printf("%-4s %-20s %14s %8s %16s%n",
                "No", "Nama", "Harga Satuan", "Jumlah", "Nilai per Item");
        System.out.println("----------------------------------------------------------------------");

        for (int i = 0; i < jumlahItemPesanan; i++) {
            Menu menu = pesanan[i];
            int jumlah = jumlahPesanan[i];
            double nilaiItem = menu.getHarga() * jumlah;

            System.out.printf("%-4d %-20s %14s %8d %16s%n",
                    i + 1,
                    menu.getNama(),
                    formatRupiah(menu.getHarga()),
                    jumlah,
                    formatRupiah(nilaiItem));
        }

        double subtotal = hitungSubtotal(pesanan, jumlahPesanan, jumlahItemPesanan);
        double diskon = hitungDiskon(subtotal);
        int indeksPromo = cariIndeksPromoMinuman(
                pesanan, jumlahPesanan, jumlahItemPesanan, subtotal);

        System.out.println();
        System.out.println("Subtotal: " + formatRupiah(subtotal));
        System.out.println("Diskon (10%): " + formatRupiah(diskon));

        double nilaiPromo = 0;

        if (indeksPromo != -1) {
            Menu minumanPromo = pesanan[indeksPromo];
            int unitGratis = hitungUnitGratis(jumlahPesanan[indeksPromo]);
            nilaiPromo = hitungNilaiPromo(minumanPromo, jumlahPesanan[indeksPromo]);

            System.out.println("Promo Beli 1 Gratis 1: " + minumanPromo.getNama()
                    + " (" + unitGratis + " unit gratis)");
            System.out.println("Nilai Promo: " + formatRupiah(nilaiPromo));
        } else {
            System.out.println("Promo Beli 1 Gratis 1: Tidak berlaku");
            System.out.println("Nilai Promo: " + formatRupiah(0));
        }

        double dasarPajak = hitungDasarPajak(subtotal, diskon, nilaiPromo);
        double pajak = hitungPajak(dasarPajak);
        double biayaPelayanan = hitungBiayaPelayanan(jumlahItemPesanan);

        System.out.println("Dasar Pengenaan Pajak: " + formatRupiah(dasarPajak));
        System.out.println("Pajak (10%): " + formatRupiah(pajak));
        System.out.println("Biaya Pelayanan: " + formatRupiah(biayaPelayanan));

        double totalAkhir = hitungTotalAkhir(
                subtotal, diskon, nilaiPromo, pajak, biayaPelayanan);
        System.out.println();
        System.out.println("Total Akhir: " + formatRupiah(totalAkhir));
    }
}
