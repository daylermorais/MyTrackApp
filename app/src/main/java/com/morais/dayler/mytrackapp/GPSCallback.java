package com.morais.dayler.mytrackapp;

import android.location.Location;

public interface GPSCallback
{
    public abstract void onGPSUpdate(Location location);
}