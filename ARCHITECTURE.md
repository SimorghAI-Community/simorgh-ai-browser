# معماری # Simorgh AI Research Browser Architecture

این سند مرجع رسمی معماری پروژه است. هدف این سند سه چیز است:

1. هر Contributor جدید بتواند در کمتر از ۳۰ دقیقه بفهمد سیستم چطور کار می‌کند.
2. مرز مسئولیت هر ماژول شفاف باشد تا تغییرات آینده بدون شکستن بقیه سیستم ممکن شود.
3. دلیل تصمیم‌های کلیدی مستند بماند (جزئیات هر تصمیم در `docs/adr/` است؛ این سند فقط خلاصه و نقشه کلی را نشان می‌دهد).

> برای جزئیات *چرایی* هر تصمیم فنی، به پوشه [`docs/adr/`](./docs/adr) مراجعه کنید. این سند *چگونگی* سیستم را توضیح می‌دهد، نه چرایی تک‌تک انتخاب‌ها.

---

## Vision

Simorgh AI Research Browser is an open-source AI-powered research browser designed to help users browse, understand, organize, and interact with web content through AI.

The project focuses on:

- Native Persian AI experience
- Claude-powered intelligence
- AI-assisted research workflows
- Privacy-aware personal memory
- Extensible browser capabilities

The long-term goal is to create an open platform where developers can build new AI-powered browser capabilities through modular plugins.

---

## ۱. فلسفه طراحی

سه اصل بر کل معماری حاکم است و هر PR باید با این سه سازگار باشد:

1. **Modular by Feature** — هر قابلیت یک ماژول Gradle مستقل با API عمومی مشخص است. اضافه‌کردن فیچر جدید یعنی اضافه‌کردن یک ماژول، نه دست‌کاری ماژول‌های موجود.
2. **Core کوچک و پایدار، همه‌چیز دیگر افزونه است** — هسته مرورگر فقط WebView، Navigation و مدیریت تب را می‌شناسد. هوش مصنوعی، Agent، حافظه، لایه فارسی، همگی از بیرون به Core متصل می‌شوند.
3. **Interface-first (Contract-driven)** — هیچ ماژولی مستقیم به پیاده‌سازی ماژول دیگر وابسته نیست؛ همه از طریق Interface تعریف‌شده در `core:domain` یا `core:*` با هم حرف می‌زنند. این یعنی می‌توان دیتابیس، مدل AI، یا موتور جستجو را بدون تغییر در بقیه کد عوض کرد.

اگر تغییری که می‌خواهید بدهید یکی از این سه اصل را نقض می‌کند، احتمالاً باید یک ADR جدید بنویسید و بحث را باز کنید، نه اینکه مستقیم کد بزنید.

---

## ۲. نمای کلی (High-Level Overview)

```
┌──────────────────────────────────────────────────────────────┐
│                    :app  (composition root)                    │
│         DI graph, Navigation host, Application class            │
└──────────────────────────────────────────────────────────────┘
                              │
   ┌──────────────────────────┼──────────────────────────┐
   │                           │                           │
┌──▼───────────┐   ┌──────────▼─────────┐   ┌─────────────▼──────┐
│ UI Features   │   │  Intelligence Layer │   │  Browser Core        │
│ tabs, bookmark│◄──┤  Agent, Memory,      │◄──┤  WebView, Nav,       │
│ downloads,... │   │  Persian layer       │   │  Capability system   │
└───────────────┘   └──────────┬──────────┘   └───────────────────┘
                                │
                    ┌───────────▼───────────┐
                    │    core:data + ai        │
                    │  Room, VectorDB, KG,     │
                    │  AiEngine abstraction     │
                    └────────────────────────┘
```

سیستم پنج لایه دارد که از پایین به بالا وابستگی یک‌طرفه دارند (لایه بالا به پایین وابسته است، نه برعکس):

