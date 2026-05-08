package com.comic.usermanagement.interfaces;

import com.comic.usermanagement.models.Users;

public interface OnUserClickListener {
    void onUserEdit(Users users);
    void onUserDelete(Users users);
}
