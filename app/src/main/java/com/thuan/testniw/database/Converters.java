package com.thuan.testniw.database;

import androidx.room.TypeConverter;

import com.thuan.testniw.model.License;
import com.thuan.testniw.model.Owner;


public class Converters {
    @TypeConverter
    public static String ConvertOwnner(Owner owner) {
        return owner == null ? null : owner.toString();
    }

    @TypeConverter
    public static Owner ConvertOwnner(String owner) {
        return owner == null ? null : new Owner(owner);
    }

    @TypeConverter
    public static String ConvertLicense(License License) {
        return License == null ? null : License.toString();
    }

    @TypeConverter
    public static License ConvertLicense(String License) {
        return License == null ? null : new License(License);
    }

}
