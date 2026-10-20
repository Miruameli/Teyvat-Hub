# ProGuard rules for the release build.
#
# The build script points ProGuard at this file for the release variant, so
# it must be part of the repository. Without it, assembleRelease fails before
# it can run.
#
# The rules below keep the parts of the app that are looked up reflectively or
# by name at runtime. Everything else is safe to shrink and obfuscate.

# Keep the attributes the runtime and the tooling rely on.
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Hilt and Dagger generate code that references the annotated types directly.
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn javax.annotation.**

# Gson reads and writes the domain models reflectively, matching the fields
# of the bundled JSON assets by name. Obfuscating those fields would break
# the parsing at runtime while the build still succeeds, so the models and
# their field names are kept.
-keep class com.teyvat.hub.domain.character.** { *; }
-keep class com.teyvat.hub.domain.equipment.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn sun.misc.**

# The remote layer parses the Enka asset descriptors.
-keep class com.teyvat.hub.data.remote.** { *; }
-keepclassmembers class com.teyvat.hub.data.remote.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# Room generates the implementation classes and looks them up by name.
-keep class com.teyvat.hub.data.local.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.paging.**

# The domain enums are persisted as their string keys, so the enum constants
# and their names have to survive shrinking.
-keepclassmembers enum com.teyvat.hub.domain.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public static ** entries;
}
-keepnames enum com.teyvat.hub.domain.**

# Compose and the Android framework keep their own rules through the
# consumer rules shipped with the libraries.

# Keep the line numbers so a crash report stays readable, but hide the source
# file name.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