| لایه | شامل چه ماژول‌هایی | مسئولیت |
|---|---|---|
| ۵. App Shell | `:app` | فقط سیم‌کشی DI و Navigation، بدون منطق |
| ۴. UI Features | `:feature:tabs-manager`, `:feature:bookmarks`, `:feature:downloads`, `:feature:settings`, `:feature:ai-agent` | صفحات و تعامل کاربر |
| ۳. Intelligence | `:core:agent-runtime`, `:core:memory`, `:feature:persian-intelligence` | Agent، حافظه، پردازش هوشمند فارسی |
| ۲. Browser Core | `:feature:browser-core` | WebView، ناوبری، سیستم Capability |
| ۱. Foundation | `:core:common`, `:core:domain`, `:core:data`, `:core:ui`, `:core:ai-engine` | زیرساخت مشترک، بدون وابستگی به Android UI |

---

## ۳. ساختار کامل ماژول‌ها

```
simorgh-browser/
├── core/
│   ├── common/            → DI helpers, logging, Result wrapper, coroutine utils
│   ├── domain/             → UseCase های خالص Kotlin، Entity های تجاری (بدون وابستگی Android)
│   ├── data/                → Room, DataStore, network client پایه، DataSource implementations
│   ├── ui/                  → Design system، تم، فونت فارسی مشترک، کامپوننت‌های Compose
│   ├── ai-engine/           → Abstraction برای مدل‌های زبانی (AiEngine interface + پیاده‌سازی‌ها)
│   ├── memory/              → Memory Manager, Vector Store, Knowledge Graph
│   └── agent-runtime/       → Planner، Tool Registry، Agent Executor
│
├── feature/
│   ├── browser-core/         → WebView Pool، Navigation، Capability Registry
│   ├── tabs-manager/          → UI گرید تب، گروه‌بندی، drag & drop
│   ├── ai-agent/               → UI چت/گزارش Agent، orchestration سطح UI
│   ├── persian-intelligence/   → RTL rendering، خلاصه‌سازی، جستجو، نگارش فارسی
│   ├── bookmarks/               → «سایت‌های من»، دسته‌بندی، پیشنهادی‌ها
│   ├── downloads/                → مدیریت دانلود فایل
│   └── settings/                  → تنظیمات، Feature Flags، حریم خصوصی
│
├── app/                        → Composition root
├── docs/
│   ├── adr/                     → تصمیم‌های معماری مستندشده
│   └── architecture/            → دیاگرام‌های تکمیلی
└── gradle/
```

قانون وابستگی: ماژول‌های `feature:*` می‌توانند به `core:*` وابسته باشند، اما هرگز به یکدیگر مستقیم وابسته نیستند. اگر دو فیچر نیاز به ارتباط دارند (مثلاً `ai-agent` باید از `browser-core` تب باز کند)، این ارتباط از طریق یک Interface در `core:domain` یا `core:agent-runtime` برقرار می‌شود، نه وابستگی مستقیم Gradle بین دو ماژول feature.

---

## ۴. لایه ۱ — Browser Core

### ۴.۱ مسئولیت
مدیریت WebView، ناوبری، و ارائه یک نقطه‌اتصال عمومی (Capability) برای اینکه سایر لایه‌ها بدون دست‌کاری مستقیم WebView به رویدادهای صفحه واکنش نشان دهند.

### ۴.۲ سیستم Capability

هسته انعطاف‌پذیری کل پروژه همین‌جاست. هیچ فیچری (AI، Adblock، Reader Mode، Persian rendering) مستقیم داخل کد WebView نوشته نمی‌شود؛ همه از این قرارداد پیروی می‌کنند:

```kotlin
interface BrowserCapability {
    val id: String
    fun onPageStarted(context: PageContext) {}
    fun onPageFinished(context: PageContext) {}
    fun onContentExtracted(content: PageContent) {}
    fun injectedScript(): String? = null
}
```

`CapabilityRegistry` مجموعه‌ای از این Capability ها را نگه می‌دارد (از طریق DI با `@IntoSet` ثبت می‌شوند) و در نقاط کلیدی چرخه حیات صفحه صدایشان می‌زند. اضافه‌کردن فیچر جدید = نوشتن یک کلاس که این Interface را پیاده‌سازی می‌کند + ثبت در DI. **صفر تغییر در کد Core.**

### ۴.۳ WebView Pool — مدیریت حافظه

