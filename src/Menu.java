public class Menu {
    private String nama;
    private double harga;
    private String kategori;

    public Menu(String nama, double harga, String kategori) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong.");
        }
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga menu harus lebih besar dari nol.");
        }
        if (kategori == null || (!kategori.equals("Makanan") && !kategori.equals("Minuman"))) {
            throw new IllegalArgumentException("Kategori menu harus Makanan atau Minuman.");
        }

        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }
}
