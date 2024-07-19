package com.example.fitfit.data

data class KakaoPayLoad(

    val aid: String,
    val tid: String,
    val cid: String,
    val sid: String,
    val partner_order_id: String,
    val partner_user_id: String,
    val payment_method_type: String,
    val amount: Amount,
    val cardInfo: CardInfo,
    val item_name: String,
    val item_code: String,
    val quantity: String,
    val created_at: String,
    val approved_at: String,
    val payload: String)
