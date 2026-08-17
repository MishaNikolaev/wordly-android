plugins {
    alias(libs.plugins.wordlyAndroidLibrary)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.dagger)
    implementation(libs.retrofit)
    ksp(libs.dagger.compiler)

    implementation(projects.core.network)
}