package com.example.fitfit.data

data class PaymentApproveRequest(val cid: String,
                                 val tid: String,
                                 val partner_order_id: String,
                                 val partner_user_id: String,
                                 val pg_token: String)


