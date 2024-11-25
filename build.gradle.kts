// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
//    id 'com.android.application' version '7.4.1' apply false
//    id 'com.android.library' version '7.4.1' apply false
}

buildscript{
    dependencies {
        classpath ("com.google.gms:google-services:4.4.2")
    }
}