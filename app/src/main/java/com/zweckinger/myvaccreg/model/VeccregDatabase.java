package com.zweckinger.myvaccreg.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.illness.IllnessDao;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.model.profile.ProfileDao;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccination.VaccinationDao;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.model.vaccine.VaccineDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = { Vaccination.class, Illness.class, Profile.class, Vaccine.class}, version = 33, exportSchema = false)
public abstract class VeccregDatabase extends RoomDatabase {

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);

    //Marking the instance as volatile to ensure atomic access to the variable
    public static volatile VeccregDatabase instance;

    //DAOs:
    public abstract VaccineDao vaccineDao();

    public abstract ProfileDao profileDao();

    public abstract IllnessDao illnessDao();

    public abstract VaccinationDao vaccinationDao();


    /** Returns the database singleton. It'll create the database the first time it's accessed.*/
    public static VeccregDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (VeccregDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            VeccregDatabase.class, "veccregdatabase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

                ProfileDao profileDao = instance.profileDao();
                IllnessDao illnessDao = instance.illnessDao();
                VaccineDao vaccineDao = instance.vaccineDao();
                VaccinationDao vaccinationDao = instance.vaccinationDao();


                Profile profile = new Profile("\uD83C\uDF46");
                profileDao.insert(profile);

                Profile profile2 = new Profile("Mohammed \uD83D\uDCA5");
                profileDao.insert(profile2);

                Profile profile3 = new Profile("Fabian");
                profile.setActive(true);
                profileDao.insert(profile3);

                Profile profile4 = new Profile("費邊 \uD83C\uDDE8\uD83C\uDDF3\uD83C\uDDE8\uD83C\uDDF3\uD83C\uDDE8\uD83C\uDDF3");
                profileDao.insert(profile4);

                Profile profile5 = new Profile("Marco \uD83E\uDD70");
                profileDao.insert(profile5);


                Illness covid = new Illness("Covid 19");
                illnessDao.insert(covid);

                Illness mmr = new Illness("Masern Mumps Röteln");
                illnessDao.insert(mmr);

                Illness pest = new Illness("Pest \uD83D\uDC80");
                illnessDao.insert(pest);

                Illness tetanus = new Illness("Tetanus");
                illnessDao.insert(tetanus);

                Illness diphtherie = new Illness("Diphtherie");
                illnessDao.insert(diphtherie);


                ArrayList<Integer> pfizerSchema = new ArrayList<>();
                pfizerSchema.add(1);
                pfizerSchema.add(9);
                Vaccine pfizer = new Vaccine("Pfizer", illnessDao.getIllness("Covid 19").getIllnessId(),
                        pfizerSchema, 12);
                vaccineDao.insert(pfizer);

                ArrayList<Integer> janssenSchema = new ArrayList<>();
                janssenSchema.add(3);
                Vaccine janssen = new Vaccine("Janssen", illnessDao.getIllness("Covid 19").getIllnessId(),
                        janssenSchema, 12);
                vaccineDao.insert(janssen);

                ArrayList<Integer> sputnikSchema = new ArrayList<>();
                sputnikSchema.add(1);
                sputnikSchema.add(2);
                sputnikSchema.add(3);
                sputnikSchema.add(5);
                Vaccine sputnik = new Vaccine("Sputnik-V", illnessDao.getIllness("Covid 19").getIllnessId(),
                        sputnikSchema, 18);
                vaccineDao.insert(sputnik);

                ArrayList<Integer> tdSchema = new ArrayList<>();
                tdSchema.add(1);
                tdSchema.add(2);
                tdSchema.add(3);
                tdSchema.add(4);
                tdSchema.add(5);
                Vaccine td = new Vaccine("Td-IMMUN", illnessDao.getIllness("Tetanus").getIllnessId(),
                        tdSchema, -1);
                vaccineDao.insert(td);

                Vaccine boostrix = new Vaccine("Boostrix", illnessDao.getIllness("Masern Mumps Röteln").getIllnessId(),
                        new ArrayList<>(), -1);
                vaccineDao.insert(boostrix);


                Vaccination v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Pfizer").getVaccineId(),
                        1, getCalendar(2021, 10, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Pfizer").getVaccineId(),
                        2, getCalendar(2021, 11, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Pfizer").getVaccineId(),
                        3, getCalendar(2022, 2, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Td-IMMUN").getVaccineId(),
                        1, getCalendar(2012, 1, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Td-IMMUN").getVaccineId(),
                        2, getCalendar(2013, 5, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Td-IMMUN").getVaccineId(),
                        3, getCalendar(2014, 10, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Td-IMMUN").getVaccineId(),
                        4, getCalendar(2015, 6, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Td-IMMUN").getVaccineId(),
                        5, getCalendar(2016, 12, 1));
                vaccinationDao.insert(v);

                v = new Vaccination(profileDao.getActiveProfile().getProfileId(),
                        vaccineDao.getVaccine("Boostrix").getVaccineId(),
                        1, getCalendar(2009, 11, 1));
                vaccinationDao.insert(v);
            });
        }
    };

    private static Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,0,0,0);
        return calendar;
    }
}