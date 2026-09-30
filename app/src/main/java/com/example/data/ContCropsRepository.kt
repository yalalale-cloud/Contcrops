package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContCropsRepository {

    private val initialUsers = listOf(
        User("u1", "الحاج سعيد", UserRole.SELLER, "س", 342, 89, "مزارع طماطم بلدي وفلفل من 20 سنة. فرز يدوي وتعبئة برانيك. توصيل مبرد لجميع المحافظات.", "البحيرة - بدر", "البحيرة", 4.9, 124, true, "20 سنة خبرة"),
        User("u2", "محطة الفجر", UserRole.STATION, "ف", 521, 45, "محطة تبريد وتجميد IQF، سعة 500 طن، شهادات EU، تصدير أوروبا والخليج.", "القليوبية - طوخ", "القليوبية", 4.8, 210, true, "12 سنة"),
        User("u3", "شركة النيل", UserRole.COMPANY, "ن", 890, 120, "تصدير مجففات وخضار مجفف، تعبئة 25ك، عقود توريد للسعودية والإمارات.", "سوهاج - المنشأة", "سوهاج", 4.7, 98, true),
        User("u4", "مزرعة الواحة", UserRole.SELLER, "و", 410, 67, "مانجو عويسي وزبدية، مزارع الإسماعيلية، نسبة سكريات عالية، فرز تصدير.", "الإسماعيلية - فايد", "الإسماعيلية", 5.0, 76, true),
        User("u5", "أبو كريم", UserRole.TRADER, "ك", 298, 102, "تاجر جملة بطاطس وبصل، توريد مصانع ومطاعم، نقل مبرد يومي.", "المنيا - سمالوط", "المنيا", 4.6, 54, false),
        User("u6", "أبو علي السمسار", UserRole.BROKER, "ع", 765, 210, "سمسار معتمد ContCrops، أربط بين الفلاح والمصنع بعمولة 2%، متابعة حتى التحصيل.", "البحيرة - كفر الدوار", "البحيرة", 4.9, 312, true),
        User("u7", "دلتا فريش", UserRole.COMPANY, "د", 634, 78, "شركة تصدير برتقال وفراولة، ميناء دمياط، تبريد وفرز آلي.", "الشرقية - الزقازيق", "الشرقية", 4.8, 145, true),
        User("u8", "مزارع النور", UserRole.SELLER, "ن", 189, 34, "رمان منفلوطي وموز صعيدي، زراعة عضوية بدون مبيدات.", "المنيا - ملوي", "المنيا", 4.5, 41, false),
        User("u9", "خليج الخضار", UserRole.TRADER, "خ", 423, 95, "توريد خضار للفنادق بالقاهرة، خيار وفلفل ألوان يومي فريش.", "القاهرة - العبور", "القاهرة", 4.6, 88, false),
        User("u10", "تاجر الإسكندرية", UserRole.BUYER, "إ", 276, 156, "مشتري جملة، أبحث عن طماطم وبطاطس تحمير بكميات كبيرة.", "الإسكندرية - العامرية", "الإسكندرية", 4.4, 33, false),
        User("u11", "مزرعة السلام", UserRole.SELLER, "س", 332, 56, "جوافة بناتي وبرتقال صيفي، الشرقية، حصاد يومي.", "الشرقية - بلبيس", "الشرقية", 4.7, 67, true),
        User("u12", "محطة الصحراء", UserRole.STATION, "ص", 498, 62, "تجميد خضار نصف مقلي وبطاطس، سعة 800 طن، تبريد -18.", "الإسكندرية - برج العرب", "الإسكندرية", 4.8, 102, true)
    )

    private val initialProducts = listOf(
        ProductItem("p1", "u1", "طماطم بلدي درجة أولى", "فريش", "15 طن", "8,500 ج / طن", 8500, "البحيرة - بدر", "منذ 20 دقيقة", true, "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&h=600&fit=crop", "15 مايو 2025", true, "طماطم ممتازة للتصدير والتصنيع، فرز يدوي، تعبئة في برانيك"),
        ProductItem("p2", "u1", "طماطم شيري فاخر", "فريش", "3 طن", "12,000 ج / طن", 12000, "البحيرة - بدر", "منذ ساعتين", false, "https://images.unsplash.com/photo-1561136594-7f68413baa99?w=600&h=600&fit=crop", "14 مايو 2025", true, "شيري تصدير، عبوات 5ك"),
        ProductItem("p3", "u2", "فراولة مجمدة IQF", "مجمد", "20 طن", "22,000 ج / طن", 22000, "القليوبية - طوخ", "منذ ساعة", false, "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=600&h=600&fit=crop", "10 مايو 2025", true, "فراولة مجمدة سريعة، تصدير أوروبا"),
        ProductItem("p4", "u2", "فراولة فريش تصدير", "فريش", "5 طن", "18,000 ج / طن", 18000, "القليوبية - طوخ", "منذ 4 ساعات", false, "https://images.unsplash.com/photo-1543528176-91ff522eb95e?w=600&h=600&fit=crop", "12 مايو 2025", true, "فراولة فريش درجة أولى"),
        ProductItem("p5", "u3", "بصل مجفف بودر", "مجفف", "5 طن", "35,000 ج / طن", 35000, "سوهاج - المنشأة", "منذ 3 ساعات", false, "https://images.unsplash.com/photo-1518977676608-bd36c2ca2f2e?w=600&h=600&fit=crop", "01 مايو 2025", false, "بصل مجفف عالي النقاء"),
        ProductItem("p6", "u3", "طماطم مجففة", "مجفف", "2 طن", "42,000 ج / طن", 42000, "سوهاج", "منذ يوم", false, "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&h=600&fit=crop", "28 أبريل", false, "طماطم مجففة شمس"),
        ProductItem("p7", "u4", "مانجو عويسي فاخر", "فريش", "8 طن", "28,000 ج / طن", 28000, "الإسماعيلية - فايد", "منذ 5 ساعات", true, "https://images.unsplash.com/photo-1553279768-865429fa0078?w=600&h=600&fit=crop", "12 مايو 2025", true, "مانجو عويسي تصدير"),
        ProductItem("p8", "u4", "مانجو زبدية", "فريش", "12 طن", "19,000 ج / طن", 19000, "الإسماعيلية", "منذ 6 ساعات", true, "https://images.unsplash.com/photo-1628689469838-524a4a973b8e?w=600&h=600&fit=crop", "11 مايو", true, "زبدية عصير"),
        ProductItem("p9", "u5", "بطاطس تحمير سبونتا", "فريش", "40 طن", "7,200 ج / طن", 7200, "المنيا - سمالوط", "منذ 6 ساعات", false, "https://images.unsplash.com/photo-1518977676608-bd36c2ca2f2e?w=600&h=600&fit=crop", "08 مايو", true, "تحمير حجم موحد"),
        ProductItem("p10", "u5", "بصل أحمر", "فريش", "25 طن", "6,800 ج / طن", 6800, "المنيا", "منذ يوم", false, "https://images.unsplash.com/photo-1508747703725-719777637510?w=600&h=600&fit=crop", "07 مايو", false, "بصل أحمر فريش"),
        ProductItem("p11", "u7", "برتقال صيفي", "فريش", "30 طن", "9,500 ج / طن", 9500, "الشرقية - الزقازيق", "منذ 30 دقيقة", true, "https://images.unsplash.com/photo-1611080626919-7cf5a9dbab5b?w=600&h=600&fit=crop", "13 مايو", true, "برتقال صيفي تصدير"),
        ProductItem("p12", "u8", "رمان منفلوطي", "فريش", "10 طن", "14,000 ج / طن", 14000, "المنيا - ملوي", "منذ 7 ساعات", false, "https://images.unsplash.com/photo-1615484477778-ca3b779589f1?w=600&h=600&fit=crop", "09 مايو", false, "رمان فاخر"),
        ProductItem("p13", "u9", "خيار بلدي", "فريش", "6 طن", "8,000 ج / طن", 8000, "القاهرة - العبور", "منذ 45 دقيقة", false, "https://images.unsplash.com/photo-1449300079323-02e209d9d3a6?w=600&h=600&fit=crop", "15 مايو", true, "خيار فريش يومي"),
        ProductItem("p14", "u10", "فلفل ألوان", "فريش", "4 طن", "15,000 ج / طن", 15000, "الإسكندرية", "منذ ساعة", false, "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=600&h=600&fit=crop", "14 مايو", false, "فلفل ألوان تصدير"),
        ProductItem("p15", "u11", "جوافة بناتي", "فريش", "9 طن", "11,000 ج / طن", 11000, "الشرقية - بلبيس", "منذ 2 ساعة", false, "https://images.unsplash.com/photo-1536511132770-e5058c7e9c01?w=600&h=600&fit=crop", "13 مايو", false, "جوافة فاخرة"),
        ProductItem("p16", "u12", "بطاطس مجمدة نصف مقلية", "مجمد", "50 طن", "18,500 ج / طن", 18500, "الإسكندرية - برج العرب", "منذ 3 ساعات", false, "https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?w=600&h=600&fit=crop", "10 مايو", true, "نصف مقلية جاهزة"),
        ProductItem("p17", "u8", "موز صعيدي", "فريش", "12 طن", "10,500 ج / طن", 10500, "المنيا - ملوي", "منذ 5 ساعات", false, "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=600&h=600&fit=crop", "12 مايو", false, "موز بلدي"),
        ProductItem("p18", "u9", "عنب كريمسون", "فريش", "7 طن", "16,000 ج / طن", 16000, "القاهرة", "منذ 8 ساعات", false, "https://images.unsplash.com/photo-1596363505729-4190a9506133?w=600&h=600&fit=crop", "11 مايو", true, "عنب تصدير")
    )

    private val initialMarketPrices = listOf(
        MarketPriceItem("m1", "طماطم بلدي", "Tomato", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=200&h=200&fit=crop", 6500, 8500, 7500, "طن", -5.2, "down", "اليوم 08:30 ص", mapOf("سوق العبور" to Pair(6500, 8000), "سوق البحيرة" to Pair(6000, 8500), "سوق الشرقية" to Pair(6700, 8200), "سوق 6 أكتوبر" to Pair(7000, 9000)), listOf(8200, 8100, 7950, 8000, 7800, 7600, 7500), "down", "سوق البحيرة"),
        MarketPriceItem("m2", "بطاطس تحمير", "Potato", "https://images.unsplash.com/photo-1518977676608-bd36c2ca2f2e?w=200&h=200&fit=crop", 7000, 9200, 8100, "طن", 3.8, "up", "اليوم 08:15 ص", mapOf("سوق العبور" to Pair(7000, 8500), "سوق البحيرة" to Pair(6800, 8000), "سوق الشرقية" to Pair(7200, 9200), "سوق 6 أكتوبر" to Pair(7500, 9000)), listOf(7500, 7600, 7700, 7900, 8000, 8050, 8100), "up", "سوق البحيرة"),
        MarketPriceItem("m3", "بصل أحمر", "Onion", "https://images.unsplash.com/photo-1508747703725-719777637510?w=200&h=200&fit=crop", 5500, 7200, 6300, "طن", 1.2, "up", "اليوم 07:45 ص", mapOf("سوق العبور" to Pair(5500, 7000), "سوق البحيرة" to Pair(5200, 6800), "سوق الشرقية" to Pair(5800, 7200), "سوق 6 أكتوبر" to Pair(6000, 7500)), listOf(6000, 6100, 6200, 6150, 6250, 6300, 6300), "stable", "سوق البحيرة"),
        MarketPriceItem("m4", "مانجو عويسي", "Mango", "https://images.unsplash.com/photo-1553279768-865429fa0078?w=200&h=200&fit=crop", 24000, 32000, 28000, "طن", 8.5, "up", "اليوم 09:00 ص", mapOf("سوق العبور" to Pair(26000, 32000), "سوق البحيرة" to Pair(24000, 30000), "سوق الشرقية" to Pair(25000, 31000), "سوق 6 أكتوبر" to Pair(27000, 33000)), listOf(25000, 25500, 26000, 27000, 27500, 28200, 28000), "up", "سوق العبور"),
        MarketPriceItem("m5", "فراولة فريش", "Strawberry", "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=200&h=200&fit=crop", 16000, 21000, 18500, "طن", -2.1, "down", "اليوم 06:30 ص", mapOf("سوق العبور" to Pair(17000, 21000), "سوق البحيرة" to Pair(16000, 19000), "سوق الشرقية" to Pair(16500, 20000), "سوق 6 أكتوبر" to Pair(18000, 22000)), listOf(19500, 19200, 19000, 18800, 18600, 18400, 18500), "down", "سوق البحيرة"),
        MarketPriceItem("m6", "فراولة مجمدة", "Frozen", "https://images.unsplash.com/photo-1543528176-91ff522eb95e?w=200&h=200&fit=crop", 20000, 25000, 22500, "طن", 0.8, "up", "اليوم 08:00 ص", mapOf("سوق العبور" to Pair(21000, 25000), "سوق البحيرة" to Pair(20000, 23000), "سوق الشرقية" to Pair(20500, 24000), "سوق 6 أكتوبر" to Pair(22000, 26000)), listOf(22000, 22100, 22200, 22300, 22400, 22450, 22500), "stable", "سوق البحيرة"),
        MarketPriceItem("m7", "بصل مجفف", "Dried Onion", "https://images.unsplash.com/photo-1518977676608-bd36c2ca2f2e?w=200&h=200&fit=crop", 32000, 38000, 35000, "طن", 2.5, "up", "اليوم 07:00 ص", mapOf("سوق العبور" to Pair(33000, 38000), "سوق البحيرة" to Pair(32000, 36000), "سوق الشرقية" to Pair(32500, 37000), "سوق 6 أكتوبر" to Pair(34000, 39000)), listOf(33500, 34000, 34200, 34500, 34800, 35000, 35000), "up", "سوق البحيرة"),
        MarketPriceItem("m8", "بطاطس نصف مقلية", "Semi-fried", "https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?w=200&h=200&fit=crop", 17000, 19500, 18250, "طن", -1.1, "down", "اليوم 08:45 ص", mapOf("سوق العبور" to Pair(17500, 19500), "سوق البحيرة" to Pair(17000, 19000), "سوق الشرقية" to Pair(17200, 19200), "سوق 6 أكتوبر" to Pair(18000, 20000)), listOf(18600, 18500, 18400, 18300, 18200, 18250, 18250), "stable", "سوق البحيرة")
    )

    private val initialPosts = listOf(
        FeedPost("j1", "u6", "انخفاض أسعار الطماطم اليوم في البحيرة بسبب زيادة المعروض - وصلت لـ 6000 جنيه للطن في بدر. فرصة للمصانع!", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&h=400&fit=crop", "منذ 15 دقيقة", 42, 12, 8, 342, "أسعار اليوم"),
        FeedPost("j2", "u2", "تم افتتاح محطة تبريد جديدة في مدينة بدر بطاقة 800 طن، بتدعم IQF وتبريد -18. هنبدأ استلام فراولة الأسبوع الجاي 🔥", null, "منذ ساعة", 89, 23, 15, 521, "تبريد وتخزين", true),
        FeedPost("j3", "u7", "تصدير 100 طن فراولة مجمدة للسعودية اليوم من ميناء دمياط - بفضل جودة الفرز في دلتا فريش. عقبال باقي الشحنات 🇸🇦", "https://images.unsplash.com/photo-1543528176-91ff522eb95e?w=600&h=400&fit=crop", "منذ ساعتين", 124, 34, 28, 890, "تصدير"),
        FeedPost("j4", "u1", "نصيحة للمزارعين: الري في الجو الحر ده يكون الفجر والمغرب بس، بلاش الظهر. الطماطم بتحتاج انتظام ري عشان متشققش.", null, "منذ 3 ساعات", 56, 18, 22, 412, "نصائح زراعية"),
        FeedPost("j5", "u3", "سعر البصل المجفف طلع لـ 35 ألف جنيه، الطلب عالي من الخليج. اللي عنده بضاعة يكلمني - تعاقد فوري والدفع كاش", "https://images.unsplash.com/photo-1518977676608-bd36c2ca2f2e?w=600&h=400&fit=crop", "منذ 4 ساعات", 67, 29, 12, 445, "فرص تصدير"),
        FeedPost("j6", "u4", "موسم المانجو العويسي بدأ بدري السنة دي - الجودة ممتازة والسكريات عالية. متوقع زيادة 20% في الإنتاج مقارنة بالسنة اللي فاتت 🥭", null, "منذ 5 ساعات", 98, 41, 19, 678, "موسم جديد"),
        FeedPost("j7", "u5", "تنبيه: طريق إسكندرية الصحراوي مقفول بسبب صيانة - النقل المبرد يحول على طريق وادي النطرون. التوصيل هيتأخر ساعتين", null, "منذ 6 ساعات", 34, 8, 45, 312, "تنبيهات نقل")
    )

    private val initialRequests = listOf(
        SupplyRequest("r1", "u7", "دلتا فريش", true, "مطلوب 20 طن فراولة مجمدة للتصدير", "20 طن", "حتى 24,000 ج/طن", "دمياط", "منذ ساعتين", 12),
        SupplyRequest("r2", "u8", "مزارع النور", true, "مطلوب 50 طن طماطم للتصنيع", "50 طن", "حتى 7,000 ج/طن", "6 أكتوبر", "منذ 5 ساعات", 8),
        SupplyRequest("r3", "u9", "خليج الخضار", false, "مطلوب بصل مجفف 10 طن - السعودية", "10 طن", "مفتوح", "سفاجا", "منذ يوم", 3),
        SupplyRequest("r4", "u2", "محطة الفجر", true, "مطلوب تبريد 100 طن مانجو", "100 طن", "خدمة", "القليوبية", "منذ 3 ساعات", 5)
    )

    private val _currentUserId = MutableStateFlow("u1")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _users = MutableStateFlow(initialUsers)
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _followingIds = MutableStateFlow(setOf("u2", "u4", "u7"))
    val followingIds: StateFlow<Set<String>> = _followingIds.asStateFlow()

    private val _products = MutableStateFlow(initialProducts)
    val products: StateFlow<List<ProductItem>> = _products.asStateFlow()

    private val _marketPrices = MutableStateFlow(initialMarketPrices)
    val marketPrices: StateFlow<List<MarketPriceItem>> = _marketPrices.asStateFlow()

    private val _posts = MutableStateFlow(initialPosts)
    val posts: StateFlow<List<FeedPost>> = _posts.asStateFlow()

    private val _requests = MutableStateFlow(initialRequests)
    val requests: StateFlow<List<SupplyRequest>> = _requests.asStateFlow()

    private val _subscription = MutableStateFlow(SubscriptionState(active = false, plan = null))
    val subscription: StateFlow<SubscriptionState> = _subscription.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _savedFilters = MutableStateFlow<List<SavedFilter>>(emptyList())
    val savedFilters: StateFlow<List<SavedFilter>> = _savedFilters.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("c1", false, "السلام عليكم يا حاج سعيد، المحصول جاهز للتحميل بكرة؟", "10:30 ص"),
            ChatMessage("c2", true, "وعليكم السلام يا باشا، جاهز 15 طن فرز أول، تبريد جاهز ومفروز يدوي.", "10:31 ص"),
            ChatMessage("c3", false, "تمام، محتاجين نثبت السعر للتوريد.", "10:32 ص", NegotiationOffer("طماطم بلدي درجة أولى", "15 طن", "8,300 ج/طن", OfferStatus.PENDING))
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun setCurrentUser(userId: String) {
        _currentUserId.value = userId
    }

    fun toggleFollow(targetUserId: String) {
        val current = _currentUserId.value
        if (targetUserId == current) return
        val isFollowing = _followingIds.value.contains(targetUserId)

        _followingIds.update { set ->
            if (isFollowing) set - targetUserId else set + targetUserId
        }

        _users.update { list ->
            list.map { u ->
                when (u.id) {
                    targetUserId -> u.copy(followers = u.followers + if (isFollowing) -1 else 1)
                    current -> u.copy(following = u.following + if (isFollowing) -1 else 1)
                    else -> u
                }
            }
        }
    }

    fun toggleLikePost(postId: String) {
        _posts.update { list ->
            list.map { p ->
                if (p.id == postId) {
                    val newLiked = !p.liked
                    p.copy(liked = newLiked, likes = p.likes + if (newLiked) 1 else -1)
                } else p
            }
        }
    }

    fun addPost(text: String, tag: String = "خبر جديد") {
        val user = _users.value.find { it.id == _currentUserId.value } ?: return
        val newPost = FeedPost(
            id = "j${System.currentTimeMillis()}",
            userId = user.id,
            text = text,
            image = null,
            time = "الآن",
            likes = 0,
            comments = 0,
            shares = 0,
            views = 1,
            tag = tag,
            liked = false
        )
        _posts.update { listOf(newPost) + it }
    }

    fun addProduct(name: String, type: String, qty: String, price: String, desc: String, cold: Boolean = true) {
        val user = _users.value.find { it.id == _currentUserId.value } ?: return
        val priceInt = price.filter { it.isDigit() }.toIntOrNull() ?: 8000
        val newProduct = ProductItem(
            id = "p${System.currentTimeMillis()}",
            userId = user.id,
            name = name,
            type = type,
            qty = qty,
            price = "$price ج / طن",
            priceNum = priceInt,
            location = user.location,
            time = "الآن",
            broker = false,
            image = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=600&h=600&fit=crop",
            harvest = "اليوم",
            cold = cold,
            desc = if (desc.isBlank()) "محصول عالي الجودة مضاف عبر منصة ContCrops" else desc
        )
        _products.update { listOf(newProduct) + it }
    }

    fun setSubscription(active: Boolean, plan: String?) {
        _subscription.value = SubscriptionState(active = active, plan = plan)
    }

    fun updateFilter(filter: FilterState) {
        _filterState.value = filter
    }

    fun resetFilter() {
        _filterState.value = FilterState()
    }

    fun saveCurrentFilter(name: String) {
        val newSaved = SavedFilter(
            id = "sf_${System.currentTimeMillis()}",
            name = name,
            filters = _filterState.value
        )
        _savedFilters.update { it + newSaved }
    }

    fun sendChatMessage(text: String) {
        val msg = ChatMessage(
            id = "c_${System.currentTimeMillis()}",
            isSender = true,
            text = text,
            time = "الآن"
        )
        _chatMessages.update { it + msg }
    }

    fun respondToOffer(messageId: String, accept: Boolean) {
        _chatMessages.update { list ->
            list.map { m ->
                if (m.id == messageId && m.offer != null) {
                    m.copy(offer = m.offer.copy(status = if (accept) OfferStatus.ACCEPTED else OfferStatus.REJECTED))
                } else m
            }
        }
    }
}