برای جلوگیری از کرش با تب‌های زیاد، حداکثر تعداد محدودی WebView واقعی هم‌زمان زنده است (پیش‌فرض قابل تنظیم). تب‌های غیرفعال state خود (`WebView.saveState()`، عنوان، favicon، thumbnail) را در Room ذخیره می‌کنند و WebView واقعی‌شان آزاد می‌شود. جزئیات کامل در `docs/architecture/webview-pool.md`.

### ۴.۴ پایداری در برابر کرش

هر WebView باید `onRenderProcessGone` را override کند تا کرش یک تب باعث کرش کل اپ نشود. این یک الزام غیرقابل‌مذاکره برای هر کدی است که WebView جدید می‌سازد؛ در Code Review باید چک شود.

---

## ۵. لایه ۲ — Foundation (`core:*`)

| ماژول | چه چیزی اینجا **باید** باشد | چه چیزی اینجا **نباید** باشد |
|---|---|---|
| `core:common` | Result wrapper، logging interface، coroutine dispatchers | هیچ منطق تجاری |
| `core:domain` | UseCase های خالص Kotlin، Entity ها، Interface های Repository | هیچ وابستگی به Android SDK یا Room |
| `core:data` | پیاده‌سازی Room، DataStore، DataSource ها | منطق تجاری (باید در UseCase باشد) |
| `core:ui` | تم، فونت فارسی، کامپوننت‌های Compose مشترک | منطق مخصوص یک فیچر خاص |
| `core:ai-engine` | Interface `AiEngine` و پیاده‌سازی‌های آن | هیچ منطق UI |

### ۵.۱ AiEngine — Abstraction اجباری برای هر تعامل با LLM

هیچ ماژولی مجاز نیست مستقیم به یک API خارجی (Claude، OpenAI، مدل on-device) وصل شود. همه از این Interface عبور می‌کنند:

```kotlin
interface AiEngine {
    suspend fun summarize(text: String, lang: Language): Result<String>
    suspend fun chat(messages: List<AiMessage>): Flow<AiStreamChunk>
    suspend fun translate(text: String, target: Language): Result<String>
    suspend fun planWithTools(goal: String, tools: String): List<AgentStep>
}
```

دلیل این الزام: تعویض provider (مثلاً افزودن مدل on-device در کنار Claude، یا Hybrid fallback) باید فقط با نوشتن یک کلاس جدید ممکن باشد، بدون لمس کد مصرف‌کننده. جزئیات در `ADR-0003`.

---

## ۶. لایه ۳ — Intelligence Layer

این لایه سه بخش دارد که هرکدام مستقل‌اند اما با هم ترکیب می‌شوند.

### ۶.۱ `core:agent-runtime` — از دستیار به عامل هوشمند

```kotlin
interface BrowserAgent {
    suspend fun execute(goal: UserGoal, context: BrowserContext): Flow<AgentEvent>
}
```

**اجزای اصلی:**

- **AgentPlanner** — هدف کاربر را با کمک `AiEngine.planWithTools` به مجموعه‌ای از قدم‌ها تبدیل می‌کند (الگوی Tool Use / function calling).
- **AgentTool** — هر ابزار (جستجوی وب، باز کردن صفحه، استخراج محتوا، مقایسه و خلاصه‌سازی) یک پیاده‌سازی از این Interface است. Tool ها از زیرساخت‌های موجود در `browser-core` استفاده می‌کنند (`WebViewPool`, `CapabilityRegistry`) — دوباره‌کاری نمی‌کنند.
- **ToolPolicyGuard** — قبل از اجرای Tool های ریسک‌دار (ارسال فرم، خرید، دانلود)، تایید کاربر را الزامی می‌کند (`AgentEvent.NeedsUserConfirmation`).

خروجی Agent به‌صورت `Flow<AgentEvent>` است، نه یک نتیجه نهایی ساده، چون کاربر باید مراحل اجرا را زنده ببیند.

### ۶.۲ `core:memory` — حافظه

سه نوع حافظه با سه مسئولیت متفاوت:

