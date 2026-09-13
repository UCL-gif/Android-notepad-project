package com.example.mynotepad.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.mynotepad.NoteDBOpenHelper;
import com.example.mynotepad.bean.Note;

public class AlertDialogUtils {
    private static final String TAG = "AlertDialogUtils";

    public static void showDeleteAlertDialog(Context context, String title, String message, Note note, NoteDBOpenHelper helper,Runnable onDeleteSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "删除一条记事");
                long delete = helper.deleteOneNote(note.getId());
                if (delete > 0) {
                    Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                    //删除成功以后，你希望我顺便执行的一段代码
                    onDeleteSuccess.run();
                } else {
                    Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("我再想想", null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showTwoItemsAlertDialog(Context context, String title, String[] items, Runnable optionOne,Runnable optionTwo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    optionOne.run();
                } else if (which == 1) {
                    optionTwo.run();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
