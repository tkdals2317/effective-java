package org.example.item46;

public class Album {
    private String name;
    public String artist;
    private Integer sales;

    @Override
    public String toString() {
        return name;
    }

    public Album(String name, String artist, Integer sales) {
        this.name = name;
        this.artist = artist;
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public static int sales(Album oldValue, Album newValue) {
        return Integer.compare(oldValue.sales, newValue.sales);
    }
}
