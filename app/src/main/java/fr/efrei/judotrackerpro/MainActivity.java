package fr.efrei.judotrackerpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.efrei.judotrackerpro.adapters.CompetAdapter;
import fr.efrei.judotrackerpro.back.bdd.LocalDatabase;
import fr.efrei.judotrackerpro.back.entities.Categorie;
import fr.efrei.judotrackerpro.back.entities.Competition;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton floatingActionButton;

    private RecyclerView recycler;

    private LocalDatabase bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        floatingActionButton = findViewById(R.id.addCateButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, CompetitionActivity.class);
                Intent intent = new Intent(MainActivity.this, EditCompetActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        recycler = findViewById(R.id.compet_recycler);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        recycler.setLayoutManager(llm);

        List<String> list = new ArrayList<>();
        list.add("Compet 1");
        list.add("Compet 2");
        list.add("Compet 3");

        bdd = LocalDatabase.getInstance(getApplicationContext());

        List<Competition> compets = bdd.getAllCompetitions();
        if(!compets.isEmpty()){
            CompetAdapter adapter = new CompetAdapter(compets);
            recycler.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "Aucune coméptition n'existe pour le moement.", Toast.LENGTH_LONG);
        }
        //tempInsertCates();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home :
                BottomNavDrawerFragment b = new BottomNavDrawerFragment();
                b.show(getSupportFragmentManager(), b.getTag());

        }

        return super.onOptionsItemSelected(item);
    }

    private void tempInsertCates(){
        List<Categorie> cates = new ArrayList<>();
        cates.add(new Categorie(1,"femme","-48"));
        cates.add(new Categorie(2,"femme","-52"));
        cates.add(new Categorie(3,"femme","-57"));
        cates.add(new Categorie(4,"femme","-63"));
        cates.add(new Categorie(5,"femme","-70"));
        cates.add(new Categorie(6,"femme","-78"));
        cates.add(new Categorie(7,"femme","+78"));

        cates.add(new Categorie(8,"homme","-60"));
        cates.add(new Categorie(9,"homme","-66"));
        cates.add(new Categorie(10,"homme","-73"));
        cates.add(new Categorie(11,"homme","-81"));
        cates.add(new Categorie(12,"homme","-90"));
        cates.add(new Categorie(13,"homme","-100"));
        cates.add(new Categorie(14,"homme","+100"));

        bdd.insertCategorieAll(cates);
    }

}
