
name: Build SSPlayer APK

on:
  workflow_dispatch:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Baixar código
        uses: actions/checkout@v4

      - name: Configurar Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'

      - name: Configurar Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.13.2'

      - name: Preparar projeto
        run: |
          mkdir -p project
          ZIP_FILE=$(find . -maxdepth 2 -name "SSPlayer-Android-Base*.zip" | head -n 1)

          if [ -z "$ZIP_FILE" ]; then
            echo "ZIP do SSPlayer não encontrado"
            exit 1
          fi

          unzip -q "$ZIP_FILE" -d project

          SETTINGS=$(find project -name settings.gradle.kts | head -n 1)
          PROJECT_DIR=$(dirname "$SETTINGS")
          APP_GRADLE="$PROJECT_DIR/app/build.gradle.kts"

          if [ ! -f "$APP_GRADLE" ]; then
            echo "Arquivo app/build.gradle.kts não encontrado"
            exit 1
          fi

          cat > "$APP_GRADLE" <<'EOF'
          plugins {
              id("com.android.application")
              id("org.jetbrains.kotlin.android")
          }

          android {
              namespace = "com.ssplayer.app"
              compileSdk = 36

              defaultConfig {
                  applicationId = "com.ssplayer.app"
                  minSdk = 26
                  targetSdk = 35
                  versionCode = 1
                  versionName = "0.1.0"
              }

              compileOptions {
                  sourceCompatibility = JavaVersion.VERSION_17
                  targetCompatibility = JavaVersion.VERSION_17
              }

              kotlinOptions {
                  jvmTarget = "17"
              }
          }

          dependencies {
              implementation("androidx.core:core-ktx:1.17.0")
              implementation("androidx.activity:activity-ktx:1.11.0")
              implementation("com.google.guava:guava:33.4.8-android")
              implementation("androidx.appcompat:appcompat:1.7.1")
              implementation("androidx.documentfile:documentfile:1.1.0")
              implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
              implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
              implementation("androidx.media3:media3-exoplayer:1.11.1")
              implementation("androidx.media3:media3-session:1.11.1")
          }
          EOF

      - name: Compilar APK
        run: |
          PROJECT_DIR=$(dirname "$(find project -name settings.gradle.kts | head -n 1)")
          cd "$PROJECT_DIR"
          gradle assembleDebug

      - name: Disponibilizar APK
        uses: actions/upload-artifact@v4
        with:
          name: SSPlayer-APK
          path: project/**/app/build/outputs/apk/debug/*.apk
          if-no-files-found: error
