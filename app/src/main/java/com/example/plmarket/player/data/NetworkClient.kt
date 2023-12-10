package com.example.plmarket.player.data

import com.example.plmarket.player.data.dto.Response

interface NetworkClient {
       fun doRequest(dto: Any): Response
}