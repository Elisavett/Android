package com.example.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab4.db.GroupDao;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.StudentDao;

public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";

    public static Intent newIntent(@NonNull Context context) {

        return new Intent(context, AddStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private StudentDao studentDao;
    private GroupDao groupDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_add_student_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        studentDao = Lab4Database.getInstance(this).studentDao();
        groupDao = Lab4Database.getInstance(this).groupDao();


        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        group = findViewById(R.id.SpinnerGroups);


        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_item, groupDao.getAll());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapter);


        Bundle arguments = getIntent().getExtras();
        Student student = null;
        if (arguments != null)
            student = (Student) arguments.get("Student");
        if (student != null) {
            firstName.setText(student.firstName);
            secondName.setText(student.secondName);
            lastName.setText(student.lastName);
            Group g = groupDao.getGroupById(student.groupId);
            group.setSelection(adapter.getPosition(g));
        }
    }


    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private Spinner group;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab4_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Если пользователь нажал "назад", то просто закрываем Activity
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        // Если пользователь нажал "Сохранить"
        Group selectedGroup = (Group) group.getSelectedItem();
        if (item.getItemId() == R.id.action_save) {
            // Создаём объект студента из введенных
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString(),
                    selectedGroup.getId()
            );

            // Проверяем, что все поля были указаны
            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                // Класс Toast позволяет показать системное уведомление поверх всего UI
                Toast.makeText(this, R.string.lab4_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }
            // Проверяем, что точно такого же студента в списке нет
            if (studentDao.count(student.firstName, student.secondName, student.lastName, student.groupId) > 0) {
                Toast.makeText(this, R.string.lab4_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            // Сохраняем Intent с инфорамцией от этой Activity, который будет передан в onActivityResult
            // вызвавшей его Activity.
            Intent data = new Intent();
            // Сохраяем объект студента. Для того, чтобы сохранить объект класса, он должен реализовывать
            // интерфейс Parcelable или Serializable, т.к. Intent передаётся в виде бинарных данных
            data.putExtra(EXTRA_STUDENT, student);
            // Указываем resultCode и сам Intent, которые будут переданы вызвавшей нас Activity в методе
            // onActivityResult
            setResult(RESULT_OK, data);
            // Закрываем нашу Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
