package io.gmcardoso.treinamentoandroid.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;

import io.gmcardoso.treinamentoandroid.R;

/**
 * Created by guilherme on 11/01/2017.
 */

public final class AppUtils {

    private AppUtils() {}

    public static boolean validateRequiredFields(Context context, TextInputLayout... fields) {

        boolean isValid = true;

        for (TextInputLayout field : fields) {
            EditText editText = field.getEditText();
            if(editText != null) {
                if (TextUtils.isEmpty(editText.getText())) {
                    isValid = false;
                    field.setErrorEnabled(true);
                    field.setError(context.getString(R.string.msg_errorEmpty));
                } else {
                    field.setErrorEnabled(false);
                    field.setError(null);
                }
            } else {
                throw new RuntimeException("O campo deve possuir um EditText.");
            }
        }

        return isValid;

    }
}
