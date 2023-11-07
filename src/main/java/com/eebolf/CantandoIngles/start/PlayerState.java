package com.eebolf.CantandoIngles.start;


import android.os.Parcel;
import android.os.Parcelable;

public class PlayerState implements Parcelable {
  public boolean stopped    = false;
  public boolean paused     = false;
  public boolean synched    = true;
  // only set when stopped, when synched and unsynched and when set to zero when created.
  // note position isn't set when paused or every time the display changes.
  public int     position   = 0;

  public PlayerState () {}
  public static final Parcelable.Creator<PlayerState> CREATOR
    = new Parcelable.Creator<PlayerState>() {
    public PlayerState createFromParcel(Parcel in) {
      return new PlayerState(in);
    }

    public PlayerState[] newArray(int size) {
      return new PlayerState[size];
    }
  };

  public PlayerState (Parcel in) {
    synched  = in.readByte() != 0;
    stopped  = in.readByte() != 0;
    paused   = in.readByte() != 0;
    position = in.readInt();
  }

  @Override
  public int describeContents(){
    return 0;
  }

  @Override
  public void writeToParcel (Parcel dest, int flags) {
    dest.writeByte((byte) (synched ? 1 : 0));
    dest.writeByte((byte) (stopped ? 1 : 0));
    dest.writeByte((byte) (paused ? 1 : 0));
    dest.writeInt(position);
  }

  public void setToStop (Integer position) {
    this.position = position;
    this.stopped = true;
    this.paused  = false;
    this.synched = false;
  }

  public void setToPaused () {
    if (!this.stopped) {
      this.paused  = true;
      this.synched = false;
    }
    // pausing if this.stopped has no effect.
  }

  public void setToPlay () {
    if (stopped || paused) {
      this.stopped = false;
      this.paused  = false;
      this.synched = true;
    }
  }

  public void setToSynched () {
    if (!paused || !stopped)
      synched = true;
  }

  public void unSynch () {
    if (!paused || !stopped)
      synched = false;
  }

  public boolean playing () {
    if (!paused && !stopped)
      return true;
    else
      return false;
  }

}
