package com.madhavsteel.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.fragment.app.Fragment;

import com.madhavsteel.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public RealmController(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController with(Context context) {

        if (instance == null) {
            instance = new RealmController(context);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.delete(User.class);
        realm.commitTransaction();
    }

    //find all objects in the User.class
    public RealmResults<User> getUsers() {

        return realm.where(User.class).findAll();
    }

    //query a single item with the given id
    public User getUser(String id) {

        return realm.where(User.class).equalTo("userId", id).findFirst();
    }

    public User getUser() {
        return realm.where(User.class).findFirst();
    }

    //check if User.class is empty
    //query example
    public RealmResults<User> queryedUsers() {

        return realm.where(User.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
