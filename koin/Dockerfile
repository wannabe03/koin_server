FROM ghcr.io/cirruslabs/flutter:stable

# 기본 설정 + Web 활성화 + Android 라이선스 수락(빌드용)
RUN flutter config --no-analytics \
 && flutter config --enable-web \
 && yes "y" | sdkmanager --licenses

WORKDIR /app

# 의존성 프리페치(처음 빌드 속도 개선)
COPY pubspec.* ./
RUN flutter pub get || true

# 소스 복사
COPY . .

# 컨테이너 기본 실행: 웹서버 디바이스로 실행 (핫리로드 가능)
EXPOSE 5173
CMD ["bash", "-lc", "flutter pub get && flutter run -d web-server --web-hostname=0.0.0.0 --web-port=5173"]
