package fr.efrei.judotrackerpro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import fr.efrei.judotrackerpro.adapters.CateAdapter;
import fr.efrei.judotrackerpro.back.bdd.LocalDatabase;
import fr.efrei.judotrackerpro.back.entities.Categorie;
import fr.efrei.judotrackerpro.back.entities.Competition;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditCompetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView name;

    private EditText edit_name;
    private EditText edit_date;
    private Switch edit_sex;
    private Spinner edit_cate;

    private Button validate_btn;

    private DatePickerDialog datePickerDialog;

    private LocalDatabase bdd;

    private Competition competition;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_compet);

        bdd = LocalDatabase.getInstance(getApplicationContext());


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            Integer competID = bundle.getInt("ID");
            competition = bdd.getCompetition(competID);
        }


        name = findViewById(R.id.compet_name);
        edit_name = findViewById(R.id.edit_name);
        edit_date = findViewById(R.id.edit_date);
        edit_sex = findViewById(R.id.edit_sex);
        edit_cate = findViewById(R.id.edit_cate);

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        datePickerDialog = new DatePickerDialog(EditCompetActivity.this, this, 2019, 4, 1);

        if (competition == null) {
            name.setText("Nouvelle Compétition");
        }
        else{
            name.setText(competition.getNom_competition());
            edit_name.setText(competition.getNom_competition());
            Categorie cate = bdd.getCategorie(competition.getId_categorie());
            edit_sex.setChecked(cate.getSexe().equals("femme"));
            int i = bdd.getAllCategories().indexOf(cate);
            edit_cate.setSelection(i);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
            edit_date.setText(dateFormat.format(competition.getDate_competition()));
        }

        edit_sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    List<Categorie> cates = bdd.getCategorieBySexe("Femme");

                    CateAdapter adapter = new CateAdapter(EditCompetActivity.this, cates);
                    edit_cate.setAdapter(adapter);
                }
                else{
                    List<Categorie> cates = bdd.getCategorieBySexe("Homme");

                    CateAdapter adapter = new CateAdapter(EditCompetActivity.this, cates);
                    edit_cate.setAdapter(adapter);
                }
            }
        });

        validate_btn = findViewById(R.id.validate_btn);
        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_name.getText().toString();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);

                Categorie cate = (Categorie) edit_cate.getSelectedItem();

                if(name.isEmpty() || cate==null || edit_date.getText().toString().isEmpty()){
                    Toast.makeText(EditCompetActivity.this,"Veuillez rentrer tous les champs.", Toast.LENGTH_LONG).show();
                }
                else{

                    Date date = new Date();
                    try {
                        date = dateFormatter.parse(edit_date.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long competID;

                    if(competition==null){
                        Competition compet = new Competition(name, cate, date);
                        competID = bdd.insertCompetition(compet);
                    }else{
                         competition.setNom_competition(name);
                         competition.setDate_competition(date);
                         competition.setCategorie(cate);

                         competID = competition.getId_competition();

                         bdd.updateCompetition(competition);
                    }


                    Intent i = new Intent(EditCompetActivity.this,CompetitionActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("ID",(int)competID);
                    i.putExtras(b);
                    finish();
                    startActivity(i);
                }
            }
        });

        edit_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        edit_date.setText(datePicker.getDayOfMonth()+"-"+datePicker.getMonth()+"-"+datePicker.getYear());

    }
}
