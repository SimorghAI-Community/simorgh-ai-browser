# Development Log

این سند تاریخچه پیاده‌سازی پروژه را به‌ترتیب فاز ثبت می‌کند.

## هدف این سند

`ARCHITECTURE.md` می‌گوید سیستم **باید چطور** باشد. `docs/roadmap.md` می‌گوید **به کجا** می‌رویم.
این سند می‌گوید **همین الان دقیقاً کجا هستیم** — چه چیزی واقعاً ساخته شده، نه چه چیزی برنامه‌ریزی شده.

هدف اصلی: وقتی کار توسعه بعد از یک وقفه ادامه پیدا می‌کند (چه توسط همان توسعه‌دهنده، چه یک عضو جدید تیم)، کافی است آخرین ورودی این فایل + `ARCHITECTURE.md` مطالعه شود تا مشخص شود:

1. چه چیزی تا الان ساخته شده.
2. چرا به همین شکل ساخته شده (اگر با ARCHITECTURE.md فرقی داشته).
3. مرحله بعدی دقیقاً باید از کجا شروع شود.

## قوانین استفاده از این فایل

1. **این فایل فقط append می‌شود، هرگز بازنویسی یا حذف نمی‌شود.** حتی اگر یک فاز بعداً اشتباه تشخیص داده شود، یک ورودی جدید اضافه کنید که آن را اصلاح می‌کند؛ ورودی قدیمی را پاک نکنید. تاریخچه تصمیم‌ها باید حفظ بماند.
2. **هر فاز باید به اندازه یک دوره کاری کوتاه و قابل‌مدیریت باشد** — نه یک نسخه کامل (v0.1)، بلکه یک تکه کوچک و قابل تایید از آن (مثلاً «اسکلت Gradle»، نه «کل Browser Core»).
3. **در شروع هر دوره کاری جدید، فقط آخرین ورودی (یا چند ورودی اخیر مرتبط) مرور شود**، نه کل فایل — مگر اینکه واقعاً لازم باشد.
4. **اگر یک فاز باعث تغییر واقعی در تصمیم معماری شد** (نه فقط جزئیات پیاده‌سازی)، آن تغییر باید در `ARCHITECTURE.md` + یک ADR جدید در `docs/adr/` هم منعکس شود. این فایل جایگزین ADR نیست؛ فقط وضعیت پیاده‌سازی را روایت می‌کند.
5. هر ورودی باید طوری نوشته شود که بدون هیچ زمینه دیگری، فقط با خواندن همان یک ورودی، وضعیت دقیق فهمیده شود و مرحله بعدی قابل شروع باشد.

## قالب هر ورودی

هر فاز جدید را با این ساختار دقیق اضافه کنید:

```markdown
## Phase N — <نام کوتاه فاز>
Status: 🔲 Not Started | 🟡 In Progress | ✅ Complete
Date: <YYYY-MM-DD>
Related roadmap phase: <مثلاً v0.1 — Browser Foundation>

### Goal
یک یا دو جمله: این فاز دقیقاً چه چیزی را می‌سازد و چرا همین الان.

### What was built
لیست دقیق فایل‌ها/ماژول‌ها/کلاس‌هایی که ساخته یا تغییر داده شدند.
توضیح کلی کافی نیست؛ مسیر فایل و نقش آن را بنویسید.
- `core/common/build.gradle.kts` — ماژول خالی، فقط Kotlin، بدون وابستگی Android
- ...

### Deviations from ARCHITECTURE.md
اگر پیاده‌سازی دقیقاً طبق سند معماری بوده: «هیچ.»
اگر چیزی فرق داشته: توضیح بده چرا، و اگر لازم بوده به ADR مرتبط ارجاع بده.

### Known issues / TODO
مشکلات باز یا کارهای ناقصی که عمداً به فاز بعد موکول شدند.

### How to verify
دستور دقیق یا مراحلی که با اجرای آن‌ها می‌شود مطمئن شد این فاز درست کار می‌کند.
مثال: `./gradlew build` باید بدون خطا پاس شود.

### Next phase entry point
فاز بعدی دقیقاً باید از کجا و با چه فرضی شروع کند.
```

---

# Phases

## Phase 0 — Gradle Multi-Module Skeleton
Status: ✅ Complete
Date: 2026-07-08
Related roadmap phase: v0.1 — Browser Foundation

### Goal
ساخت اسکلت خالی همه ماژول‌های Gradle طبق بخش ۳ ARCHITECTURE.md (`core:*` و `feature:*`)، فقط با `build.gradle.kts` و یک کلاس placeholder در هر ماژول — بدون منطق واقعی. هدف: تایید اینکه قانون وابستگی (`feature:*` فقط به `core:*`، بدون وابستگی مستقیم بین ماژول‌های `feature`) بدون وابستگی دایره‌ای قابل ساخت است.

### What was built
- `settings.gradle.kts` — شامل تمام ۱۵ ماژول (۷ تا `core:*`، ۷ تا `feature:*`، یک `app`)
- `build.gradle.kts` (روت) — تعریف نسخه پلاگین‌های AGP 8.5.2 و Kotlin 1.9.24
- `core/common`, `core/domain` — ماژول‌های خالص Kotlin/JVM، بدون وابستگی Android
- `core/data`, `core/ui`, `core/ai-engine`, `core/memory`, `core/agent-runtime` — ماژول‌های Android Library خالی، هرکدام با یک کلاس Placeholder
- `feature/browser-core`, `feature/tabs-manager`, `feature/ai-agent`, `feature/persian-intelligence`, `feature/bookmarks`, `feature/downloads`, `feature/settings` — ماژول‌های Android Library خالی
- `app` — ماژول Android Application، composition root، وابسته به همه ماژول‌های `core:*` و `feature:*`

