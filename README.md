# GMIProspect

**GMIProspect** is an Android app for students who are thinking about studying at the **German-Malaysian Institute (GMI)**. It lists every GMI programme in one place and tells students straight away which ones they qualify for, based on their own exam results. It was built as the mini project for **SPG 0562**.

## What it does
- **Browse programmes:** see all 13 Diploma programmes plus the **GAPP** (German A-Level Preparatory Programme) and **GUFP** (GMI-UniKL Foundation Programme). You can sort them with tabs, search them, and open each one for its overview, department, duration, intake, entry requirements and career options.
- **Eligibility Checker:** enter your **SPM/SVM**, **O-Level/IGCSE** or **SKM Level 3** results. A built-in rule engine checks them against every programme's entry requirements. You see the programmes you qualify for first, along with the requirements you still haven't met for the others.
- **Apply Now:** jump straight to GMI's online application portal (https://gmi.vialing.com/oa/login).
- **Contact & About:** a map, phone, fax, email, the website and an enquiry form.
- **Share:** send a programme's details to friends or family.

## Built with
- Java 17 with XML layouts
- AndroidX AppCompat, Material Components, RecyclerView, CardView and ConstraintLayout
- minSdk 26, targetSdk 34
- Programme data loaded from a local `courses.json` file (`app/src/main/assets/courses.json`), so it works offline

## Open & run
1. Android Studio > File > Open > select this `GMIProspect` folder.
2. Wait for Gradle sync (downloads AGP 8.7.3 / Gradle 8.11.1 on first run).
3. Create an emulator (Device Manager > Pixel 7, API 34) and press Run.
