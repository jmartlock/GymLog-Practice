package com.example.gymlog.Database.typeConverters;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTypeConverter {
    @TypeConverter
    public long convertDateToLong(LocalDateTime date){
        ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli(); // returns a long

}

    @TypeConverter
    public LocalDateTime convertLongToDate(Long epochMilli){
        Instant instant = Instant.ofEpochMilli(epochMilli);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