### Deviations from ARCHITECTURE.md
هیچ. ساختار ماژول‌ها دقیقاً طبق بخش ۳ سند معماری است.

### Known issues / TODO
- Gradle Wrapper در ابتدا وجود نداشت؛ در Android Studio تولید و بعداً commit شد.
- برای رفع خطای «Cannot find a Java installation... languageVersion=17»، پلاگین `org.gradle.toolchains.foojay-resolver-convention` (نسخه 0.8.0) به `settings.gradle.kts` اضافه شد تا Gradle در صورت نبود JDK 17 محلی، خودش دانلودش کند. این پلاگین باید بعد از بلوک `pluginManagement { ... }` قرار بگیرد، نه قبل از آن.

### How to verify
./gradlew build
باید بدون خطا و بدون هیچ وابستگی دایره‌ای پاس شود. (تایید شد: Gradle Sync + Build موفق در Android Studio.)

### Next phase entry point
Phase 1 باید از ماژول `feature:browser-core` و `app` شروع شود تا یک مسیر کامل و نازک (Walking Skeleton) از WebView تا Capability system ساخته و تست شود.

---

## Phase 1 — Walking Skeleton (WebView + Capability System)
Status: ✅ Complete
Date: 2026-07-09
Related roadmap phase: v0.1 — Browser Foundation

### Goal
ساخت نازک‌ترین مسیر ممکن که از همه لایه‌های اصلی رد شود: یک WebView واقعی + یک Capability تستی، تا ثابت شود سیستم Capability (بخش ۴.۲ ARCHITECTURE.md) واقعاً در عمل کار می‌کند، نه فقط روی کاغذ.

### What was built
- `feature/browser-core/.../BrowserCapability.kt` — اینترفیس، دقیقاً مطابق بخش ۴.۲ ARCHITECTURE.md
- `feature/browser-core/.../PageContext.kt` — مدل داده حداقلی (فقط `url`)
- `feature/browser-core/.../PageContent.kt` — مدل داده حداقلی برای محتوای استخراج‌شده (`url`, `title`)
- `feature/browser-core/.../CapabilityRegistry.kt` — نگه‌دارنده لیست Capability ها و صدازننده آن‌ها در رویدادهای `onPageStarted` / `onPageFinished` / `onContentExtracted`
- `feature/browser-core/.../SimorghWebViewClient.kt` — تنها کلاسی که مستقیم با `WebView`/`WebViewClient` کار می‌کند و رویدادها را به `CapabilityRegistry` منتقل می‌کند
- `feature/browser-core/.../LoggingCapability.kt` — یک Capability تستی که فقط با `Log.d` رویدادها را چاپ می‌کند؛ فقط برای اثبات صحت سیستم، نه یک فیچر واقعی
- `feature/browser-core/src/main/AndroidManifest.xml` — افزودن `<uses-permission android:name="android.permission.INTERNET" />`
- `app/.../MainActivity.kt` — یک `WebView` می‌سازد، `CapabilityRegistry` را با `LoggingCapability` مقداردهی می‌کند، و آدرس `https://simorghai.ir` را بارگذاری می‌کند
- `app/src/main/AndroidManifest.xml` — افزودن `MainActivity` به‌عنوان launcher activity
- `app/build.gradle.kts` — افزودن وابستگی‌های `androidx.core:core-ktx:1.13.1` و `androidx.activity:activity-ktx:1.9.0`

### Deviations from ARCHITECTURE.md
هیچ. اینترفیس `BrowserCapability` عیناً مطابق سند است. ثبت Capability فعلاً دستی (در `MainActivity`) انجام می‌شود، نه از طریق Hilt `@IntoSet` — چون DI هنوز در هیچ فازی وایر نشده. این یک محدودیت موقت این فاز است، نه انحراف از تصمیم معماری؛ در فازی که Hilt اضافه می‌شود باید اصلاح شود.

### Known issues / TODO
- ثبت Capability دستی است؛ باید در فاز DI (Hilt) به ثبت خودکار (`@IntoSet`) تبدیل شود.
- فقط یک WebView ثابت وجود دارد؛ نه چندتبی، نه WebView Pool (بخش ۴.۳ ARCHITECTURE.md) — این‌ها موضوع Phase 2 هستند.
- `onContentExtracted` هنوز هیچ‌جا صدا زده نمی‌شود (چون استخراج محتوا هنوز پیاده نشده).

### How to verify
اجرای اپ روی دستگاه/Emulator و بررسی Logcat با فیلتر `CapabilitySystem`. خروجی مورد انتظار:
D/CapabilitySystem: onPageStarted: https://simorghai.ir/
D/CapabilitySystem: onPageFinished: https://simorghai.ir/
(تایید شد: این خروجی دقیقاً روی دستگاه واقعی مشاهده شد.)

### Next phase entry point
Phase 2 باید مدیریت چندتبی و WebView Pool (بخش ۴.۳ ARCHITECTURE.md) را روی همین `feature/browser-core` بسازد: هر تب باید state خودش (URL، عنوان، favicon) را نگه دارد، و تعداد WebView های واقعی هم‌زمان زنده باید محدود باشد. `CapabilityRegistry` و `SimorghWebViewClient` فعلی باید بدون تغییر معماری، فقط برای کار با چند WebView تطبیق داده شوند.
