package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab4.adapter.StudentsAdapter;
import com.example.lab4.db.GroupDao;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.StudentDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Lab4Activity extends AppCompatActivity {


    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab4Activity.class);
    }

    private RecyclerView list;
    private FloatingActionButton fab;
    private FloatingActionButton fabGroup;

    private StudentDao studentDao;
    private GroupDao groupDao;

    private static final int REQUEST_STUDENT_ADD = 3;
    private static final int REQUEST_GROUP_ADD = 2;
    private static final int DIALOG_EXIT = 1;
    private static final String EXTRA_GROUP = "group";


    private StudentsAdapter studentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activityl);
        setTitle(getString(R.string.lab4_title, getClass().getSimpleName()));
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        fabGroup = findViewById(R.id.fabGroup);
        studentDao = Lab4Database.getInstance(this).studentDao();
        groupDao = Lab4Database.getInstance(this).groupDao();
        List<Student> unsortedStudents = studentDao.getAll();

        list.setAdapter(studentsAdapter = new StudentsAdapter());
        studentsAdapter.setStudents(sortStudents(unsortedStudents));


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
        передачу данных обратно от запущенной Activity. В нашем случае, после закрытия lab4_AddStudentActivity,
        у нашей Activity будет вызван метод onActivityResult, в котором будут данные, которые мы
        указали перед закрытием lab4_AddStudentActivity.
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
            builder.setTitle(R.string.lab4_dialog_title)
                    .setView(groupName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Group group = new Group(
                                    groupName.getText().toString()
                            );
                            groupDao.insert(group);

                            //studentsAdapter.setGroups(groupDao.getAll());

                        }
                    });
            return builder.create();
        }
        return super.onCreateDialog(id);
    }
    boolean isFromAddStudentActivity = false;

    @Override
    protected void onResume() {
        if(isFromAddStudentActivity == false) {
            final SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(this);
            int position = preferences.getInt("position", 0);
            list.scrollToPosition(position);
            isFromAddStudentActivity = false;
        }
        super.onResume();
    }
    @Override
    protected void onPause() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        int firstVisiblePosition = ((LinearLayoutManager) list.getLayoutManager())
                .findFirstVisibleItemPosition();
        preferences.edit().putInt("position", firstVisiblePosition).apply();
        super.onPause();
    }

    public List<ListItem> sortStudents (List<Student> unsortedStudents)
    {
        boolean isFound;

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
                isNewGroup = 1;
                Group group = new Group(currStudent.groupName);
                sortedStudents.add(group);
                sortedStudents.add(currStudent);
            }
        }
        return sortedStudents;
    }
    public int isNewGroup = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);
            studentDao.insert(student);

            List<Student> unsortedStudents = studentDao.getAll();
            List<ListItem> sortedStudents = sortStudents(unsortedStudents);
            studentsAdapter.setStudents(sortedStudents);
            int insertionIndx = sortedStudents.indexOf(student);
            int insertionCount = 1;
            if(insertionIndx == sortedStudents.size()-1)//при добавлении студента он ставится первым в группе(sortStudents)
            {
                insertionCount++;//в данном случае кроме студента добавляется группа
                insertionIndx--;//группа указывается перед студентом
            }
            studentsAdapter.notifyItemRangeInserted(insertionIndx, insertionCount);
            list.scrollToPosition(studentsAdapter.getItemCount() - 1);
            isFromAddStudentActivity = true;
            isNewGroup = 0;
        }
    }

}
