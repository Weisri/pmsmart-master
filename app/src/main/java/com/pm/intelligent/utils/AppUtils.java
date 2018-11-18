package com.pm.intelligent.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pm.intelligent.MyApplication;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/9/6.
 */

public class AppUtils {
    private static Gson gson;


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(String.class, STRING);
        gson = gsonBuilder.create();
        return gson;
    }


    //自定义Strig适配器
    public static final TypeAdapter STRING = new TypeAdapter() {
        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            if (value == null) {
                // 在这里处理null改为空字符串
                out.value("");
                return;
            }
            out.value(String.valueOf(value));
        }

        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

    };


}

