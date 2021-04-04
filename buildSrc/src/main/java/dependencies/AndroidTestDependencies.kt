package dependencies

object AndroidTestDependencies{

    val ext_junit = "androidx.test.ext:junit:${Versions.ext_junit}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    val hilt = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    val truth = "com.google.truth:truth:${Versions.truth}"
    val arch_test = "androidx.arch.core:core-testing:${Versions.arch_test}"
    val runner_test = "androidx.test:runner:${Versions.runner_test}"
    val rules_test = "androidx.test:rules:${Versions.rules_test}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_andorid_test}"

}