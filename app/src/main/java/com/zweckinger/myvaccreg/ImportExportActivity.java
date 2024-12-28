package com.zweckinger.myvaccreg;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zweckinger.myvaccreg.model.Converters;
import com.zweckinger.myvaccreg.model.IllnessWithVaccines;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.illness.IllnessDao;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccination.VaccinationDao;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.model.vaccine.VaccineDao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ImportExportActivity extends AppCompatActivity {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    private Button importExportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_import);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String importExportMode = i.getStringExtra("mode");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exportImport_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner s = findViewById(R.id.form_exportImport_spinner);
        s.setAdapter(adapter);
        s.setSelection(0);

        TextView importExportTextView = findViewById(R.id.form_exportImport_textView);
        importExportButton = findViewById(R.id.form_ok_button);
        if (importExportMode.equals("import")) {
            importExportTextView.setText(R.string.select_import_type);
            importExportButton.setText(R.string.navigation_vaccinations_import);
            setActionBarTitle(R.string.navigation_vaccinations_import);
        } else {
            importExportTextView.setText(R.string.select_export_type);
            importExportButton.setText(R.string.navigation_vaccinations_export);
            setActionBarTitle(R.string.navigation_vaccinations_export);
        }

        ActivityResultLauncher<Intent> importActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();

                            VeccregDatabase.databaseWriteExecutor.execute(() -> {
                                try (InputStream inputStream = this.getContentResolver().openInputStream(uri);
                                     BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                                     InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
                                     BufferedReader br = new BufferedReader(inputStreamReader)) {

                                    //Skip sample:
                                    br.readLine();

                                    LinkedList<String> lines = new LinkedList<>();
                                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                                        lines.add(line);
                                    }

                                    bufferedInputStream.close();
                                    inputStream.close();

                                    csvToDb(s.getSelectedItemId(), lines);

                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Import successful. (" + uri.getPath() + ")", Toast.LENGTH_SHORT).show());
                                } catch (IOException e) {
                                    runOnUiThread(() -> Utils.showError("Error while exporting"));
                                    e.printStackTrace();
                                }
                            });
                        }
                    } else {
                        Utils.showError(getString(R.string.file_chooser_error));
                    }
                });

        ActivityResultLauncher<Intent> exportActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();

                            VeccregDatabase.databaseWriteExecutor.execute(() -> {
                                try (OutputStream outputStream = this.getContentResolver().openOutputStream(uri);
                                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                                    String fileContent = dbToCsv(s.getSelectedItemId());
                                    bufferedOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
                                    bufferedOutputStream.flush();
                                    bufferedOutputStream.close();
                                    outputStream.close();
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Import successful. (" + uri.getPath() + ")", Toast.LENGTH_SHORT).show());
                                } catch (IOException e) {
                                    runOnUiThread(() -> Utils.showError("Error while exporting"));
                                    e.printStackTrace();
                                }
                            });
                        } else {
                           Utils.showError(getString(R.string.file_chooser_error));
                        }
                    }
                });


        importExportButton.setOnClickListener(v -> {
            if (getStoragePermission()) {
                if (importExportMode.equals("import")) {
                    Intent fileChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    fileChooser.setType("text/comma-separated-values");
                    fileChooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileChooser.addCategory(Intent.CATEGORY_OPENABLE);
                    fileChooser = Intent.createChooser(fileChooser, getString(R.string.choose_a_file_to_import));
                    importActivityResultLauncher.launch(fileChooser);
                } else {
                    String fileName;
                    if (s.getSelectedItemId() == 0) { //IllnessesVaccines
                        fileName = "IllnessesVaccines.csv";
                    } else { //Vaccinations
                        fileName = "Vaccinations.csv";
                    }
                    Intent fileCreator = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    fileCreator.addCategory(Intent.CATEGORY_OPENABLE);
                    fileCreator.setType("text/comma-separated-values");
                    fileCreator.putExtra(Intent.EXTRA_TITLE, fileName);
                    exportActivityResultLauncher.launch(fileCreator);
                }
            } else {
                importExportButton.post(() -> Utils.showError(getString(R.string.sttorage_permission_missing)));
            }
        });
    }

    private void csvToDb(long importType, LinkedList<String> lines) {
        if (importType == 0) { //IllnessesVaccines
            IllnessDao illnessDao = VeccregDatabase.instance.illnessDao();
            VaccineDao vaccineDao = VeccregDatabase.instance.vaccineDao();
            for (String line : lines) {
                String[] splitted = line.split(",");
                String vaccineName = splitted[0];
                if (illnessDao.getIllness(splitted[0]) == null) {
                    illnessDao.insert(new Illness(vaccineName));
                } else {
                    importExportButton.post(() -> Utils.showError("Illness " + splitted[0] + " already exists"));
                }
                List<Integer> basicImmus = new ArrayList<>();
                for (int i = 3; i < splitted.length; i++) {
                    basicImmus.add(Integer.parseInt(splitted[i]));
                }
                if (vaccineDao.getVaccine(splitted[1]) == null) {
                    Vaccine vaccine = new Vaccine(splitted[1],
                            illnessDao.getIllness(splitted[0]).getIllnessId(),
                            basicImmus, Integer.parseInt(splitted[2]));
                    vaccineDao.insert(vaccine);
                } else {
                    importExportButton.post(() -> Utils.showError("Vaccine " + splitted[1] + " already exists"));
                }
            }
        } else { //Vaccinations
            VaccinationDao vaccinationDao = VeccregDatabase.instance.vaccinationDao();
            VaccineDao vaccineDao = VeccregDatabase.instance.vaccineDao();
            for (String line : lines) {
                try {
                    String[] splitted = line.split(",");
                    String vaccineName = splitted[0];
                    int dose = Integer.parseInt(splitted[1]);
                    Calendar vaccinationDate = Calendar.getInstance();
                    vaccinationDate.setTime(dateFormatter.parse(splitted[2]));
                    Vaccine vaccine = vaccineDao.getVaccine(vaccineName);
                    if (vaccine != null) {
                       Vaccination vaccinationExistsAlready = vaccinationDao.getVaccination(vaccine.getVaccineId(),
                               dose, Converters.fromCalendar(vaccinationDate));
                       if(vaccinationExistsAlready == null) {
                           Vaccination v = new Vaccination(MainActivity.activeProfile.getProfileId(),
                                   vaccine.getVaccineId(), dose, vaccinationDate);
                           vaccinationDao.insert(v);
                       }
                    } else {
                        importExportButton.post(() -> Utils.showError("Vaccine " + splitted[0] + " is missing"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    importExportButton.post(() -> Utils.showError("Vaccination import error"));
                }
            }
        }
    }

    private String dbToCsv(long importType) {
        StringBuilder csvExport = new StringBuilder();
        VeccregDatabase db = VeccregDatabase.instance;
        if (importType == 0) { //IllnessesVaccines
            csvExport.append("Illness,VaccineName,BoosterVaccinationRepeating,BasicImmunisation[],\n");
            IllnessDao illnessDao = db.illnessDao();
            List<IllnessWithVaccines> illnessesWithVaccines = illnessDao.getIllnessesWithVaccines();
            for (IllnessWithVaccines illnessesWithVaccine : illnessesWithVaccines) {
                for (int i = 0; i < illnessesWithVaccine.vaccines.size(); i++) {
                    Vaccine vaccine = illnessesWithVaccine.vaccines.get(i);
                    csvExport.append(illnessesWithVaccine.illness.getIllnessName()).append(",");
                    csvExport.append(vaccine.getVaccineName()).append(",").
                            append(vaccine.getBoosterVaccinationRepeating()).append(",");
                    for (int i1 = 0; i1 < vaccine.getBasicImmunizations().size(); i1++) {
                        csvExport.append(vaccine.getBasicImmunizations().get(i1));
                        //Linebreak not for last row
                        if (i < vaccine.getBasicImmunizations().size() - 1) {
                            csvExport.append(",");
                        }
                    }
                    //Linebreak not for last row
                    if (i < illnessesWithVaccines.size() - 1) {
                        csvExport.append("\n");
                    }
                }
            }
        } else { //Vaccinations
            csvExport.append("VaccineName,Dose,Date,\n");
            VaccinationDao vaccinationDao = db.vaccinationDao();
            VaccineDao vaccineDao = db.vaccineDao();
            List<Vaccination> vaccinations = vaccinationDao.getAllVaccinations();
            for (int i = 0; i < vaccinations.size(); i++) {
                String vaccineName = vaccineDao.getVaccine(vaccinations.get(i).getVaccineId()).getVaccineName();
                csvExport.append(vaccineName).append(",");
                csvExport.append(vaccinations.get(i).getDose()).append(",");
                csvExport.append(dateFormatter.format(vaccinations.get(i).getDate().getTime())).append(",");
                //Linebreak not for last row
                if (i < vaccinations.size() - 1) {
                    csvExport.append("\n");
                }
            }
        }
        System.out.println(csvExport.toString());
        return csvExport.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarTitle(int id) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(id);
    }

    public boolean getStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }
}