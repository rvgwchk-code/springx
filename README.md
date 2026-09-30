# Springx

A document search and research workspace with a Spring Boot API and a React + Vite frontend.

## Structure

- `services/api` – Java Spring Boot API
- `services/web` – React + TypeScript + Fluent UI client

## Local development
UI:
SpringX 
https://github.com/rvgwchk-code/springx/blob/main/springx.png

Backend:

```bash
cd services/api
./mvnw spring-boot:run
```

Frontend:

```bash
npm --prefix services/web install
npm --prefix services/web run dev
```

## Production checks

```bash
cd services/api
./mvnw -q test
npm --prefix services/web run build
```
