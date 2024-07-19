package com.example.fitfit.data

data class PaymentReadyRequest(

    val cid: String,
    val partner_order_id: String,
    val partner_user_id: String,
    val item_name: String,
    val quantity: Int,
    val total_amount: Int,
    val vat_amount: Int,
    val tax_free_amount: Int,
    val approval_url: String,
    val fail_url: String,
    val cancel_url: String)