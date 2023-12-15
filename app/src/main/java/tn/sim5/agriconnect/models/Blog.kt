package tn.sim5.agriconnect.models

data class Blog(
    val _id: String,
    val titre: String,
    val description: String,
    val lieu: String,
    val image: String?,
    val date: String,
    val prix: Double,
    val jAime: Int,
    val sauvegarde: Int,
    val dislike: Int
) {
}
