package com.piccante.anurr.piccantemenu;

/**
 * Created by anurr on 10/20/2017.
 */

public class PastaHelper {
    private static int image;
    private static int id;
    private static String toppings;
    private static int amount;
    private static int sauce;
    private static int level;
    private static int harga;
    String title;

    public static int getSauce() {
        return sauce;
    }

    public static int getlevel() {
        return level;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public static String getToppings() {
        return toppings;
    }
    public void setToppings(String toppings) {
        this.toppings = toppings;
    }
    public static int getAmount() { return amount; }
    public void setAmount(int amount) {this.amount = amount;}
    public static int getHarga() { return harga; }
    public void setHarga(int harga) {this.harga = harga;}
    public static int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public static int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setSauce(int sauce) {
        this.sauce = sauce;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
