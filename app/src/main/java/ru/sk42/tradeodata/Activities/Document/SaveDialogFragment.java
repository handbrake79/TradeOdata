package ru.sk42.tradeodata.Activities.Document;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import ru.sk42.tradeodata.R;

/**
 * Created by хрюн моржов on 20.10.2016.
 */

public class SaveDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "SaveDialog****";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Документ").setPositiveButton("Да", this)
                .setNegativeButton("Нет", this)
                .setNeutralButton("Отмена", this)
                .setMessage("Сохранить изменения локально?");
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                DocumentActivity activity = (DocumentActivity) getActivity();
                activity.saveLocal();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, ": onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, ": onCancel");
    }

}
