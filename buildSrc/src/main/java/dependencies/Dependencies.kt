package dependencies

object Dependencies{

    val kotlin_standard_library = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val core = "androidx.core:core-ktx:${Versions.core}"
    val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val viewmodel_lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    val livedata_lifecycle = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_convertor = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val fragmentx = "androidx.fragment:fragment-ktx:${Versions.fragmentx}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val room = "androidx.room:room-runtime:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"


}