| نوع | فناوری (فاز اول) | چه سوالی جواب می‌دهد |
|---|---|---|
| Episodic Store | Room | «دیروز چی خواندم؟» |
| Vector Store | sqlite-vec (on-device) | «صفحاتی مشابه این مقاله چه بودند؟» |
| Knowledge Graph | جداول رابطه‌ای در Room | «کاربر در چه حوزه‌ای متخصص/علاقه‌مند است؟» |

```kotlin
interface MemoryManager {
    suspend fun recordVisit(page: PageContent, engagement: EngagementSignal)
    suspend fun recallSimilar(query: String, limit: Int = 5): List<MemoryChunk>
    suspend fun getUserProfile(): UserKnowledgeProfile
    suspend fun updateGraph(entities: List<Entity>, relations: List<Relation>)
}
```

**تصمیم فاز اول:** همه‌چیز on-device (حریم خصوصی، بدون نیاز به بک‌اند). Sync ابری بین دستگاه‌ها به فاز بعدی موکول شده (نگاه کنید `docs/roadmap.md`). دلیل کامل در `ADR-0004`.

Memory از طریق `browser-core` (به‌شکل یک `BrowserCapability` به نام `MemoryCaptureCapability`) با رویدادهای بازدید صفحه تغذیه می‌شود، و توسط Agent برای شخصی‌سازی برنامه‌ریزی مصرف می‌شود.

### ۶.۳ `feature:persian-intelligence` — مزیت رقابتی اصلی

```kotlin
interface PersianIntelligenceService {
    fun renderingRules(): RtlRenderingConfig
    suspend fun summarizePersian(text: String): PersianSummary
    suspend fun searchPersian(query: String): List<SearchResult>
    suspend fun extractPersianContent(html: String): CleanPersianContent
    suspend fun assistWriting(draft: String, tone: WritingTone): WritingSuggestion
}
```

این سرویس در دو نقطه ثبت می‌شود:
1. به‌عنوان `BrowserCapability` — برای رندر هوشمند RTL و استخراج محتوای صفحات فارسی هنگام مرور عادی.
2. به‌عنوان `AgentTool` — تا وقتی Agent به‌صورت خودکار کار می‌کند هم از همان کیفیت پردازش فارسی استفاده کند.

هیچ منطق پردازش فارسی نباید مستقیم در `browser-core` یا `agent-runtime` نوشته شود؛ همیشه باید از این سرویس عبور کند.

---

## ۷. لایه ۴ — UI Features

هر ماژول در `feature/` یک صفحه یا مجموعه صفحات Compose، ViewModel مربوطه، و مسیرهای Navigation خودش را دارد و آن‌ها را در `:app` ثبت می‌کند:

```kotlin
// در feature:ai-agent
fun NavGraphBuilder.aiChatDestination() {
    composable<AiChatScreen> { AiChatScreenContent() }
}

// در :app - فقط جمع می‌کند
NavHost(navController, startDestination = BrowserRoute) {
    browserDestination()
    aiChatDestination()
    tabsGridDestination()
    settingsDestination()
}
```

### ۷.۱ مدیریت State

از الگوی **Unidirectional Data Flow** استفاده می‌شود. یک `BrowserStateHolder` مرکزی، state چند منبع (تب‌ها، گروه‌ها، پنل AI) را با `combine` روی `Flow` ترکیب کرده و به UI می‌دهد. هر بخش UI فقط با Action/Intent این state را تغییر می‌دهد، نه مستقیم.

---

## ۸. جریان کامل یک درخواست نمونه

سناریو: کاربر می‌گوید «مقالات جدید AI پزشکی را پیدا کن و خلاصه کن».

```
feature:ai-agent (UI) → goal گرفته می‌شود
        │
        ▼
core:agent-runtime → AgentPlanner.plan(goal)
        │
        ├─→ core:memory.getUserProfile() → "محقق AI پزشکی، علاقه‌مند به Claude"
        │        (پروفایل به prompt اضافه می‌شود تا جستجو شخصی‌سازی‌شده باشد)
        │
        ├─→ Tool: web_search (با query غنی‌شده)
        ├─→ Tool: open_page × N (از WebViewPool در feature:browser-core)
        ├─→ Tool: extract_content (با persian-intelligence اگر صفحه فارسی باشد)
        ├─→ Tool: compare_and_summarize (با core:ai-engine)
        │
        ▼
AgentEvent.Finished(report) → نمایش در UI
        │
        ▼
core:memory.recordVisit() برای هر صفحه → Knowledge Graph به‌روزرسانی می‌شود
```

