package com.example.expirationdateapp.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import org.threeten.bp.LocalDate;

// 상품 클래스
// 장바구니나 입력된 상품은 inBasket 변수로 구분
// expiryDate 가 Null 이면 유통기한 없는 제품
@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    @TypeConverters(LocalDateConverter.class)
    public LocalDate expiryDate;

    @NonNull
    @TypeConverters(StoredTypeConverter.class)
    public StoredType stored;

    public boolean inBasket;

    // id 는 언제나 0으로 사용해야됨
    // autoGenerate 되서
    public Product(int id, @NonNull String name, @NonNull LocalDate expiryDate,
                   @NonNull StoredType stored, boolean inBasket){
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.stored = stored;
        this.inBasket = inBasket;
    }

    // 장바구니 상품 리턴
    public static Product getBasketItem(String name, LocalDate expiryDate, StoredType stored){
        return new Product(0, name, expiryDate, stored, true);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        // id가 같은데 다른 content 가질 가능성 없을듯;
        Product other = (Product) obj;
        return id == other.id;
    }
}

