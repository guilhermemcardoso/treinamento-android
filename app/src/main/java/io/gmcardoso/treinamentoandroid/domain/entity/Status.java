package io.gmcardoso.treinamentoandroid.domain.entity;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.gmcardoso.treinamentoandroid.R;

/**
 * Entidade da API Github Status
 *
 * @see <a href="https://status.github.com/api/last-message.json">Last Message</a>
 *
 * Created by guilherme on 09/01/2017.
 */

public class Status {

    @SerializedName("status")
    public Type type;
    public String body;
    public Date created_on;

    public enum Type {
        @SerializedName("good")
        GOOD(R.color.green),
        @SerializedName("minor")
        MINOR(R.color.yellow),
        @SerializedName("major")
        MAJOR(R.color.red),
        NONE(R.color.black);

        private int colorRes;

        Type(int colorRes) {
            this.colorRes = colorRes;
        }

        public int getColorRes() {
            return colorRes;
        }
    }

}
