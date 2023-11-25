package com.example.pl_market.data

import com.example.pl_market.data.dto.Response

interface NetworkClient {
    var resultCode: Int

    fun doRequest(dto: Any): Response
}