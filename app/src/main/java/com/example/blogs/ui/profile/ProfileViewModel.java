package com.example.blogs.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogs.models.AppUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends ViewModel {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
   private final MutableLiveData<AppUser> mutableAppUser = new MutableLiveData<>();


     LiveData<AppUser> getUserData() {

        firestore.collection("ultras").document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            AppUser appUser = documentSnapshot.toObject(AppUser.class);
                            mutableAppUser.postValue(appUser);

                        }
                    }

                });
        return mutableAppUser;
    }
}
