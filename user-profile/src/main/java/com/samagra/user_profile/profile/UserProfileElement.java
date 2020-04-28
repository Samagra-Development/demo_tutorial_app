package com.samagra.user_profile.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.samagra.user_profile.InvalidConfigurationException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * An object used to represent a single profile element on the {@link ProfileActivity} screen.
 * These objects are passed in a 'View Holder' which helps this object to render on screen. The
 * kind of view holder depends on the {@link ProfileElementContentType} given to this object.
 * This object <b>must</b> implement either {@link Serializable} or {@link Parcelable}.
 * Since there are no complex types within the class, {@link Serializable} is much easier to implement,
 * however {@link Parcelable} gives much better performance.
 *
 * @author Pranav Sharma
 * @see com.samagra.user_profile.profile.ProfileElementViewHolders
 * @see {https://developer.android.com/reference/android/os/Parcel.html}
 */
public class UserProfileElement implements Parcelable {

    private String base64Icon;
    private String title;
    private String content;
    private boolean isEditable;
    private int section;
    private String value;
    private ProfileElementContentType profileElementContentType;
    private ArrayList<String> spinner_extras = new ArrayList<>();

    public UserProfileElement(@NonNull String base64Icon, @NonNull String title, @NonNull String content, boolean isEditable, int section,
                              @NonNull ProfileElementContentType profileElementContentType,
                              @Nullable ArrayList<String> spinner_extras,
                              @Nullable String value) {
        if (profileElementContentType == ProfileElementContentType.SPINNER && (spinner_extras == null || spinner_extras.size() == 0))
            throw new InvalidConfigurationException(UserProfileElement.class);
        this.base64Icon = base64Icon;
        this.title = title;
        this.content = content;
        this.isEditable = isEditable;
        this.section = section;
        this.profileElementContentType = profileElementContentType;
        this.spinner_extras = spinner_extras;
        this.value = value;
    }

    private UserProfileElement(Parcel parcel) {
        this.base64Icon = parcel.readString();
        this.title = parcel.readString();
        this.content = parcel.readString();
        this.isEditable = parcel.readInt() == 1;
        this.section = parcel.readInt();
        this.profileElementContentType = ProfileElementContentType.valueOf(parcel.readString());
        parcel.readStringList(this.spinner_extras);
    }

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public int getSection() {
        return section;
    }

    public ArrayList<String> getSpinner_extras() {
        return spinner_extras;
    }


    public Bitmap decodeBase64ToGetBitmap(String base64Icon) {
        byte[] decodedString = Base64.decode(base64Icon,  Base64.DEFAULT | Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public ProfileElementContentType getProfileElementContentType() {
        return profileElementContentType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.base64Icon);
        parcel.writeString(this.title);
        parcel.writeString(this.content);
        parcel.writeInt(this.isEditable ? 1 : 0);
        parcel.writeInt(this.section);
        parcel.writeString(this.profileElementContentType.name());
        parcel.writeStringList(this.spinner_extras);
    }

    public static final Creator CREATOR = new Creator<UserProfileElement>() {

        @Override
        public UserProfileElement createFromParcel(Parcel parcel) {
            return new UserProfileElement(parcel);
        }

        @Override
        public UserProfileElement[] newArray(int size) {
            return new UserProfileElement[size];
        }
    };

    public String getBase64Icon() {
        return base64Icon;
    }

    public String getValue() {
        return value;
    }

    public enum ProfileElementContentType {
        TEXT,
        DATE,
        PHONE_NUMBER,
        SPINNER;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nbase64 " + base64Icon + "\n" +
                "title " + title + "\n" +
                "content " + content + "\n" +
                "isEditable " + isEditable + "\n" +
                "section " + section + "\n" +
                "profileElementContentType " + profileElementContentType + "\n" +
                "spinner_extras " + spinner_extras;
    }
}
