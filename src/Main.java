import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final int KAPASITAS_PESANAN = 5;
    private static final double BATAS_DISKON = 100000;
    private static final double BATAS_PROMO = 50000;
    private static final double PERSENTASE_DISKON = 0.10;
    private static final double TARIF_PAJAK = 0.10;
    private static final double BIAYA_PELAYANAN = 20000;

    private static Menu[] daftarMenu;

    public static void main(String[] args) {
        daftarMenu = buatDaftarMenu();
        Scanner scanner = new Scanner(System.in);

        boolean keluarAplikasi = false;

        while (!keluarAplikasi) {
            tampilkanMenuUtama();
            int pilihanUtama = bacaPilihanMenu(scanner, 0, 2);

            if (pilihanUtama == 0) {
                keluarAplikasi = true;
                System.out.println("Terima kasih. Aplikasi telah keluar.");
            } else if (pilihanUtama == 1) {
                jalankanPemesananPelanggan(scanner);
            } else if (pilihanUtama == 2) {
                jalankanMenuPemilik(scanner);
            }
        }

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

    private static void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("              MENU UTAMA");
        System.out.println("========================================");
        System.out.println("1. Customer");
        System.out.println("2. Restaurant Owner");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private static void tampilkanDaftarMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("          DAFTAR MENU RESTORAN");
        System.out.println("========================================");
        System.out.printf("%-4s %-20s %-10s %12s%n", "No", "Nama", "Kategori", "Harga");
        System.out.println("----------------------------------------");

        tampilkanMenuBerdasarkanKategori(daftarMenu, "Makanan");
        System.out.println();
        tampilkanMenuBerdasarkanKategori(daftarMenu, "Minuman");
        System.out.println("========================================");
    }

    private static void tampilkanMenuPemilik() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("            MENU PEMILIK RESTORAN");
        System.out.println("========================================");
        System.out.println("1. Manage Restaurant Menu");
        System.out.println("0. Back to Main Menu");
        System.out.println("========================================");
    }

    private static void tampilkanMenuPengelolaanMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("           PENGELOLAAN MENU");
        System.out.println("========================================");
        System.out.println("1. Add menu items");
        System.out.println("2. Change menu prices");
        System.out.println("3. Delete menu items");
        System.out.println("0. Back to Owner Menu");
        System.out.println("========================================");
    }

    private static void tampilkanMenuBerdasarkanKategori(Menu[] daftarMenuArr, String kategori) {
        System.out.println("--- " + kategori.toUpperCase() + " ---");

        for (int i = 0; i < daftarMenuArr.length; i++) {
            if (daftarMenuArr[i] != null && daftarMenuArr[i].getKategori().equals(kategori)) {
                System.out.printf("%-4d %-20s %-10s %12s%n",
                        i + 1,
                        daftarMenuArr[i].getNama(),
                        daftarMenuArr[i].getKategori(),
                        formatRupiah(daftarMenuArr[i].getHarga()));
            }
        }
    }

    private static int bacaPilihanMenu(Scanner scanner, int minPilihan, int maxPilihan) {
        while (true) {
            try {
                if (!scanner.hasNextLine()) {
                    System.out.println();
                    System.out.println("Terima kasih. Aplikasi telah keluar.");
                    return 0;
                }

                System.out.print("Masukkan pilihan: ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Pilihan tidak boleh kosong. Silakan pilih nomor yang tersedia.");
                    continue;
                }

                try {
                    long nilaiLong = Long.parseLong(input);

                    if (nilaiLong < Integer.MIN_VALUE || nilaiLong > Integer.MAX_VALUE) {
                        System.out.println("Pilihan terlalu besar. Silakan pilih nomor yang tersedia.");
                        continue;
                    }

                    int pilihan = (int) nilaiLong;

                    if (pilihan < minPilihan || pilihan > maxPilihan) {
                        System.out.println("Pilihan tidak valid. Silakan pilih nomor yang tersedia.");
                        continue;
                    }

                    return pilihan;
                } catch (NumberFormatException e) {
                    System.out.println("Pilihan harus berupa angka. Silakan pilih nomor yang tersedia.");
                }
            } catch (Exception e) {
                System.out.println();
                System.out.println("Terima kasih. Aplikasi telah keluar.");
                return 0;
            }
        }
    }

    private static void jalankanPemesananPelanggan(Scanner scanner) {
        Menu[] pesanan = new Menu[KAPASITAS_PESANAN];
        int[] jumlahPesanan = new int[KAPASITAS_PESANAN];

        tampilkanDaftarMenu();
        int jumlahItemPesanan = prosesPemesanan(scanner, daftarMenu, pesanan, jumlahPesanan);
        tampilkanRingkasanPesanan(pesanan, jumlahPesanan, jumlahItemPesanan);

        System.out.println();
        System.out.println("Kembali ke menu utama.");
    }

    private static void jalankanMenuPemilik(Scanner scanner) {
        boolean kembaliKeUtama = false;

        while (!kembaliKeUtama) {
            tampilkanMenuPemilik();
            int pilihanPemilik = bacaPilihanMenu(scanner, 0, 1);

            if (pilihanPemilik == 0) {
                kembaliKeUtama = true;
                System.out.println("Kembali ke menu utama.");
            } else if (pilihanPemilik == 1) {
                jalankanMenuPengelolaanMenu(scanner);
            }
        }
    }

    private static void jalankanMenuPengelolaanMenu(Scanner scanner) {
        boolean kembaliKePemilik = false;

        while (!kembaliKePemilik) {
            tampilkanMenuPengelolaanMenu();
            int pilihanPengelolaan = bacaPilihanMenu(scanner, 0, 3);

            if (pilihanPengelolaan == 0) {
                kembaliKePemilik = true;
                System.out.println("Kembali ke menu pemilik.");
            } else if (pilihanPengelolaan == 1) {
                tambahMenuItems(scanner);
            } else if (pilihanPengelolaan == 2) {
                ubahHargaMenu(scanner);
            } else if (pilihanPengelolaan == 3) {
                hapusMenuItems(scanner);
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
            try {
                System.out.print("Masukkan nomor menu: ");
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    pemesananSelesai = true;
                    break;
                }

                String inputMenu = scanner.nextLine().trim();

                if (inputMenu.equalsIgnoreCase("selesai")) {
                    pemesananSelesai = true;
                    continue;
                }

                if (inputMenu.isEmpty()) {
                    System.out.println("Nomor menu tidak boleh kosong. Silakan pilih nomor menu yang tersedia.");
                    continue;
                }

                int nomorMenu = 0;
                boolean nomorValid = false;

                try {
                    long nomorLong = Long.parseLong(inputMenu);

                    if (nomorLong >= 1 && nomorLong <= daftarMenu.length && nomorLong <= Integer.MAX_VALUE) {
                        nomorMenu = (int) nomorLong;
                        if (daftarMenu[nomorMenu - 1] != null) {
                            nomorValid = true;
                        }
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
                    try {
                        System.out.print("Masukkan jumlah: ");
                        System.out.flush();
                        if (!scanner.hasNextLine()) {
                            pemesananSelesai = true;
                            break;
                        }

                        String inputJumlah = scanner.nextLine().trim();

                        if (inputJumlah.equalsIgnoreCase("selesai")) {
                            pemesananSelesai = true;
                            break;
                        } else if (inputJumlah.isEmpty()) {
                            System.out.println("Jumlah tidak boleh kosong. Silakan masukkan bilangan bulat positif.");
                            continue;
                        }

                        try {
                            long jumlahLong = Long.parseLong(inputJumlah);

                            if (jumlahLong < Integer.MIN_VALUE || jumlahLong > Integer.MAX_VALUE) {
                                System.out.println("Jumlah terlalu besar. Silakan masukkan bilangan bulat positif.");
                                continue;
                            }

                            jumlah = (int) jumlahLong;

                            if (jumlah > 0) {
                                jumlahValid = true;
                            } else {
                                System.out.println("Jumlah harus berupa bilangan bulat positif. Silakan coba lagi.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Jumlah harus berupa bilangan bulat positif. Silakan coba lagi.");
                        }
                    } catch (Exception e) {
                        pemesananSelesai = true;
                        break;
                    }
                }

                if (pemesananSelesai) {
                    continue;
                }

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
            } catch (Exception e) {
                pemesananSelesai = true;
                break;
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

    private static int cariIndeksMenuKosong(Menu[] menuArr) {
        for (int i = 0; i < menuArr.length; i++) {
            if (menuArr[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private static void perluasDaftarMenuJikaPenuh() {
        int indeksKosong = cariIndeksMenuKosong(daftarMenu);

        if (indeksKosong == -1) {
            int kapasitasBaru = daftarMenu.length * 2;

            if (kapasitasBaru < daftarMenu.length + 1) {
                kapasitasBaru = daftarMenu.length + 1;
            }

            daftarMenu = Arrays.copyOf(daftarMenu, kapasitasBaru);
            System.out.println("Kapasitas menu diperluas menjadi " + kapasitasBaru + " slot.");
        }
    }

    private static String bacaInputWajib(Scanner scanner, String pesan) {
        while (true) {
            try {
                System.out.print(pesan);
                System.out.flush();

                if (!scanner.hasNextLine()) {
                    return null;
                }

                String input = scanner.nextLine();
                String trimmed = input.trim();

                if (trimmed.isEmpty()) {
                    System.out.println("Input tidak boleh kosong. Silakan coba lagi.");
                    continue;
                }

                return trimmed;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static String bacaNamaMenu(Scanner scanner) {
        while (true) {
            String nama = bacaInputWajib(scanner, "Nama menu (kosongkan untuk selesai): ");

            if (nama == null) {
                return null;
            }

            if (nama.equalsIgnoreCase("selesai")) {
                return "SELESAI";
            }

            if (nama.matches("^-?\\d+(\\.\\d+)?$")) {
                System.out.println("Nama menu tidak boleh berupa angka. Silakan masukkan nama menu.");
                continue;
            }

            return nama;
        }
    }

    private static double bacaHargaMenu(Scanner scanner) {
        while (true) {
            String input = bacaInputWajib(scanner, "Harga menu (Rp): ");

            if (input == null) {
                return -1;
            }

            if (input.equalsIgnoreCase("selesai")) {
                return -2;
            }

            String hargaBersih = input.replace("Rp", "").replace("rp", "").replace(".", "").replace(",", ".").trim();

            try {
                String numericOnly = hargaBersih.replace(".", "");

                if (!numericOnly.matches("\\d+")) {
                    System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
                    continue;
                }

                if (numericOnly.startsWith("0") && numericOnly.length() > 1) {
                    System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
                    continue;
                }

                long hargaLong = Long.parseLong(numericOnly);

                if (hargaLong <= 0) {
                    System.out.println("Harga harus berupa bilangan positif lebih besar dari 0.");
                    continue;
                }

                double harga = (double) hargaLong;

                return harga;
            } catch (NumberFormatException e) {
                System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
            }
        }
    }

    private static String bacaKategoriMenu(Scanner scanner) {
        while (true) {
            String input = bacaInputWajib(scanner, "Kategori (Makanan/Minuman, kosongkan untuk selesai): ");

            if (input == null) {
                return null;
            }

            if (input.equalsIgnoreCase("selesai")) {
                return "SELESAI";
            }

            String kategori = input.trim();

            if (kategori.equalsIgnoreCase("makanan")) {
                return "Makanan";
            }

            if (kategori.equalsIgnoreCase("minuman")) {
                return "Minuman";
            }

            System.out.println("Kategori tidak valid. Harus Makanan atau Minuman.");
        }
    }

    private static void tambahMenuItems(Scanner scanner) {
        System.out.println();
        System.out.println("=== TAMBAH MENU ITEM ===");
        System.out.println("Ketik 'selesai' pada nama menu untuk mengakhiri sesi penambahan.");

        java.util.ArrayList<Menu> menuDitambahkan = new java.util.ArrayList<Menu>();

        boolean selesaiTambah = false;

        while (!selesaiTambah) {
            String namaMenu = bacaNamaMenu(scanner);

            if (namaMenu == null) {
                selesaiTambah = true;
                break;
            }

            if (namaMenu.equals("SELESAI")) {
                selesaiTambah = true;
                break;
            }

            double hargaMenu = bacaHargaMenu(scanner);

            if (hargaMenu == -1) {
                selesaiTambah = true;
                break;
            }

            if (hargaMenu == -2) {
                selesaiTambah = true;
                break;
            }

            String kategoriMenu = bacaKategoriMenu(scanner);

            if (kategoriMenu == null) {
                selesaiTambah = true;
                break;
            }

            if (kategoriMenu.equals("SELESAI")) {
                selesaiTambah = true;
                break;
            }

            try {
                Menu menuBaru = new Menu(namaMenu, hargaMenu, kategoriMenu);
                perluasDaftarMenuJikaPenuh();
                int indeksKosong = cariIndeksMenuKosong(daftarMenu);

                if (indeksKosong == -1) {
                    daftarMenu = Arrays.copyOf(daftarMenu, daftarMenu.length + 1);
                    indeksKosong = daftarMenu.length - 1;
                }

                daftarMenu[indeksKosong] = menuBaru;
                menuDitambahkan.add(menuBaru);

                System.out.println("Menu berhasil ditambahkan: " + menuBaru.getNama()
                        + " | " + menuBaru.getKategori()
                        + " | " + formatRupiah(menuBaru.getHarga()));
                System.out.println("Tambahkan menu lain atau ketik 'selesai' pada nama menu.");
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Gagal menambahkan menu: " + e.getMessage());
                System.out.println("Silakan coba lagi dari awal item ini.");
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("=== RINGKASAN PENAMBAHAN MENU ===");

        if (menuDitambahkan.isEmpty()) {
            System.out.println("Tidak ada menu baru yang ditambahkan.");
        } else {
            for (int i = 0; i < menuDitambahkan.size(); i++) {
                Menu menuBaru = menuDitambahkan.get(i);
                System.out.println((i + 1) + ". " + menuBaru.getNama()
                        + " - " + menuBaru.getKategori()
                        + " - " + formatRupiah(menuBaru.getHarga()));
            }

            System.out.println("Jumlah menu ditambahkan: " + menuDitambahkan.size());
        }

        System.out.println();
        System.out.println("Kembali ke menu pengelolaan menu.");
    }

    private static void tampilkanBarisItem(int nomor, Menu menu, int jumlah, double nilaiItem) {
        int lebarNama = 20;
        String nama = menu.getNama();
        int awal = 0;
        boolean barisPertama = true;

        while (awal < nama.length()) {
            int akhir = Math.min(awal + lebarNama, nama.length());

            if (akhir < nama.length()) {
                int spasiTerakhir = nama.lastIndexOf(' ', akhir - 1);

                if (spasiTerakhir > awal) {
                    akhir = spasiTerakhir;
                }
            }

            String potonganNama = nama.substring(awal, akhir).trim();

            if (barisPertama) {
                System.out.printf("%-4d %-20s %14s %8d %16s%n",
                        nomor,
                        potonganNama,
                        formatRupiah(menu.getHarga()),
                        jumlah,
                        formatRupiah(nilaiItem));
                barisPertama = false;
            } else {
                System.out.printf("%-4s %-20s%n", "", potonganNama);
            }

            awal = akhir;

            while (awal < nama.length() && nama.charAt(awal) == ' ') {
                awal++;
            }
        }
    }

    private static void tampilkanRingkasanPesanan(Menu[] pesanan, int[] jumlahPesanan,
            int jumlahItemPesanan) {
        System.out.println();
        System.out.println("=== RINGKASAN PESANAN ===");

        if (jumlahItemPesanan == 0) {
            System.out.println("Belum ada pesanan.");
            return;
        }

        System.out.println("Rincian Item:");
        System.out.printf("%-4s %-20s %14s %8s %16s%n",
                "No", "Nama", "Harga Satuan", "Jumlah", "Nilai per Item");
        System.out.println("----------------------------------------------------------------------");

        for (int i = 0; i < jumlahItemPesanan; i++) {
            Menu menu = pesanan[i];
            int jumlah = jumlahPesanan[i];
            double nilaiItem = menu.getHarga() * jumlah;

            tampilkanBarisItem(i + 1, menu, jumlah, nilaiItem);
        }

        double subtotal = hitungSubtotal(pesanan, jumlahPesanan, jumlahItemPesanan);
        double diskon = hitungDiskon(subtotal);
        int indeksPromo = cariIndeksPromoMinuman(
                pesanan, jumlahPesanan, jumlahItemPesanan, subtotal);

        System.out.println();
        System.out.println("Rincian Biaya:");
        System.out.println("Subtotal: " + formatRupiah(subtotal));

        if (diskon > 0) {
            System.out.println("Diskon (10%): " + formatRupiah(diskon));
        } else {
            System.out.println("Diskon (10%): " + formatRupiah(0) + " (tidak berlaku)");
        }

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
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Total Akhir: " + formatRupiah(totalAkhir));
    }

    private static int cariJumlahMenuTersedia(Menu[] menuArr) {
        int jumlah = 0;

        for (int i = 0; i < menuArr.length; i++) {
            if (menuArr[i] != null) {
                jumlah++;
            }
        }

        return jumlah;
    }

    private static int getIndeksMenuBerdasarkanNomorTampil(Menu[] menuArr, int nomorTampil) {
        int hitung = 0;

        for (int i = 0; i < menuArr.length; i++) {
            if (menuArr[i] != null) {
                hitung++;
                if (hitung == nomorTampil) {
                    return i;
                }
            }
        }

        return -1;
    }

    private static int bacaNomorMenuUntukPemilik(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Pilih nomor menu (0 untuk kembali): ");
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    return -1;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Nomor menu tidak boleh kosong. Silakan pilih nomor menu yang tersedia.");
                    continue;
                }

                try {
                    long nilaiLong = Long.parseLong(input);

                    if (nilaiLong == 0) {
                        return 0;
                    }

                    if (nilaiLong < Integer.MIN_VALUE || nilaiLong > Integer.MAX_VALUE) {
                        System.out.println("Nomor menu tidak valid. Silakan pilih nomor menu yang tersedia.");
                        continue;
                    }

                    int nomorTampil = (int) nilaiLong;
                    int jumlahTersedia = cariJumlahMenuTersedia(daftarMenu);

                    if (nomorTampil < 1 || nomorTampil > jumlahTersedia) {
                        System.out.println("Nomor menu tidak valid. Silakan pilih nomor menu yang tersedia.");
                        continue;
                    }

                    int indeksMenu = getIndeksMenuBerdasarkanNomorTampil(daftarMenu, nomorTampil);

                    if (indeksMenu == -1 || daftarMenu[indeksMenu] == null) {
                        System.out.println("Nomor menu tidak valid. Silakan pilih nomor menu yang tersedia.");
                        continue;
                    }

                    return nomorTampil;
                } catch (NumberFormatException e) {
                    System.out.println("Nomor menu tidak valid. Silakan pilih nomor menu yang tersedia.");
                }
            } catch (Exception e) {
                return -1;
            }
        }
    }

    private static double bacaHargaBaru(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Masukkan harga baru (Rp): ");
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    return -1;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Harga tidak boleh kosong. Silakan masukkan harga positif.");
                    continue;
                }

                if (input.equalsIgnoreCase("selesai") || input.equalsIgnoreCase("batal")) {
                    return -2;
                }

                String hargaBersih = input.replace("Rp", "").replace("rp", "").replace(".", "").replace(",", ".").trim();

                if (!hargaBersih.matches("\\d+(\\.0+)?")) {
                    System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
                    continue;
                }

                String numericOnly = hargaBersih.split("\\.")[0];

                if (numericOnly.startsWith("0") && numericOnly.length() > 1) {
                    System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
                    continue;
                }

                try {
                    long hargaLong = Long.parseLong(numericOnly);

                    if (hargaLong <= 0) {
                        System.out.println("Harga harus berupa bilangan positif lebih besar dari 0.");
                        continue;
                    }

                    return (double) hargaLong;
                } catch (NumberFormatException e) {
                    System.out.println("Harga tidak valid. Masukkan angka positif (contoh: 25000).");
                }
            } catch (Exception e) {
                return -1;
            }
        }
    }

    private static boolean bacaKonfirmasi(Scanner scanner, String pesan) {
        while (true) {
            try {
                System.out.print(pesan);
                System.out.flush();
                if (!scanner.hasNextLine()) {
                    return false;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Konfirmasi tidak boleh kosong. Masukkan Ya atau Tidak.");
                    continue;
                }

                if (input.equalsIgnoreCase("ya")) {
                    return true;
                }

                if (input.equalsIgnoreCase("tidak")) {
                    return false;
                }

                System.out.println("Konfirmasi tidak valid. Masukkan Ya atau Tidak.");
            } catch (Exception e) {
                return false;
            }
        }
    }

    private static void ubahHargaMenu(Scanner scanner) {
        System.out.println();
        System.out.println("=== UBAH HARGA MENU ===");

        tampilkanDaftarMenu();

        int nomorTampil = bacaNomorMenuUntukPemilik(scanner);

        if (nomorTampil == 0) {
            System.out.println("Perubahan harga dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        if (nomorTampil == -1) {
            System.out.println("Perubahan harga dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        int indeksMenu = getIndeksMenuBerdasarkanNomorTampil(daftarMenu, nomorTampil);

        if (indeksMenu == -1 || daftarMenu[indeksMenu] == null) {
            System.out.println("Nomor menu tidak valid. Perubahan harga dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        Menu menuDipilih = daftarMenu[indeksMenu];

        double hargaBaru = bacaHargaBaru(scanner);

        if (hargaBaru == -1) {
            System.out.println("Perubahan harga dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        if (hargaBaru == -2) {
            System.out.println("Perubahan harga dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        System.out.println();
        System.out.println("=== RINGKASAN PERUBAHAN HARGA ===");
        System.out.println("Nama Menu   : " + menuDipilih.getNama());
        System.out.println("Kategori    : " + menuDipilih.getKategori());
        System.out.println("Harga Saat Ini : " + formatRupiah(menuDipilih.getHarga()));
        System.out.println("Harga Baru     : " + formatRupiah(hargaBaru));
        System.out.println();

        boolean konfirmasiUbah = bacaKonfirmasi(scanner, "Konfirmasi perubahan? (Ya/Tidak): ");

        if (!konfirmasiUbah) {
            System.out.println("Perubahan dibatalkan. Harga tetap " + formatRupiah(menuDipilih.getHarga()) + ".");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        try {
            menuDipilih.setHarga(hargaBaru);
            System.out.println("Harga menu berhasil diperbarui.");
            System.out.println(menuDipilih.getNama() + " - " + formatRupiah(menuDipilih.getHarga()));
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal memperbarui harga: " + e.getMessage());
            System.out.println("Perubahan dibatalkan. Harga tetap " + formatRupiah(menuDipilih.getHarga()) + ".");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
        }
    }

    private static void hapusMenuItems(Scanner scanner) {
        System.out.println();
        System.out.println("=== HAPUS MENU ITEM ===");

        tampilkanDaftarMenu();

        int nomorTampil = bacaNomorMenuUntukPemilik(scanner);

        if (nomorTampil == 0) {
            System.out.println("Penghapusan dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        if (nomorTampil == -1) {
            System.out.println("Penghapusan dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        int indeksMenu = getIndeksMenuBerdasarkanNomorTampil(daftarMenu, nomorTampil);

        if (indeksMenu == -1 || daftarMenu[indeksMenu] == null) {
            System.out.println("Nomor menu tidak valid. Penghapusan dibatalkan.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        Menu menuDipilih = daftarMenu[indeksMenu];

        System.out.println();
        System.out.println("=== RINGKASAN PENGHAPUSAN MENU ===");
        System.out.println("Nama Menu   : " + menuDipilih.getNama());
        System.out.println("Kategori    : " + menuDipilih.getKategori());
        System.out.println("Harga Saat Ini : " + formatRupiah(menuDipilih.getHarga()));
        System.out.println();

        boolean konfirmasiHapus = bacaKonfirmasi(scanner, "Konfirmasi penghapusan? (Ya/Tidak): ");

        if (!konfirmasiHapus) {
            System.out.println("Penghapusan dibatalkan. Menu tetap tersedia.");
            System.out.println();
            System.out.println("Kembali ke menu pengelolaan menu.");
            return;
        }

        if (indeksMenu >= 0 && indeksMenu < daftarMenu.length - 1) {
            for (int i = indeksMenu; i < daftarMenu.length - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];
            }
        }

        if (daftarMenu.length > 0) {
            daftarMenu[daftarMenu.length - 1] = null;
        }

        System.out.println("Menu berhasil dihapus.");
        System.out.println(menuDipilih.getNama() + " telah dihapus dari daftar menu.");
        System.out.println("Jumlah menu aktif saat ini: " + cariJumlahMenuTersedia(daftarMenu));
        System.out.println();
        System.out.println("Kembali ke menu pengelolaan menu.");
    }
}
