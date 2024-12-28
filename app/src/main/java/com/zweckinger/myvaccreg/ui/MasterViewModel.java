package com.zweckinger.myvaccreg.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import java.io.Serializable;

public abstract class MasterViewModel extends AndroidViewModel {
    public MasterViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void insert(Serializable content);

    public abstract void update(Serializable content);

    public abstract void delete(Serializable content);
}
