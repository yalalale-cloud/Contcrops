package com.example.model

enum class UserRole(val label: String, val colorHex: Long, val bgHex: Long) {
    SELLER("بائع", 0xFF16A34A, 0xFFE8F5E9),
    BUYER("مشتري", 0xFFF59E0B, 0xFFFFF8E1),
    BROKER("سمسار", 0xFF0EA5E9, 0xFFE3F2FD),
    TRADER("تاجر", 0xFFEC4899, 0xFFFCE4EC),
    COMPANY("شركة", 0xFF7C3AED, 0xFFEDE7F6),
    STATION("محطة", 0xFFEA580C, 0xFFFFF3E0);

    companion object {
        fun fromKey(key: String): UserRole {
            return when (key.lowercase()) {
                "seller" -> SELLER
                "buyer" -> BUYER
                "broker" -> BROKER
                "trader" -> TRADER
                "company" -> COMPANY
                "station" -> STATION
                else -> SELLER
            }
        }
    }
}

data class User(
    val id: String,
    val name: String,
    val role: UserRole,
    val avatar: String,
    val followers: Int,
    val following: Int,
    val bio: String,
    val location: String,
    val gov: String,
    val rating: Double,
    val reviews: Int,
    val verified: Boolean,
    val years: String = ""
)

data class ProductItem(
    val id: String,
    val userId: String,
    val name: String,
    val type: String, // فريش, مجمد, مجفف
    val qty: String,
    val price: String,
    val priceNum: Int,
    val location: String,
    val time: String,
    val broker: Boolean,
    val image: String,
    val harvest: String,
    val cold: Boolean,
    val desc: String
)

data class MarketPriceItem(
    val id: String,
    val name: String,
    val nameEn: String,
    val image: String,
    val min: Int,
    val max: Int,
    val avg: Int,
    val unit: String,
    val change: Double,
    val direction: String, // "up", "down", "stable"
    val lastUpdate: String,
    val markets: Map<String, Pair<Int, Int>>,
    val sparkline: List<Int>,
    val prediction: String, // "up", "down", "stable"
    val bestMarket: String
)

data class FeedPost(
    val id: String,
    val userId: String,
    val text: String,
    val image: String? = null,
    val time: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val views: Int,
    val tag: String,
    val liked: Boolean = false
)

data class SupplyRequest(
    val id: String,
    val userId: String,
    val company: String,
    val verified: Boolean,
    val title: String,
    val qty: String,
    val budget: String,
    val location: String,
    val time: String,
    val offers: Int
)

data class ChatMessage(
    val id: String,
    val isSender: Boolean,
    val text: String,
    val time: String,
    val offer: NegotiationOffer? = null
)

data class NegotiationOffer(
    val cropName: String,
    val qty: String,
    val pricePerTon: String,
    val status: OfferStatus = OfferStatus.PENDING
)

enum class OfferStatus {
    PENDING, ACCEPTED, REJECTED
}

data class FilterState(
    val regions: List<String> = emptyList(),
    val qtyMin: Int = 1,
    val qtyMax: Int = 100,
    val qtyQuick: String = "all", // "all", "lt5", "5-20", "gt20"
    val priceMin: Int = 0,
    val priceMax: Int = 100,
    val priceQuick: String = "all", // "all", "lt10", "10-30", "gt30"
    val rating: Int = 0,
    val productTypes: List<String> = emptyList(),
    val roles: List<UserRole> = emptyList(),
    val delivery: List<String> = emptyList(), // "ready", "cold", "farm"
    val sort: String = "latest" // "latest", "cheapest", "highestRated", "nearest", "largestQty"
)

data class SavedFilter(
    val id: String,
    val name: String,
    val filters: FilterState
)

data class SubscriptionState(
    val active: Boolean = false,
    val plan: String? = null // "prices", "mojaz", "bundle"
)
