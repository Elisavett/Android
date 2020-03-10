package com.example.lab3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lab3.adapter.StudentsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;


public class Lab3Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab3Activity.class);
    }
    private final StudentsCache studentsCache = StudentsCache.getInstance();
    private final GroupCache groupsCache = GroupCache.getInstance();

    private RecyclerView list;
    private FloatingActionButton fab;
    private FloatingActionButton fabGroup;

    private static final int REQUEST_STUDENT_ADD = 1;
    private static final int REQUEST_GROUP_ADD = 2;
    private static final int DIALOG_EXIT = 1;
    private static final String EXTRA_GROUP = "group";


    private StudentsAdapter studentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity);
        setTitle(getString(R.string.lab3_title, getClass().getSimpleName()));
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        fabGroup = findViewById(R.id.fabGroup);


        /*
        Здесь идёт инициализация RecyclerView. Первое, что необходимо для его работы, это установить
        реализацию LayoutManager-а. Он содержит логику размещения View внутри RecyclerView. Так,
        LinearLayoutManager, который используется ниже, располагает View последовательно, друг за
        другом, по аналогии с LinearLayout-ом. Из альтернатив можно например использовать
        GridLayoutManager, который располагает View в виде таблицы. Необходимость написания своего
        LayoutManager-а возникает очень редко и при этом является весьма сложным процессом, поэтому
        рассматриваться в лабораторной работе не будет.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        /*
        Следующий ключевой компонент - это RecyclerView.Adapter. В нём описывается вся информация,
        необходимая для заполнения RecyclerView. В примере мы выводим пронумерованный список
        студентов, подробнее о работе адаптера в документации к классу StudentsAdapter.
         */

        /*
        При нажатии на кнопку мы переходим на Activity для добавления студента. Обратите внимание,
        что здесь используется метод startActivityForResult. Этот метод позволяет организовывать
        передачу данных обратно от запущенной Activity. В нашем случае, после закрытия AddStudentActivity,
        у нашей Activity будет вызван метод onActivityResult, в котором будут данные, которые мы
        указали перед закрытием AddStudentActivity.
         */
        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
        fabGroup.setOnClickListener(v->showDialog(DIALOG_EXIT));
    }

    protected Dialog onCreateDialog(int id) {
        EditText groupName = new EditText(this);
        if (id == DIALOG_EXIT) {

            //LayoutInflater inflater = getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // заголовок
            builder.setTitle(R.string.lab3_dialog_title)
                    .setView(groupName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Group group = new Group(
                            groupName.getText().toString()
                    );
                    Intent data = new Intent();
                    // Сохраяем объект студента. Для того, чтобы сохранить объект класса, он должен реализовывать
                    // интерфейс Parcelable или Serializable, т.к. Intent передаётся в виде бинарных данных
                    data.putExtra(EXTRA_GROUP, group);
                    Group group1 = data.getParcelableExtra(EXTRA_GROUP);

                    groupsCache.addGroup(group1);

                    //studentsAdapter.setGroups(groupsCache.getGroups());

                }
            });
            return builder.create();
        }
        return super.onCreateDialog(id);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentsCache.addStudent(student);

            ArrayList<Student> SortedStudents = new ArrayList<>();
            boolean isFound;
            List<Student> unsortedStudents = studentsCache.getStudents();
            List<ListItem> sortedStudents = new ArrayList<>();
            for (int j = 0; j < unsortedStudents.size(); j++ ) {
                isFound = false;
                Student currStudent = unsortedStudents.get(j);
                for(int i = 0; i < sortedStudents.size(); i++)
                {
                    if(sortedStudents.get(i).getType() == 0) continue;
                    Student st = (Student)sortedStudents.get(i);
                    String currStudentGroup = st.groupName;
                    if(currStudentGroup.equals(currStudent.groupName))
                    {
                        sortedStudents.add(i, currStudent);
                        isFound = true;
                        break;
                    }
                }
                if(isFound == false)
                {
                    Group group = new Group(currStudent.groupName);
                    sortedStudents.add(group);
                    sortedStudents.add(currStudent);
                }
            }
            list.setAdapter(studentsAdapter = new StudentsAdapter());
            studentsAdapter.setStudents(sortedStudents);
            studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }
        /*if (requestCode == REQUEST_GROUP_ADD && resultCode == RESULT_OK) {
            Group group = data.getParcelableExtra(EXTRA_GROUP);

            groupsCache.addGroup(group);

            studentsAdapter.setGroups(groupsCache.getGroups());
            studentsAdapter.notifyItemRangeInserted(studentsAdapter.getItemCount() - 2, 2);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
        }*/
    }
}
