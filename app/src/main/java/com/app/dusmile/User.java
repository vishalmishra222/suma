package com.app.dusmile;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String tokenId;
    public String progress;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String tokenId,String progress) {
        this.username = username;
        this.email = email;
        this.tokenId = tokenId;
        this.progress = progress;
    }

}
// [END blog_user_class]