هر پیکان در این دیاگرام از طریق یک Interface عبور می‌کند، نه پیاده‌سازی مستقیم — این دقیقاً چیزی است که تعویض هرکدام از این اجزا را در آینده ارزان می‌کند.

---

## ۹. تصمیم‌های فناوری (خلاصه)

جدول کامل و به‌روز در [`docs/tech-stack.md`](./docs/tech-stack.md) نگه‌داری می‌شود. خلاصه فعلی:

| لایه | انتخاب فعلی | ADR |
|---|---|---|
| زبان | Kotlin | ADR-0001 |
| UI | Jetpack Compose | ADR-0001 |
| DI | Hilt | ADR-0001 |
| Async | Kotlin Coroutines + Flow | ADR-0001 |
| ماژول‌بندی | Gradle Multi-module | ADR-0001 |
| سیستم Capability | BrowserCapability interface | ADR-0002 |
| AI Provider | Claude API (از طریق `AiEngine` abstraction) | ADR-0003 |
| دیتابیس محلی | Room | ADR-0004 |
| Vector Store | sqlite-vec (on-device) | ADR-0004 |
| ماژول‌بندی | Gradle Multi-module | ADR-0008 |

**قانون مهم برای Contributor ها:** هیچ وابستگی بیرونی جدید (خصوصاً دیتابیس، DI، شبکه، یا AI provider) بدون یک ADR همراه در همان PR پذیرفته نمی‌شود. الگو در `docs/adr/template.md` موجود است.

---

## ۱۰. قوانین Contract Testing

چون معماری کاملاً Interface-first است، هر پیاده‌سازی جدید از یک Interface کلیدی (`TabDataSource`, `AiEngine`, `AgentTool`, `MemoryManager`) باید یک مجموعه تست مشترک و مستقل از پیاده‌سازی را پاس کند:

```kotlin
abstract class TabDataSourceContractTest {
    abstract fun createDataSource(): TabDataSource
    @Test fun `saving and retrieving a tab works`() { ... }
}

class RoomTabDataSourceTest : TabDataSourceContractTest() {
    override fun createDataSource() = LocalTabDataSource(testDb.tabDao())
}
```

این یعنی تعویض پیاده‌سازی (مثلاً تعویض دیتابیس یا افزودن یک AiEngine جدید) با اطمینان از حفظ رفتار درست ممکن است، بدون بحث دوباره درباره‌ی «آیا این تغییر چیزی را می‌شکند؟».

---

## ۱۱. نقشه راه فازبندی‌شده

جزئیات کامل در [`docs/roadmap.md`](./docs/roadmap.md). خلاصه:

- **v0.1 — Core:** WebView Pool، Capability system، گروه‌بندی تب، فونت فارسی
- **v0.2 — Intelligence پایه:** `AiEngine` + اتصال Claude، `persian-intelligence` (خلاصه‌سازی و RTL)
- **v0.3 — Agent:** `agent-runtime` با ابزارهای پایه، Memory (فقط Episodic)
- **v1.0 — چشم‌انداز کامل:** Vector Memory + Knowledge Graph، Agent با ابزارهای کامل، Sync (اختیاری)

---

## ۱۲. برای Contributor های جدید — از کجا شروع کنم؟

1. این سند و `docs/tech-stack.md` را بخوانید.
2. `CONTRIBUTING.md` را برای راه‌اندازی محیط ببینید.
3. یک Issue با برچسب `good first issue` را انتخاب کنید — این‌ها معمولاً یک `BrowserCapability` یا `AgentTool` کوچک و مستقل هستند که نیاز به درک کل سیستم ندارند.
4. قبل از هر PR که ماژول جدید یا وابستگی جدید اضافه می‌کند، یک ADR بنویسید یا در Issue مربوطه بحث را باز کنید.

اگر بخشی از این سند نامفهوم یا ناقص است، خودش یک Issue خوب برای بهبود مستندات است.
