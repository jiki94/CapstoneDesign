package com.example.expirationdateapp.viewing;

import com.example.expirationdateapp.db.Product;

import java.util.Comparator;

enum SortingType {
    SORT_BY_NAME, SORT_BY_NAME_REVERSE, SORT_BY_EXPIRY_DATE, SORT_BY_EXPIRY_DATE_REVERSE;

    private static final Comparator<Product> sortByName = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.name.compareTo(o2.name);
        }
    };

    private static final Comparator<Product> sortByNameReverse = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o2.name.compareTo(o1.name);
        }
    };

    private static final Comparator<Product> sortByExpiryDate = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.expiryDate.compareTo(o2.expiryDate);
        }
    };

    private static final Comparator<Product> sortByExpiryDateReverse = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return o2.expiryDate.compareTo(o1.expiryDate);
        }
    };

    static SortingType getDefaultSortFlag(){
        return SORT_BY_EXPIRY_DATE;
    }

    Comparator<Product> getAssociatedComparator(){
        switch (this){
            case SORT_BY_NAME:
                return sortByName;
            case SORT_BY_NAME_REVERSE:
                return sortByNameReverse;
            case SORT_BY_EXPIRY_DATE:
                return sortByExpiryDate;
            case SORT_BY_EXPIRY_DATE_REVERSE:
                return sortByExpiryDateReverse;
            default:
                throw new IllegalArgumentException();
        }
    }

    boolean isConjugate(SortingType other){
        switch (this){
            case SORT_BY_NAME:
                return other == SORT_BY_NAME_REVERSE;
            case SORT_BY_NAME_REVERSE:
                return other == SORT_BY_NAME;
            case SORT_BY_EXPIRY_DATE:
                return other == SORT_BY_EXPIRY_DATE_REVERSE;
            case SORT_BY_EXPIRY_DATE_REVERSE:
                return other == SORT_BY_EXPIRY_DATE;
            default:
                throw new IllegalArgumentException();
        }
    }
}

class SortByName implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return 0;
    }

}