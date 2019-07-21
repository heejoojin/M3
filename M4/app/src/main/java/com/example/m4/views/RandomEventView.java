package com.example.m4.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.m4.R;
import com.example.m4.repository.Repository;
import com.example.m4.viewmodels.RandomEventViewModel;

import java.util.Random;

/**
 * View class that shows random events occurring when traveling between regions
 */

@SuppressWarnings("CyclicClassDependency")
public class RandomEventView extends AppCompatActivity implements View.OnClickListener {

    private String base = "After an eventful trip," +
            "\nyou couldn't arrive at your destination :(\n";
    private String newMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomevent);

        TextView travelMessage = findViewById(R.id.travel_message);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);

        RandomEventViewModel viewModel = ViewModelProviders.of(this).get(RandomEventViewModel.class);
        int randomElement = viewModel.getRandomElement();
        boolean isitaddingCredits = viewModel.getAddCredits();

        int randomChance = viewModel.getRandomChance();

        // when randomElement == 0
        // case 1 no credits being changed
        newMessage = base + "\n\nTry again";
        travelMessage.setText(newMessage);

        if (randomChance == 0) {
            // changing credits
            if (randomElement != 0) {
                // credits being changed

                if (isitaddingCredits) {

                    // case 2 found credits
                    newMessage = base + "but you just found " + randomElement + " credits! Congrats!" +
                            "\n\nTry again to travel to your destination";
                    travelMessage.setText(newMessage);
                    Repository.playerClass.setCredits(Repository.playerClass.getCredits() + randomElement);
                } else {

                    // case 3 lost credits
                    if (randomElement < Repository.playerClass.getCredits()) {
                        newMessage = base + "and you just lost " + randomElement + " credits :(" +
                                "\n\nTry again to travel to your destination";
                        travelMessage.setText(newMessage);

                        Repository.playerClass.setCredits(Repository.playerClass.getCredits() - randomElement);
                    }
                }
            }
        } else if (randomChance == 1) {

            // case 4 get a new item
            String item = viewModel.findRandomItem();

            newMessage = base + "but you just found a new " + item + " item!\nCongrats!" +
                    "\n\nTry again to travel to your destination";
            travelMessage.setText(newMessage);
        } else if (randomChance == 2) {

            // case 5 lose an item that you already have
            String item = viewModel.loseRandomItem();

            if (item != null) {
                newMessage = base + "and you just lost one of your " + item + " items :(" +
                        "\n\nTry again to travel to your destination";
            }
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_button) {
            startActivity(new Intent(this, UniverseView.class));
        }
    }
}
