
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

      - name: Encontrar e extrair projeto
        run: |
          ZIP_FILE=$(find . -maxdepth 1 -type f -iname '*.zip' | head -n 1)
          if [ -z "$ZIP_FILE" ]; then
            echo "Nenhum arquivo ZIP encontrado"
            exit 1
          fi
          mkdir project
          unzip -q "$ZIP_FILE" -d project
          GRADLE_FILE=$(find project -name settings.gradle.kts -print -quit)
          if [ -z "$GRADLE_FILE" ]; then
            echo "Projeto Gradle não encontrado"
            exit 1
          fi
          echo "PROJECT_DIR=$(dirname "$GRADLE_FILE")" >> "$GITHUB_ENV"

      - name: Configurar Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.13'

      - name: Compilar APK
        run: gradle -p "$PROJECT_DIR" assembleDebug

      - name: Disponibilizar APK
        uses: actions/upload-artifact@v4
        with:
          name: SSPlayer-APK
          path: project/**/app/build/outputs/apk/debug/*.apk
