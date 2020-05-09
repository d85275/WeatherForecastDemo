package ch.protonmail.android.protonmailtest.repo

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class WeatherData() : Parcelable {
    @PrimaryKey
    @SerializedName("day")
    lateinit var day: String

    @ColumnInfo(name = "description")
    @SerializedName("description")
    lateinit var des: String

    @ColumnInfo(name = "sunrise")
    @SerializedName("sunrise")
    var sunrise: Int = 0

    @ColumnInfo(name = "sunset")
    @SerializedName("sunset")
    var sunset: Int = 0

    @ColumnInfo(name = "chance_rain")
    @SerializedName("chance_rain")
    var chanceRain: Float = 0f

    @ColumnInfo(name = "high")
    @SerializedName("high")
    var high: Int = 0

    @ColumnInfo(name = "low")
    @SerializedName("low")
    var low: Int = 0

    @ColumnInfo(name = "image")
    @SerializedName("image")
    lateinit var imageUrl: String

    constructor(parcel: Parcel) : this() {
        day = parcel.readString()
        des = parcel.readString()
        sunrise = parcel.readInt()
        sunset = parcel.readInt()
        chanceRain = parcel.readFloat()
        high = parcel.readInt()
        low = parcel.readInt()
        imageUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeString(des)
        parcel.writeInt(sunrise)
        parcel.writeInt(sunset)
        parcel.writeFloat(chanceRain)
        parcel.writeInt(high)
        parcel.writeInt(low)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherData> {
        override fun createFromParcel(parcel: Parcel): WeatherData {
            return WeatherData(parcel)
        }

        override fun newArray(size: Int): Array<WeatherData?> {
            return arrayOfNulls(size)
        }
    }

}