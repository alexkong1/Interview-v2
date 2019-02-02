package com.zumepizza.interview.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private int id;
    private String name;
    private String price;
    @SerializedName("menu_description")
    private String menuDescription;
    private Classification classifications;
    private Assets assets;
    private List<Topping> toppings = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public Classification getClassifications() {
        return classifications;
    }

    public Assets getAssets() {
        return assets;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public class Classification {
        private boolean vegetarian;
        @SerializedName("gluten_free")
        private boolean glutenFree;

        public boolean isVegetarian() {
            return vegetarian;
        }

        public boolean isGlutenFree() {
            return glutenFree;
        }
    }

    public class Asset {
        private String url;

        public String getUrl() {
            return url;
        }

    }

    public class Assets {
        private List<Asset> menu;
        @SerializedName("product_details_page")
        private List<Asset> productDetailsPage;

        public List<Asset> getMenu() {
            return menu;
        }

        public List<Asset> getProductDetailsPage() {
            return productDetailsPage;
        }
    }

    public class Topping {
        private int id;
        private String name;
        private String description;
        private Asset asset;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Asset getAsset() {
            return asset;
        }
    }
}
