package com.example.plmarket.search.data

import com.example.plmarket.search.data.dto.Response

interface NetworkClient {
       fun doRequest(dto: Any): Response
}