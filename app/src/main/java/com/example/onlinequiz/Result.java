package com.example.onlinequiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Result extends AppCompatActivity {

    private Question[] data;
    private String uid;
    private String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        quizID = getIntent().getStringExtra("Quiz ID");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getIntent().hasExtra("User UID")) uid = getIntent().getStringExtra("User UID");

        TextView title = findViewById(R.id.title);
        ListView listview = findViewById(R.id.listview);
        TextView total = findViewById(R.id.total);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Quizzes").hasChild(quizID)) {
                    DataSnapshot ansRef = snapshot.child("Quizzes").child(quizID).child("Answers").child(uid);
                    DataSnapshot qRef = snapshot.child("Quizzes").child(quizID);

                    // Null check for the quiz title
                    if (qRef.child("Title").exists() && qRef.child("Title").getValue() != null) {
                        title.setText(qRef.child("Title").getValue().toString());
                    } else {
                        title.setText("Unknown Quiz Title");
                    }

                    // Null check for total questions
                    int num = 0;
                    if (qRef.child("Total Questions").exists() && qRef.child("Total Questions").getValue() != null) {
                        num = Integer.parseInt(qRef.child("Total Questions").getValue().toString());
                    }

                    data = new Question[num];
                    int correctAns = 0;

                    for (int i = 0; i < num; i++) {
                        DataSnapshot qRef2 = qRef.child("Questions").child(String.valueOf(i));
                        Question question = new Question();

                        // Null checks for each field in the question
                        question.setQuestion(qRef2.child("Question").exists() && qRef2.child("Question").getValue() != null
                                ? qRef2.child("Question").getValue().toString()
                                : "No question text available");

                        question.setOption1(qRef2.child("Option 1").exists() && qRef2.child("Option 1").getValue() != null
                                ? qRef2.child("Option 1").getValue().toString()
                                : "Option 1 not available");

                        question.setOption2(qRef2.child("Option 2").exists() && qRef2.child("Option 2").getValue() != null
                                ? qRef2.child("Option 2").getValue().toString()
                                : "Option 2 not available");

                        question.setOption3(qRef2.child("Option 3").exists() && qRef2.child("Option 3").getValue() != null
                                ? qRef2.child("Option 3").getValue().toString()
                                : "Option 3 not available");

                        question.setOption4(qRef2.child("Option 4").exists() && qRef2.child("Option 4").getValue() != null
                                ? qRef2.child("Option 4").getValue().toString()
                                : "Option 4 not available");

                        // Handle selected answer safely
                        question.setSelectedAnswer(ansRef.child(String.valueOf(i + 1)).exists() &&
                                ansRef.child(String.valueOf(i + 1)).getValue() != null
                                ? Integer.parseInt(ansRef.child(String.valueOf(i + 1)).getValue().toString())
                                : -1);

                        // Handle correct answer safely
                        int ans = qRef2.child("Ans").exists() && qRef2.child("Ans").getValue() != null
                                ? Integer.parseInt(qRef2.child("Ans").getValue().toString())
                                : -1;

                        if (ans == question.getSelectedAnswer()) {
                            correctAns++;
                        }

                        question.setCorrectAnswer(ans);
                        data[i] = question;
                    }

                    total.setText("Total: " + correctAns + "/" + data.length);

                    ListAdapter listAdapter = new ListAdapter(data);
                    listview.setAdapter(listAdapter);

                } else {
                    Toast.makeText(Result.this, "Quiz not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Result.this, "Can't connect", Toast.LENGTH_SHORT).show();
            }
        };
        database.addValueEventListener(listener);
    }

    public class ListAdapter extends BaseAdapter {
        Question[] arr;

        ListAdapter(Question[] arr2) {
            arr = arr2;
        }

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int i) {
            return arr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.question, null);

            TextView question = v.findViewById(R.id.question);
            RadioButton option1 = v.findViewById(R.id.option1);
            RadioButton option2 = v.findViewById(R.id.option2);
            RadioButton option3 = v.findViewById(R.id.option3);
            RadioButton option4 = v.findViewById(R.id.option4);
            TextView result = v.findViewById(R.id.result);

            question.setText(data[i].getQuestion());
            option1.setText(data[i].getOption1());
            option2.setText(data[i].getOption2());
            option3.setText(data[i].getOption3());
            option4.setText(data[i].getOption4());

            switch (data[i].getSelectedAnswer()) {
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                case 4:
                    option4.setChecked(true);
                    break;
            }

            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
            option4.setEnabled(false);

            result.setVisibility(View.VISIBLE);

            if (data[i].getSelectedAnswer() == data[i].getCorrectAnswer()) {
                result.setBackgroundResource(R.drawable.green_background);
                result.setTextColor(ContextCompat.getColor(Result.this, R.color.green_dark));
                result.setText("Correct Answer");
            } else {
                result.setBackgroundResource(R.drawable.red_background);
                result.setTextColor(ContextCompat.getColor(Result.this, R.color.red_dark));
                result.setText("Wrong Answer");

                switch (data[i].getCorrectAnswer()) {
                    case 1:
                        option1.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 2:
                        option2.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 3:
                        option3.setBackgroundResource(R.drawable.green_background);
                        break;
                    case 4:
                        option4.setBackgroundResource(R.drawable.green_background);
                        break;
                }
            }

            return v;
        }
    }
}
