package com.example.plmarket.player

sealed class PlayerState(val isPLayButtonEnabled: Boolean, val buttonText: String, val progress: String) {

    class Default : PlayerState(false, "PLAY", "00:00")

    class Prepared : PlayerState(true, "PLAY", "00:00")

    class Playing(progress: String) : PlayerState(true, "PAUSE", progress)

    class Paused(progress: String) : PlayerState(true, "PLAY", progress)
}
