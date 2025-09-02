-keepclassmembers class * {
    <init>();
}

-keep class kolomyichuk.runly.domain.run.model.** {
    <fields>;
    <init>();
}

-keep class kolomyichuk.runly.data.remote.firestore.model.** {
    *;
}

-keepclassmembers class kolomyichuk.runly.data.remote.firestore.model.** {
    *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valuesOf(java.lang.String);
}
