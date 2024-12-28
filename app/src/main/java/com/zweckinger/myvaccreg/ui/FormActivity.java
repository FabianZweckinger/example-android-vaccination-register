package com.zweckinger.myvaccreg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.zweckinger.myvaccreg.R;
import java.io.Serializable;
import java.util.Objects;

public abstract class FormActivity extends AppCompatActivity {

    /** Set to "create", "edit" via intent and could changed to "delete" the object gets deleted */
    private final int activityFormId; //Set in constructor
    private final int stringCreateId; //Set in constructor
    private final int stringEditId; //Set in constructor
    protected Button createEditButton;
    protected String mode;
    protected boolean noBack, noDelete;
    protected Serializable editContent;
    /** Used to show error only when something was messed up */
    protected boolean wasValidBefore;

    public FormActivity(int activityFormId, int stringCreateId, int stringEditId) {
        this.activityFormId = activityFormId;
        this.stringCreateId = stringCreateId;
        this.stringEditId = stringEditId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        noBack = intent.getBooleanExtra("noBack", false);
        noDelete = intent.getBooleanExtra("noDelete", false);

        //Create view:
        setContentView(activityFormId);

        //Back action bar button:
        if(!noBack) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        //Get buttons:
        createEditButton = findViewById(R.id.form_ok_button);
        Button deleteButton = findViewById(R.id.form_delete_button);

        if(mode.equals("create")) {
            setActionBarTitle(stringCreateId);

            //Hide delete button:
            deleteButton.setVisibility(View.INVISIBLE);

            //Relabel button from edit to create:
            createEditButton.setText(stringCreateId);

            //Disable button by default, enable with finishForm:
            createEditButton.setEnabled(false);

            createEditButton.setOnClickListener(v -> {
                if(checkForm(true)) {
                    finishForm();
                }
            });

        } else { //Edit
            setActionBarTitle(stringEditId);

            //Load edit entry:
            editContent = intent.getSerializableExtra("content");

            if(!noDelete){
            deleteButton.setOnClickListener(v -> {
                mode = "delete";
                finishForm();
            });
            }else{
                deleteButton.setEnabled(false);
                deleteButton.setBackgroundColor(getColor(R.color.dangerDisabled));
            }

            //Edit button:
            createEditButton.setOnClickListener(v -> {
                if(checkForm(true)) {
                    finishForm();
                }
            });
        }
    }

    private void setActionBarTitle(int id) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(id);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get edit entry
        if(!mode.equals("create")) {
            fillFormEditMode();
        }
    }

    @Override
    public void onBackPressed() {
        if(!noBack) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create button in action bar:
        getMenuInflater().inflate(R.menu.generic_actionbar_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.generic_actionbar_ok_button) {
            if(checkForm(true))
                finishForm();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void finishForm() {
        Intent data = new Intent();
        data.putExtra("mode", mode);
        data.putExtra("content", makeContentFromForm());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public abstract boolean checkForm(boolean showError);

    public abstract void fillFormEditMode();

    protected abstract Serializable makeContentFromForm();
}

